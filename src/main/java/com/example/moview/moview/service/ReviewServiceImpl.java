package com.example.moview.moview.service;

import com.example.moview.moview.model.Review;
import com.example.moview.moview.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static java.time.LocalDate.now;

@Service
public class ReviewServiceImpl extends AbstractService<Review, ReviewRepository> implements ReviewService {

    private final MovieService movieService;

    public ReviewServiceImpl(final ReviewRepository repository, final MovieService movieService) {

        super(repository, Review.class);
        this.movieService = movieService;
    }

    @Override
    @Transactional
    public Review create(final Review entity) {
        entity.setPublicationDate(now());
        final Review createdReview = super.create(entity);
        final Long movieId = createdReview.getMovie().getId();
        movieService.actualizeRating(movieId);
        return createdReview;
    }

    @Override
    @Transactional
    public Review update(final Review newEntity) {
        final Review updatedReview = super.update(newEntity);
        final Long movieId = updatedReview.getMovie().getId();
        movieService.actualizeRating(movieId);
        return updatedReview;
    }

    @Override
    @Transactional
    public void delete(final Long id) {
        final Long movieId = super.read(id).getMovie().getId();
        super.delete(id);
        movieService.actualizeRating(movieId);
    }
}