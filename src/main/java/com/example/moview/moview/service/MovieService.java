package com.example.moview.moview.service;

import com.example.moview.moview.model.Movie;

import java.sql.SQLException;
import java.util.List;

public interface MovieService {

    void create(Movie movie) throws SQLException, ClassNotFoundException;

    Movie read(Long id) throws SQLException, ClassNotFoundException;

    void update(Movie movie) throws SQLException, ClassNotFoundException;

    void delete(Long id) throws SQLException, ClassNotFoundException;

    List<Movie> readAll() throws SQLException, ClassNotFoundException;
}
