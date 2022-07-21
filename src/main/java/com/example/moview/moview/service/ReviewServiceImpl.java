package com.example.moview.moview.service;

import com.example.moview.moview.model.Movie;
import com.example.moview.moview.model.Review;
import com.example.moview.moview.repository.MovieRepository;
import com.example.moview.moview.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.OptionalDouble;

@Service
public class ReviewServiceImpl extends AbstractService<Review, ReviewRepository> implements ReviewService {

    private final MovieRepository movieRepository;

    public ReviewServiceImpl(final ReviewRepository repository, final MovieRepository movieRepository) {
        super(repository, Review.class);
        this.movieRepository = movieRepository;
    }

    @Override
    @Transactional
    public Review create(final Review entity) {
        final Review createdReview = super.create(entity);
        final Long movieId = createdReview.getMovie().getId();
        actualizeMovieRating(movieId);
        return createdReview;
    }

    @Override
    @Transactional
    public Review update(final Review newEntity) {
        final Review updatedReview = super.update(newEntity);
        final Long movieId = updatedReview.getMovie().getId();
        actualizeMovieRating(movieId);
        return updatedReview;
    }

    @Override
    @Transactional
    public void delete(final Long id) {
        final Long movieId = super.read(id).getMovie().getId();
        super.delete(id);
        actualizeMovieRating(movieId);
    }

    private void actualizeMovieRating(final Long movieId) {
        final Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(" Unable to find %s with id %d",
                        Movie.class.getName(), movieId)));

        final List<Review> reviewList = repository.findAllByMovieId(movieId);
        final OptionalDouble optionalAvgMovieScore = reviewList.stream().mapToLong(Review::getScore).average();
        final Integer avgMovieScore = optionalAvgMovieScore.isEmpty()
                ? null
                : Double.valueOf(Math.ceil(optionalAvgMovieScore.getAsDouble())).intValue();

        movie.setRating(avgMovieScore);
        movieRepository.save(movie);
    }
}