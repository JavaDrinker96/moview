package com.example.moview.validator;

import com.example.moview.exception.ContentPermissionException;
import com.example.moview.exception.ForbiddenUserException;
import com.example.moview.repository.MovieRepository;
import com.example.moview.security.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MovieValidator {
    private final MovieRepository movieRepository;
    private final UserValidator userValidator;

    public void validateUserPermissionToInteract(final UserPrincipal userPrincipal, final Long movieId) {

        final Long foundAuthorId = movieRepository.findUserIdByMovieId(movieId)
                .orElseThrow(() -> new ContentPermissionException(
                        String.format("Can't find user id for movie with id %d. Most likely there is no such movie", movieId))
                );

        final Long userId = userPrincipal.getId();
        if (!foundAuthorId.equals(userId)) {

            throw new ForbiddenUserException(
                    String.format("A user with id %d cannot modify movie with id %d, " +
                            "because he did not create it.", userId, movieId)
            );
        }
    }
}
