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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/review")
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

    @PostMapping
    public ResponseEntity<ReviewDto> create(@RequestHeader("Authorization") final Long userId,
                                            @RequestBody @Valid final ReviewCreateDto dto) {

        userValidator.validateUserExisting(userId);
        final Review review = reviewMapper.createDtoToModel(dto);
        review.setAuthor(User.builder().id(userId).build());
        final ReviewDto reviewDto = reviewMapper.modelToDto(reviewService.create(review));
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewDto);
    }

    @PutMapping
    public ResponseEntity<ReviewDto> update(@RequestHeader("Authorization") final Long userId,
                                            @RequestBody @Valid final ReviewUpdateDto dto) {

        reviewValidator.validateAuthor(userId, dto.getId());
        final Review review = reviewMapper.updateDtoToModel(dto);
        review.setAuthor(User.builder().id(userId).build());
        final ReviewDto reviewDto = reviewMapper.modelToDto(reviewService.update(review));
        return ResponseEntity.status(HttpStatus.OK).body(reviewDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader("Authorization") final Long userId, @PathVariable final Long id) {
        reviewValidator.validateAuthor(userId, id);
        reviewService.delete(id);
    }
}