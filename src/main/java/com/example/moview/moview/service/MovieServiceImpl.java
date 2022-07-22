package com.example.moview.moview.service;

import com.example.moview.moview.model.Genre;
import com.example.moview.moview.model.Movie;
import com.example.moview.moview.model.Review;
import com.example.moview.moview.repository.MovieRepository;
import com.example.moview.moview.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Set;

@Validated
@Service
public class MovieServiceImpl extends AbstractService<Movie, MovieRepository> implements MovieService {
    private static final int SAMPLE_USERS_TOP_SCORE = 80;
    private static final int USERS_TOP_SIZE = 5;

    private final ReviewRepository reviewRepository;

    public MovieServiceImpl(final MovieRepository repository, final ReviewRepository reviewRepository) {
        super(repository, Movie.class);
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

        final List<Movie> bestUsersMovies = repository.getUserMoviesByHisScoreDESC(userId, sampleScore);
        final List<Movie> topList = pickUpMoviesByScoreAndGenres(bestUsersMovies, sampleScore, genresIds, topSize);

        if (topList.size() < topSize) {
            final int remainToAdd = topSize - topList.size();
            final List<Movie> pickedUpMovies = pickUpMoviesByScore(bestUsersMovies, sampleScore, remainToAdd);
            topList.addAll(pickedUpMovies);
        }

        if (topList.size() < topSize) {
            final int remainToAdd = topSize - topList.size();
            final List<Movie> pickedUpMovies = pickUpFirstMovies(bestUsersMovies, remainToAdd);
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

    private List<Movie> pickUpMoviesByScoreAndGenres(final List<Movie> sourceList,
                                                     final int score,
                                                     final List<Long> genresIds,
                                                     final int pickUpQuantity) {

        final List<Movie> result = new ArrayList<>(sourceList.stream()
                .filter(m -> m.getRating() >= score && anyGenresMatch(m.getGenres(), genresIds))
                .limit(pickUpQuantity)
                .toList());
        sourceList.removeAll(result);

        return result;
    }

    private boolean anyGenresMatch(Set<Genre> genres, List<Long> genreIds) {
        return genres.stream().anyMatch(genre -> genreIds.contains(genre.getId()));
    }

    private List<Movie> pickUpMoviesByScore(final List<Movie> sourceList, final int score, final int pickUpQuantity) {
        final List<Movie> result = sourceList.stream()
                .filter(m -> m.getRating() >= score)
                .limit(pickUpQuantity)
                .toList();
        sourceList.removeAll(result);

        return result;
    }

    private List<Movie> pickUpFirstMovies(final List<Movie> sourceList, final int pickUpQuantity) {
        final List<Movie> result = sourceList.isEmpty()
                ? Collections.emptyList()
                : sourceList.subList(0, pickUpQuantity);
        sourceList.removeAll(result);

        return result;
    }
}