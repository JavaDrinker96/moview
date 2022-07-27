package com.example.moview.moview.controller;

import com.example.moview.moview.dto.review.ReviewCreateDto;
import com.example.moview.moview.dto.review.ReviewDto;
import com.example.moview.moview.dto.review.ReviewUpdateDto;
import com.example.moview.moview.mapper.ReviewMapper;
import com.example.moview.moview.model.Review;
import com.example.moview.moview.model.User;
import com.example.moview.moview.service.ReviewService;
import com.example.moview.moview.validator.ReviewValidator;
import com.example.moview.moview.validator.UserValidator;
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

    private final ReviewMapper reviewMapper;
    private final UserValidator userValidator;
    private final ReviewValidator reviewValidator;

    @Autowired
    public ReviewController(final ReviewService reviewService,
                            final ReviewMapper reviewMapper,
                            final UserValidator userValidator,
                            final ReviewValidator reviewValidator) {

        this.reviewService = reviewService;
        this.reviewMapper = reviewMapper;
        this.userValidator = userValidator;
        this.reviewValidator = reviewValidator;
    }

    @RequestMapping(value = "/review", method = RequestMethod.POST)
    public ResponseEntity<ReviewDto> create(@RequestHeader("Authorization") final Long userId,
                                            @RequestBody @Valid final ReviewCreateDto dto) {

        userValidator.validateUserExisting(userId);
        final Review review = reviewMapper.createDtoToModel(dto);
        review.setAuthor(User.builder().id(userId).build());
        final Review createdReview = reviewService.create(review);
        final ReviewDto dtoCreated = reviewMapper.modelToDto(createdReview);
        return ResponseEntity.status(HttpStatus.OK).body(dtoCreated);
    }

    @RequestMapping(value = "/review", method = RequestMethod.PUT)
    public ResponseEntity<ReviewDto> update(@RequestHeader("Authorization") final Long userId,
                                            @RequestBody @Valid final ReviewUpdateDto dto) {

        reviewValidator.validateAuthor(userId, dto.getId());
        final Review review = reviewMapper.updateDtoToModel(dto);
        review.setAuthor(User.builder().id(userId).build());
        final Review updatedReview = reviewService.update(review);
        final ReviewDto updatedDto = reviewMapper.modelToDto(updatedReview);
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
    }

    @RequestMapping(value = "/review/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestHeader("Authorization") final Long userId, @PathVariable final Long id) {
        reviewValidator.validateAuthor(userId, id);
        reviewService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}