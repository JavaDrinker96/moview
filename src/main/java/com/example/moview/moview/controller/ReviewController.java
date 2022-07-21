package com.example.moview.moview.controller;

import com.example.moview.moview.dto.review.ReviewCreateDto;
import com.example.moview.moview.dto.review.ReviewDto;
import com.example.moview.moview.dto.review.ReviewUpdateDto;
import com.example.moview.moview.exception.ForbiddenAuthorException;
import com.example.moview.moview.exception.UnauthorizedAuthorException;
import com.example.moview.moview.model.Review;
import com.example.moview.moview.model.User;
import com.example.moview.moview.service.ReviewService;
import com.example.moview.moview.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public ReviewController(final ReviewService reviewService,
                            final UserService userService,
                            final ModelMapper modelMapper) {

        this.reviewService = reviewService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @RequestMapping(value = "/review", method = RequestMethod.POST)
    public ResponseEntity<ReviewDto> create(@RequestHeader("Authorization") final String userId,
                                            @RequestBody @Valid final ReviewCreateDto dto) {

        final Long authorId = Long.valueOf(userId);
        validateUserExisting(authorId);

        final Review review = modelMapper.map(dto, Review.class);
        review.setAuthor(User.builder().id(authorId).build());
        final Review createdReview = reviewService.create(review);
        final ReviewDto dtoCreated = modelMapper.map(createdReview, ReviewDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(dtoCreated);
    }

    @RequestMapping(value = "/review", method = RequestMethod.PUT)
    public ResponseEntity<ReviewDto> update(@RequestHeader("Authorization") final String userId,
                                            @RequestBody @Valid final ReviewUpdateDto dto) {

        final Long authorId = Long.valueOf(userId);
        final Long reviewId = dto.getId();
        validateAuthor(authorId, reviewId);

        final Review review = modelMapper.map(dto, Review.class);
        final Review updatedReview = reviewService.update(review);
        final ReviewDto updatedDto = modelMapper.map(updatedReview, ReviewDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
    }

    @RequestMapping(value = "/review/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestHeader("Authorization") final String userId, @PathVariable Long id) {

        final Long authorId = Long.valueOf(userId);
        validateAuthor(authorId, id);

        reviewService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private void validateAuthor(final Long authorId, final Long reviewId) {
        validateUserExisting(authorId);
        validateAuthorsPermissionToInteract(authorId, reviewId);
    }

    private void validateAuthorsPermissionToInteract(final Long authorId, final Long reviewId) {
        final Long foundAuthorId = reviewService.getAuthorId(reviewId);
        if (!foundAuthorId.equals(authorId)) {
            throw new ForbiddenAuthorException(
                    String.format(String.format("A user with id = %d cannot interact with a review with id = %d, " +
                            "because he did not create it.", authorId, reviewId))
            );
        }
    }

    private void validateUserExisting(final Long authorId) {
        final boolean exist = userService.readAll().stream().anyMatch(user -> user.getId().equals(authorId));
        if (!exist) {
            throw new UnauthorizedAuthorException(
                    String.format("User with id = %s is not exist in the database", authorId)
            );
        }
    }
}