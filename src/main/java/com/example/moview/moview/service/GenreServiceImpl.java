package com.example.moview.moview.service;

import com.example.moview.moview.model.Genre;
import com.example.moview.moview.repository.GenreRepository;
import org.springframework.stereotype.Service;

@Service
public class GenreServiceImpl extends AbstractService<Genre, GenreRepository> implements GenreService {

    public GenreServiceImpl(final GenreRepository repository) {
        super(repository, Genre.class);
    }
}
