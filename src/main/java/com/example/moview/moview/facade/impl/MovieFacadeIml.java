package com.example.moview.moview.facade.impl;

import com.example.moview.moview.dto.movie.MovieDto;
import com.example.moview.moview.exception.OmdbDescriptionException;
import com.example.moview.moview.facade.MovieFacade;
import com.example.moview.moview.mapper.MovieMapper;
import com.example.moview.moview.model.Movie;
import com.example.moview.moview.service.MovieService;
import com.example.moview.moview.service.OmdbService;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

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
            trySetOmdbDescription(movie);
        }
        return movieService.create(movie);
    }

    @Override
    public MovieDto read(final Long id) {
        final Movie movie = movieService.read(id);
        final Optional<Integer> omdbRating = omdbService.getRatingByMovieName(movie.getTitle());
        return movieMapper.entityToMovieDto(movie, omdbRating);
    }

    @Override
    public Movie update(final Movie movie) {
        if (hasBadDescription(movie)) {
            trySetOmdbDescription(movie);
        }
        return movieService.update(movie);
    }

    private void trySetOmdbDescription(final Movie movie) {
        final String movieDescription = omdbService.getDescriptionByMovieName(movie.getTitle())
                .orElseThrow(() -> new OmdbDescriptionException(
                        String.format("Unable to get description for movie with title = %s  via Imdb API.",
                                movie.getTitle()))
                );
        movie.setDescription(movieDescription);
    }

    private boolean hasBadDescription(final Movie movie) {
        return Objects.isNull(movie.getDescription()) || movie.getDescription().isBlank();
    }
}
