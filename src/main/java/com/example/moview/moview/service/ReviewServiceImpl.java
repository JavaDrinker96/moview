package com.example.moview.moview.service;

import com.example.moview.moview.config.mode_mapper.ModelMapperConfigurer;
import com.example.moview.moview.model.Review;
import com.example.moview.moview.repository.ReviewRepository;
import com.example.moview.moview.repository.ReviewRepositoryImpl;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;

public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repository;

    public ReviewServiceImpl() {
        this.repository = new ReviewRepositoryImpl();
    }

    @Override
    public void create(final Review review) throws SQLException, ClassNotFoundException {
        repository.create(review);
    }

    @Override
    public void update(final Review review) throws SQLException, ClassNotFoundException {
        repository.update(review);
    }

    @Override
    public void delete(final Long id) throws SQLException, ClassNotFoundException {
        repository.delete(id);
    }
}
