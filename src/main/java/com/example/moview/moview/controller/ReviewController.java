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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final ModelMapper modelMapper;

    @Autowired
    public ReviewController(final ReviewService reviewService, final ModelMapper modelMapper) {
        this.reviewService = reviewService;
        this.modelMapper = modelMapper;
    }

   @PostMapping
    public ResponseEntity<ReviewDto> create(@RequestBody @Valid final ReviewCreateDto dto) {
        final Review review = modelMapper.map(dto, Review.class);
        final ReviewDto reviewDto = modelMapper.map(reviewService.create(review), ReviewDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(reviewDto);
    }

    @PutMapping
    public ResponseEntity<ReviewDto> update(@RequestBody @Valid final ReviewUpdateDto dto) {
        final Review review = modelMapper.map(dto, Review.class);
        final ReviewDto reviewDto = modelMapper.map(reviewService.update(review), ReviewDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(reviewDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        reviewService.delete(id);
    }
}