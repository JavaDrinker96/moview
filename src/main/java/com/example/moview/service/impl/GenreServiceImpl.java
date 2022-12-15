package com.example.moview.service.impl;

import com.example.moview.model.Genre;
import com.example.moview.repository.GenreRepository;
import com.example.moview.service.GenreService;
import org.springframework.stereotype.Service;

@Service
public class GenreServiceImpl extends AbstractService<Genre, GenreRepository, Long> implements GenreService {

    public GenreServiceImpl(final GenreRepository repository) {
        super(repository);
    }

}