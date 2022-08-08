package com.example.moview.moview.facade;

import com.example.moview.moview.dto.movie.MovieDto;

public interface MovieFacade {

    MovieDto read(Long id);
}
