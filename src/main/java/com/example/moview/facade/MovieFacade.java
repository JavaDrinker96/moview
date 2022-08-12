package com.example.moview.facade;

import com.example.moview.model.Movie;
import com.example.moview.dto.movie.MovieDto;

public interface MovieFacade {

    Movie create(Movie movie);

    MovieDto read(Long id);

    Movie update(Movie movie);
}
