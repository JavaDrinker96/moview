package com.example.moview.moview.repository;

import com.example.moview.moview.model.Movie;

import java.sql.SQLException;
import java.util.List;

public interface MovieRepository {

    void create(Movie movie) throws ClassNotFoundException, SQLException;

    void update(Movie newMovie) throws SQLException, ClassNotFoundException;

    void delete(Long id) throws SQLException, ClassNotFoundException;

    Movie read(Long id) throws ClassNotFoundException, SQLException;

    List<Movie> readAll() throws SQLException, ClassNotFoundException;
}
