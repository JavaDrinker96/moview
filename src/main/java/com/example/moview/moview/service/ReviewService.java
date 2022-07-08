package com.example.moview.moview.service;

import com.example.moview.moview.model.Review;

import java.sql.SQLException;

public interface ReviewService {

    void create(Review review) throws SQLException, ClassNotFoundException;

    void update(Review review) throws SQLException, ClassNotFoundException;

    void delete(Long id) throws SQLException, ClassNotFoundException;
}
