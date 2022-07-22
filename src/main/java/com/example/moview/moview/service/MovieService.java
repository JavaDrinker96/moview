package com.example.moview.moview.service;

import com.example.moview.moview.model.Movie;

import java.util.List;

public interface MovieService extends BaseService<Movie> {

    void actualizeRating(Long id);

    List<Movie> getUsersTop(Long userId, List<Long> genreIds);
}