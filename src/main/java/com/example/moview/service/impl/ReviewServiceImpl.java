package com.example.moview.service.impl;

import com.example.moview.model.Review;
import com.example.moview.repository.ReviewRepository;
import com.example.moview.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Override
    public Page<Review> readReviewsForMovie(final Long movieId, final PageRequest pageRequest) {

        return repository.findAllByMovieId(movieId, pageRequest);
    }
}