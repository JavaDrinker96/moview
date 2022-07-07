package com.example.moview.repository;

import com.example.moview.model.Review;

public interface ReviewRepository {

    void create(Review review);

    void update(Review newReview);

    void delete(Long id);
}
