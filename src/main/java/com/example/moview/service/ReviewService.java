package com.example.moview.service;

public interface ReviewService {

    void create(String jsonReview);

    void update(String jsonReview);

    void delete(Long id);
}
