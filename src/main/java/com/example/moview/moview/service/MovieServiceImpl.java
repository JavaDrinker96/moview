package com.example.moview.moview.service;

import com.example.moview.moview.model.Movie;
import com.example.moview.moview.repository.MovieRepository;
import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl extends AbstractService<Movie, MovieRepository, Long> implements MovieService {

    public MovieServiceImpl(final MovieRepository repository) {
        super(repository, Movie.class);
    }
}