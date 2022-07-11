package com.example.moview.moview.service;

import com.example.moview.moview.model.Movie;
import com.example.moview.moview.repository.MovieRepository;
import com.example.moview.moview.repository.MovieRepositoryImpl;

import java.sql.SQLException;
import java.util.List;

public class MovieServiceImpl implements MovieService {

    private final MovieRepository repository;

    public MovieServiceImpl() {
        this.repository = new MovieRepositoryImpl();
    }

    @Override
    public void create(final Movie movie) throws SQLException, ClassNotFoundException {
        repository.create(movie);
    }

    @Override
    public Movie read(final Long id) throws SQLException, ClassNotFoundException {
        return repository.read(id);
    }

    @Override
    public void update(final Movie movie) throws SQLException, ClassNotFoundException {
        repository.update(movie);
    }

    @Override
    public void delete(final Long id) throws SQLException, ClassNotFoundException {
        repository.delete(id);
    }

    @Override
    public List<Movie> readAll() throws SQLException, ClassNotFoundException {
        return repository.readAll();
    }
}