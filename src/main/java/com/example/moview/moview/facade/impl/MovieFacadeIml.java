package com.example.moview.moview.facade.impl;

import com.example.moview.moview.dto.movie.MovieDto;
import com.example.moview.moview.facade.MovieFacade;
import org.springframework.stereotype.Component;

@Component
public class MovieFacadeIml implements MovieFacade {

    public MovieDto read(final Long id) {

//        return repository.findById(id).orElseThrow();
        return null;
    }
}
