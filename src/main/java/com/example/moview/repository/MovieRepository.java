package com.example.moview.repository;

import com.example.moview.model.Movie;

import java.util.List;

public interface MovieRepository {

    void create(Movie movie);

    void update(Movie newMovie);

    void delete(Long id);

    Movie read(Long id);

    List<Movie> readAll();
}
