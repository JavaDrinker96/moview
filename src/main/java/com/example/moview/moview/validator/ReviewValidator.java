package com.example.moview.moview.validator;

import com.example.moview.moview.exception.ForbiddenAuthorException;
import com.example.moview.moview.repository.ReviewRepository;
import org.springframework.stereotype.Component;

@Component
public class ReviewValidator {

    private final ReviewRepository reviewRepository;
    private final UserValidator userValidator;

    public ReviewValidator(final ReviewRepository reviewRepository, final UserValidator userValidator) {
        this.reviewRepository = reviewRepository;
        this.userValidator = userValidator;
    }

    public void validateAuthor(final Long authorId, final Long reviewId) {
        userValidator.validateUserExisting(authorId);
        validateAuthorsPermissionToInteract(authorId, reviewId);
    }

    private void validateAuthorsPermissionToInteract(final Long authorId, final Long reviewId) {
        final Long foundAuthorId = reviewRepository.getAuthorIdById(reviewId);
        if (!foundAuthorId.equals(authorId)) {
            throw new ForbiddenAuthorException(
                    String.format("A user with id %d cannot interact with a review with id %d, " +
                            "because he did not create it.", authorId, reviewId)
            );
        }
    }
}