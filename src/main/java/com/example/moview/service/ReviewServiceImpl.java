package com.example.moview.service;

import com.example.moview.model.Review;
import com.example.moview.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import static java.time.LocalDate.now;

@Service
public class ReviewServiceImpl extends AbstractService<Review, ReviewRepository, Long> implements ReviewService {

    public ReviewServiceImpl(final ReviewRepository repository) {
        super(repository);
    }

    @Override
    public Review create(final Review entity) {
        entity.setPublicationDate(now());
        return repository.save(entity);
    }
}