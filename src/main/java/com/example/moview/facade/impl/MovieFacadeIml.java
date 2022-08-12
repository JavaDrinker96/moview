package com.example.moview.facade.impl;

import com.example.moview.facade.MovieFacade;
import com.example.moview.mapper.MovieMapper;
import com.example.moview.model.Movie;
import com.example.moview.dto.movie.MovieDto;
import com.example.moview.service.MovieService;
import com.example.moview.service.OmdbService;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class MovieFacadeIml implements MovieFacade {

    private final MovieService movieService;
    private final MovieMapper movieMapper;
    private final OmdbService omdbService;

    public MovieFacadeIml(final MovieService movieService,
                          final MovieMapper movieMapper,
                          final OmdbService omdbService) {

        this.movieService = movieService;
        this.movieMapper = movieMapper;
        this.omdbService = omdbService;
    }

    @Override
    public Movie create(final Movie movie) {
        if (hasBadDescription(movie)) {
            movie.setDescription(omdbService.getDescriptionByMovieName(movie.getTitle()));
        }
        return movieService.create(movie);
    }

    @Override
    public MovieDto read(final Long id) {
        final Movie movie = movieService.read(id);
        final Integer imdbRating = omdbService.getRatingByMovieName(movie.getTitle());
        return movieMapper.entityToMovieDto(movie, imdbRating);
    }

    @Override
    public Movie update(final Movie movie) {
        if (hasBadDescription(movie)) {
            movie.setDescription(omdbService.getDescriptionByMovieName(movie.getTitle()));
        }
        return movieService.update(movie);
    }

    private boolean hasBadDescription(final Movie movie) {
        return Objects.isNull(movie.getDescription()) || movie.getDescription().isBlank();
    }
}
