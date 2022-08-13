package com.example.moview.service;

import com.example.moview.model.Movie;

import java.util.List;

public interface MovieService extends BaseService<Movie, Long> {

    List<Movie> getUsersTop(Long userId, List<Long> genreIds);
}