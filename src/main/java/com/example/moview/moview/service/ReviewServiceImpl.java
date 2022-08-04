package com.example.moview.moview.service;

import com.example.moview.moview.model.Movie;
import com.example.moview.moview.model.Review;
import com.example.moview.moview.repository.MovieRepository;
import com.example.moview.moview.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.OptionalDouble;

@Service
public class ReviewServiceImpl extends AbstractService<Review, ReviewRepository, Long> implements ReviewService {

    private final MovieRepository movieRepository;

    public ReviewServiceImpl(final ReviewRepository repository, final MovieRepository movieRepository) {
        super(repository);
        this.movieRepository = movieRepository;
    }

    @Override
    public Review create(final Review entity) {
        final Review review = super.create(entity);
        actualizeMovieRating(review.getMovie().getId());
        return review;
    }

    @Override
    public Review update(final Review newEntity) {
        final Review review = super.update(newEntity);
        actualizeMovieRating(review.getMovie().getId());
        return review;
    }

    @Override
    public void delete(final Long id) {
        final Long movieId = super.read(id).getMovie().getId();
        super.delete(id);
        actualizeMovieRating(movieId);
    }

    private void actualizeMovieRating(final Long movieId) {
        final Movie movie = movieRepository.findById(movieId)
                .orElseThrow();

        final List<Review> reviewList = repository.findAllByMovieId(movieId);
        final OptionalDouble optionalAvgMovieScore = reviewList.stream().mapToLong(Review::getScore).average();
        final Integer avgMovieScore = optionalAvgMovieScore.isEmpty()
                ? null
                : Double.valueOf(Math.ceil(optionalAvgMovieScore.getAsDouble())).intValue();

        movie.setRating(avgMovieScore);
        movieRepository.save(movie);
    }
}