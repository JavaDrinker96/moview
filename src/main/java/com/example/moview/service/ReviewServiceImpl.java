package com.example.moview.service;

import com.example.moview.dto.review.ReviewCreateDto;
import com.example.moview.dto.review.ReviewUpdateDto;
import com.example.moview.model.Review;
import com.example.moview.repository.ReviewRepository;
import com.example.moview.repository.ReviewRepositoryImpl;
import com.example.moview.util.mapper.JsonMapper;
import com.example.moview.util.mapper.ModelMapperConfigurer;
import org.modelmapper.ModelMapper;

public class ReviewServiceImpl implements ReviewService {

    private final ModelMapper modelMapper;
    private final ReviewRepository repository;

    public ReviewServiceImpl() {
        this.modelMapper = new ModelMapper();
        ModelMapperConfigurer.reviewConfigure(modelMapper);
        this.repository = new ReviewRepositoryImpl();
    }

    @Override
    public void create(final String jsonReview) {
        final ReviewCreateDto createDto = JsonMapper.readValue(jsonReview, ReviewCreateDto.class);
        final Review review = modelMapper.map(createDto, Review.class);
        repository.create(review);
    }

    @Override
    public void update(final String jsonReview) {
        final ReviewUpdateDto updateDto = JsonMapper.readValue(jsonReview, ReviewUpdateDto.class);
        final Review review = modelMapper.map(updateDto, Review.class);
        repository.update(review);
    }

    @Override
    public void delete(final Long id) {
        repository.delete(id);
    }
}
