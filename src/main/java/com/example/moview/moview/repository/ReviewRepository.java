package com.example.moview.moview.repository;


import com.example.moview.moview.model.Review;

import java.sql.SQLException;

public interface ReviewRepository {

    void create(Review review) throws ClassNotFoundException, SQLException;

    void update(Review newReview) throws SQLException, ClassNotFoundException;

    void delete(Long id) throws SQLException, ClassNotFoundException;
}
