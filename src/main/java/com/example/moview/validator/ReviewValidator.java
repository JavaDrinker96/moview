package com.example.moview.validator;

import com.example.moview.exception.ForbiddenUserException;
import com.example.moview.repository.ReviewRepository;
import com.example.moview.security.UserPrincipal;
import org.springframework.stereotype.Component;

@Component
public class ReviewValidator {

    private final ReviewRepository reviewRepository;
    private final UserValidator userValidator;

    public ReviewValidator(final ReviewRepository reviewRepository, final UserValidator userValidator) {
        this.reviewRepository = reviewRepository;
        this.userValidator = userValidator;
    }

    public void validateAuthor(final UserPrincipal userPrincipal, final Long reviewId) {

        userValidator.validateUserExisting(userPrincipal);
        validateAuthorsPermissionToInteract(userPrincipal.getId(), reviewId);
    }

    private void validateAuthorsPermissionToInteract(final Long authorId, final Long reviewId) {

        final Long foundAuthorId = reviewRepository.getAuthorIdById(reviewId);

        if (!foundAuthorId.equals(authorId)) {

            throw new ForbiddenUserException(
                    String.format("A user with id %d cannot interact with a review with id %d, " +
                            "because he did not create it.", authorId, reviewId)
            );
        }
    }
}