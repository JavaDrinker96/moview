package com.example.moview.controller;

import com.example.moview.dto.review.ReviewCreateDto;
import com.example.moview.dto.review.ReviewDto;
import com.example.moview.dto.review.ReviewUpdateDto;
import com.example.moview.mapper.ReviewMapper;
import com.example.moview.model.Review;
import com.example.moview.service.ReviewService;
import com.example.moview.validator.ReviewValidator;
import com.example.moview.validator.UserValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
@Api(tags = "Endpoints for users reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;
    private final UserValidator userValidator;
    private final ReviewValidator reviewValidator;


    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiOperation("Add new review")
    public ReviewDto create(@RequestHeader("Authorization") final Long userId,
                            @RequestBody @Valid final ReviewCreateDto dto) {

        userValidator.validateUserExisting(userId);
        final Review review = reviewMapper.reviewCreateDtoToEntity(dto, userId);
        return reviewMapper.entityToReviewDto(reviewService.create(review));
    }

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation("Change review")
    public ReviewDto update(@RequestHeader("Authorization") final Long userId,
                            @RequestBody @Valid final ReviewUpdateDto dto) {

        reviewValidator.validateAuthor(userId, dto.getId());
        final Review review = reviewMapper.reviewUpdateDtoToEntity(dto, userId);
        return reviewMapper.entityToReviewDto(reviewService.update(review));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation("Get review by id")
    public void delete(@RequestHeader("Authorization") final Long userId, @PathVariable final Long id) {
        reviewValidator.validateAuthor(userId, id);
        reviewService.delete(id);
    }

}