package com.example.moview.service;

import com.example.moview.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ReviewService extends BaseService<Review, Long> {

    Page<Review> readReviewsForMovie(Long movieId, PageRequest pageRequest);
}