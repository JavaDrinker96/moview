package com.example.moview.moview.service;

import com.example.moview.moview.model.Review;
import com.example.moview.moview.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import static java.time.LocalDate.now;

@Validated
@Service
public class ReviewServiceImpl extends AbstractService<Review, ReviewRepository, Long> implements ReviewService {

    private final MovieService movieService;

    public ReviewServiceImpl(final ReviewRepository repository, final MovieService movieService) {
        super(repository);
        this.movieService = movieService;
    }

    @Override
    public Review create(@NotNull final Review entity) {
        entity.setPublicationDate(now());
        final Review createdReview = super.create(entity);
        final Long movieId = createdReview.getMovie().getId();
        movieService.actualizeRating(movieId);
        return createdReview;
    }

    @Override
    public Review update(@NotNull final Review newEntity) {
        final Review updatedReview = super.update(newEntity);
        final Long movieId = updatedReview.getMovie().getId();
        movieService.actualizeRating(movieId);
        return updatedReview;
    }

    @Override
    public void delete(@NotNull final Long id) {
        final Long movieId = super.read(id).getMovie().getId();
        super.delete(id);
        movieService.actualizeRating(movieId);
    }
}