package com.example.moview.moview.facade;

import com.example.moview.moview.dto.movie.MovieDto;
import com.example.moview.moview.model.Movie;

public interface MovieFacade {

    Movie create(Movie movie);

    MovieDto read(Long id);

    Movie update(Movie movie);
}
