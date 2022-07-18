package com.example.moview.moview.controller;

import com.example.moview.moview.dto.review.ReviewCreateDto;
import com.example.moview.moview.dto.review.ReviewDto;
import com.example.moview.moview.dto.review.ReviewUpdateDto;
import com.example.moview.moview.model.Review;
import com.example.moview.moview.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ReviewController {

    private final ReviewService reviewService;
    private final ModelMapper modelMapper;

    @Autowired
    public ReviewController(final ReviewService reviewService, final ModelMapper modelMapper) {
        this.reviewService = reviewService;
        this.modelMapper = modelMapper;
    }

    @RequestMapping(value = "/review", method = RequestMethod.POST)
    public ResponseEntity<ReviewDto> create(@RequestBody @Valid final ReviewCreateDto dto) {
        final Review review = modelMapper.map(dto, Review.class);
        final Review createdReview = reviewService.create(review);
        final ReviewDto dtoCreated = modelMapper.map(createdReview, ReviewDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(dtoCreated);
    }

    @RequestMapping(value = "/review", method = RequestMethod.PUT)
    public ResponseEntity<ReviewDto> update(@RequestBody @Valid final ReviewUpdateDto dto) {
        final Review review = modelMapper.map(dto, Review.class);
        final Review updatedReview = reviewService.update(review);
        final ReviewDto updatedDto = modelMapper.map(updatedReview, ReviewDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
    }

    @RequestMapping(value = "/review/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
