package com.example.moview.service;

import com.example.moview.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface MovieService extends BaseService<Movie, Long> {

    Page<Movie> findByTitle(String title, PageRequest pageRequest);

    Page<Movie> findByTitleAndUserId(String title, Long userId, PageRequest pageRequest);

    List<Movie> getUsersTop(Long userId, List<Long> genreIds);
}