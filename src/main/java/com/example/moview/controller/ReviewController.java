package com.example.moview.controller;

import com.example.moview.dto.pagination.ReviewReadPageDto;
import com.example.moview.dto.review.ReviewCreateDto;
import com.example.moview.dto.review.ReviewDto;
import com.example.moview.dto.review.ReviewPageResponse;
import com.example.moview.dto.review.ReviewUpdateDto;
import com.example.moview.mapper.PaginationMapper;
import com.example.moview.mapper.ReviewMapper;
import com.example.moview.model.Review;
import com.example.moview.security.CurrentUser;
import com.example.moview.security.UserPrincipal;
import com.example.moview.service.ReviewService;
import com.example.moview.validator.ReviewValidator;
import com.example.moview.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    private final ReviewMapper reviewMapper;
    private final UserValidator userValidator;
    private final ReviewValidator reviewValidator;
    private final PaginationMapper paginationMapper;


    @PostMapping
    public ResponseEntity<ReviewDto> create(@CurrentUser final UserPrincipal userPrincipal,
                                            @RequestBody @Valid final ReviewCreateDto dto) {

        userValidator.validateUserExisting(userPrincipal);
        final Review review = reviewMapper.reviewCreateDtoToEntity(dto, userPrincipal.getId());
        final ReviewDto reviewDto = reviewMapper.entityToReviewDto(reviewService.create(review));
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewDto);
    }

    @PutMapping
    public ResponseEntity<ReviewDto> update(@CurrentUser final UserPrincipal userPrincipal,
                                            @RequestBody @Valid final ReviewUpdateDto dto) {

        reviewValidator.validateAuthor(userPrincipal, dto.getId());
        final Review review = reviewMapper.reviewUpdateDtoToEntity(dto, userPrincipal.getId());
        final ReviewDto reviewDto = reviewMapper.entityToReviewDto(reviewService.update(review));
        return ResponseEntity.ok(reviewDto);
    }


    @DeleteMapping("/{id}")
    public void delete(@CurrentUser final UserPrincipal userPrincipal, @PathVariable final Long id) {

        reviewValidator.validateAuthor(userPrincipal, id);
        reviewService.delete(id);
    }

    @GetMapping("/all")
    public ResponseEntity<ReviewPageResponse> getForMovie(ReviewReadPageDto dto) {

        final PageRequest pageRequest = paginationMapper.dtoToRequest(dto, dto.getProperty().getName());
        final Page<Review> reviews = reviewService.readReviewsForMovie(dto.getMovieId(), pageRequest);
        final List<ReviewDto> reviewDtos = reviewMapper.entityListToDtoList(reviews.getContent());
        final ReviewPageResponse response = ReviewPageResponse.of(reviews.getTotalElements(), reviewDtos);

        return ResponseEntity.ok(response);
    }
}