package com.example.moview.moview.service;

import com.example.moview.moview.model.BaseEntity;
import com.example.moview.moview.model.Genre;
import com.example.moview.moview.model.Movie;
import com.example.moview.moview.model.Review;
import com.example.moview.moview.repository.MovieRepository;
import com.example.moview.moview.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;

@Validated
@Service
public class MovieServiceImpl extends AbstractService<Movie, MovieRepository> implements MovieService {
    private static final int SAMPLE_USERS_TOP_SCORE = 80;
    private static final int USERS_TOP_SIZE = 5;

    private final ReviewRepository reviewRepository;

    public MovieServiceImpl(final MovieRepository repository, final ReviewRepository reviewRepository) {
        super(repository);
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Movie update(@NotNull final Movie movie) {
        setActualRating(movie);
        return repository.save(movie);
    }

    @Override
    public void actualizeRating(@NotNull final Long id) {
        final Movie movie = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(" Unable to find %s with id %d",
                        Movie.class.getName(), id)));

        setActualRating(movie);
        repository.save(movie);
    }

    @Override
    public List<Movie> getUsersTop(@NotNull final Long userId, @NotNull final List<Long> genresIds) {
        final int sampleScore = SAMPLE_USERS_TOP_SCORE;
        final int topSize = USERS_TOP_SIZE;

        final List<Movie> userMovies = repository.getMoviesByUserId(userId);
        final List<Movie> bestUserMovies = filterByCurrentReviewsAndScore(userMovies, sampleScore);
        final List<Movie> topList = pickUpMoviesByRatingAndGenres(bestUserMovies, sampleScore, genresIds, topSize);

        if (topList.size() < topSize) {
            final int remainToAdd = topSize - topList.size();
            final List<Movie> pickedUpMovies = pickUpMoviesByScore(bestUserMovies, sampleScore, remainToAdd);
            topList.addAll(pickedUpMovies);
        }

        if (topList.size() < topSize) {
            final int remainToAdd = topSize - topList.size();
            final List<Movie> pickedUpMovies = pickUpFirstMovies(bestUserMovies, remainToAdd);
            topList.addAll(pickedUpMovies);
        }

        return topList;
    }

    private void setActualRating(final Movie movie) {
        final Integer movieRating = calculateRating(movie.getId());
        movie.setRating(movieRating);
    }

    private Integer calculateRating(final Long id) {
        final List<Review> reviewList = reviewRepository.findAllByMovieId(id);
        final OptionalDouble optionalAvgMovieRating = reviewList.stream().mapToLong(Review::getScore).average();
        return optionalAvgMovieRating.isEmpty()
                ? null
                : Double.valueOf(Math.ceil(optionalAvgMovieRating.getAsDouble())).intValue();
    }

    private List<Movie> filterByCurrentReviewsAndScore(final List<Movie> movies, final int score) {
        return movies.stream()
                .filter(movie -> getLastReview(movie.getReviews()).getScore() > score)
                .sorted(Comparator.comparing(Movie::getRating).reversed())
                .collect(Collectors.toList());
    }

    private Review getLastReview(final Set<Review> reviews) {
        final int zeroScore = 0;
        return reviews.stream().max(Comparator.comparing(BaseEntity::getCreated))
                .orElse(Review.builder().score(zeroScore).build());
    }

    private List<Movie> pickUpMoviesByRatingAndGenres(final List<Movie> sourceList,
                                                      final int score,
                                                      final List<Long> genresIds,
                                                      final int pickUpQuantity) {

        final List<Movie> result = sourceList.stream()
                .filter(m -> m.getRating() >= score && anyGenresMatch(m.getGenres(), genresIds))
                .limit(pickUpQuantity).collect(Collectors.toList());

        if(!result.isEmpty()){
            sourceList.removeAll(result);
        }

        return result;
    }

    private boolean anyGenresMatch(Set<Genre> genres, List<Long> genreIds) {
        return genres.stream().anyMatch(genre -> genreIds.contains(genre.getId()));
    }

    private List<Movie> pickUpMoviesByScore(final List<Movie> sourceList, final int score, final int pickUpQuantity) {
        final List<Movie> result = sourceList.stream()
                .filter(m -> m.getRating() >= score)
                .limit(pickUpQuantity)
                .collect(Collectors.toList());

        if(!result.isEmpty()){
            sourceList.removeAll(result);
        }

        return result;
    }

    private List<Movie> pickUpFirstMovies(final List<Movie> sourceList, final int pickUpQuantity) {
        final List<Movie> result = sourceList.stream().limit(pickUpQuantity).collect(Collectors.toList());

        if(!result.isEmpty()){
            sourceList.removeAll(result);
        }

        return result;
    }
}