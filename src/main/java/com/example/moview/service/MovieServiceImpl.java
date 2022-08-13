package com.example.moview.service;

import com.example.moview.model.BaseEntity;
import com.example.moview.model.Genre;
import com.example.moview.model.Movie;
import com.example.moview.model.Review;
import com.example.moview.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl extends AbstractService<Movie, MovieRepository, Long> implements MovieService {
    private static final int SAMPLE_SCORE = 80;
    private static final int TOP_SIZE = 5;

    public MovieServiceImpl(final MovieRepository repository) {
        super(repository);
    }

    @Override
    public List<Movie> getUsersTop(final Long userId, final List<Long> genresIds) {
        final List<Movie> userMovies = repository.getMoviesByUserId(userId);
        final List<Movie> bestUserMovies = filterByCurrentReviewsAndScore(userMovies, SAMPLE_SCORE);
        final List<Movie> topList = pickUpMoviesByRatingAndGenres(bestUserMovies, SAMPLE_SCORE, genresIds, TOP_SIZE);

        if (topList.size() < TOP_SIZE) {
            topList.addAll(pickUpMoviesByScore(bestUserMovies, SAMPLE_SCORE, TOP_SIZE - topList.size()));
        }

        if (topList.size() < TOP_SIZE) {
            topList.addAll(pickUpFirstMovies(bestUserMovies, TOP_SIZE - topList.size()));
        }

        return topList;
    }

    private List<Movie> filterByCurrentReviewsAndScore(final List<Movie> movies, final int score) {
        return movies
                .stream()
                .filter(movie -> getLastReview(movie.getReviews()).getScore() >= score)
                .sorted(Comparator.comparing(Movie::getRating).reversed())
                .collect(Collectors.toList());
    }

    private Review getLastReview(final Set<Review> reviews) {
        final int zeroScore = 0;
        return reviews
                .stream()
                .max(Comparator.comparing(BaseEntity::getCreated))
                .orElse(Review.builder()
                        .score(zeroScore)
                        .build());
    }

    private List<Movie> pickUpMoviesByRatingAndGenres(final List<Movie> sourceList,
                                                      final int score,
                                                      final List<Long> genresIds,
                                                      final int pickUpQuantity) {

        final List<Movie> result = sourceList
                .stream()
                .filter(m -> m.getRating() >= score && anyGenresMatch(m.getGenres(), genresIds))
                .limit(pickUpQuantity)
                .collect(Collectors.toList());
        sourceList.removeAll(result);

        return result;
    }

    private boolean anyGenresMatch(final Set<Genre> genres, final List<Long> genreIds) {
        return genres.stream().anyMatch(genre -> genreIds.contains(genre.getId()));
    }

    private List<Movie> pickUpMoviesByScore(final List<Movie> sourceList, final int score, final int pickUpQuantity) {
        final List<Movie> result = sourceList
                .stream()
                .filter(m -> m.getRating() >= score)
                .limit(pickUpQuantity)
                .collect(Collectors.toList());
        sourceList.removeAll(result);

        return result;
    }

    private List<Movie> pickUpFirstMovies(final List<Movie> sourceList, final int pickUpQuantity) {
        final List<Movie> result = sourceList
                .stream()
                .limit(pickUpQuantity)
                .collect(Collectors.toList());
        sourceList.removeAll(result);

        return result;
    }
}