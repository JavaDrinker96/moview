package com.example.moview.repository;

import com.example.moview.config.db.DataSource;
import com.example.moview.exception.DataBaseException;
import com.example.moview.exception.NotFoundException;
import com.example.moview.model.Movie;
import com.example.moview.model.Review;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class MovieRepositoryImpl implements MovieRepository {

    static final Logger log = (Logger) LogManager.getRootLogger();

    @Override
    public void create(final Movie movie) {
        log.info("Saving movie data to the database.");
        try (Connection conn = DataSource.getConnection()) {
            final PreparedStatement ps = conn.prepareStatement("INSERT INTO movie (title, description, " +
                    "release_date, duration) VALUES (?,?,?,?)");

            ps.setString(1, movie.getTitle());
            ps.setString(2, movie.getDescription());
            ps.setDate(3, Date.valueOf(movie.getReleaseDate()));
            ps.setLong(4, movie.getDuration().getSeconds());
            ps.executeUpdate();

            log.info("Saving the movie data in the database was successful.");
        } catch (SQLException e) {
            log.error("The movie data could not be written to the database.");
            throw new DataBaseException("The movie data could not be written to the database.");
        }
    }

    @Override
    public void update(final Movie newMovie) {
        log.info("Updating movie data to the database.");
        try (Connection conn = DataSource.getConnection()) {
            final PreparedStatement ps =
                    conn.prepareStatement("UPDATE movie SET title = ?, description = ?, release_date = ?, " +
                            "duration = ? WHERE id = ?");

            ps.setString(1, newMovie.getTitle());
            ps.setString(2, newMovie.getDescription());
            ps.setDate(3, Date.valueOf(newMovie.getReleaseDate()));
            ps.setLong(4, newMovie.getDuration().getSeconds());
            ps.setLong(5, newMovie.getId());
            ps.executeUpdate();

            log.info("Updating the movie data in the database was successful.");
        } catch (SQLException e) {
            log.error("The movie data was not updated in the database.");
            throw new DataBaseException("The movie data was not updated in the database.");
        }
    }

    @Override
    public void delete(final Long id) {
        log.info("Deleting movie data in the database.");
        try (Connection conn = DataSource.getConnection()) {
            final PreparedStatement ps = conn.prepareStatement("DELETE FROM movie WHERE id = ?");
            ps.setLong(1, id);
            ps.executeUpdate();

            log.info("Deleting the movie data in the database was successful.");
        } catch (SQLException e) {
            log.error("The movie data was not deleted in the database.");
            throw new DataBaseException("The movie data was not deleted in the database.");
        }
    }

    @Override
    public Movie read(final Long id) {
        log.info("Reading movie data in the database.");
        try (Connection conn = DataSource.getConnection()) {
            final String movieQuery = "SELECT * FROM movie WHERE id = ?";
            final PreparedStatement movieStatement = conn.prepareStatement(movieQuery);
            movieStatement.setLong(1, id);
            ResultSet movieSet = movieStatement.executeQuery();

            if (!movieSet.next()) {
                log.error(String.format("Movie with id=%s not found in the data base.", id));
                throw new NotFoundException(String.format("Movie with id=%s not found in the data base.", id));
            }

            final Movie movie = Movie.builder()
                    .id(movieSet.getLong("id"))
                    .title(movieSet.getString("title"))
                    .description(movieSet.getString("description"))
                    .releaseDate(movieSet.getDate("release_date").toLocalDate())
                    .duration(Duration.ofSeconds(movieSet.getLong("duration")))
                    .rating(movieSet.getInt("rating"))
                    .reviews(new ArrayList<>())
                    .build();

            log.info(String.format("Reading review data in the database for movie with id=%s.", id));
            final String reviewsQuery = "SELECT * FROM review WHERE movie_id = ?";
            final PreparedStatement reviewsStatement = conn.prepareStatement(reviewsQuery);
            reviewsStatement.setLong(1, id);
            ResultSet reviewsSet = reviewsStatement.executeQuery();

            while (reviewsSet.next()) {
                movie.getReviews().add(
                        Review.builder()
                                .id(reviewsSet.getLong("id"))
                                .score(reviewsSet.getInt("score"))
                                .title(reviewsSet.getString("title"))
                                .content(reviewsSet.getString("content"))
                                .publicationDate(reviewsSet.getDate("publication_date").toLocalDate())
                                .build());
            }

            log.info("Reading the movie data in the database was successful.");
            return movie;
        } catch (SQLException e) {
            log.error("The movie data with review information could not be read from the database.");
            throw new DataBaseException("The movie data with review information could not be read from the database.");
        }
    }

    @Override
    public List<Movie> readAll() {
        final List<Movie> movieList = new ArrayList<>();
        log.info("Reading movie data list from the database.");
        try (Connection conn = DataSource.getConnection()) {
            final String query = "SELECT * FROM movie";
            final PreparedStatement ps = conn.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                movieList.add(Movie.builder()
                        .id(resultSet.getLong("id"))
                        .title(resultSet.getString("title"))
                        .description(resultSet.getString("description"))
                        .releaseDate(resultSet.getDate("release_date").toLocalDate())
                        .duration(Duration.ofSeconds(resultSet.getLong("duration")))
                        .rating(resultSet.getInt("rating"))
                        .build());
            }
        } catch (SQLException e) {
            log.error("The movie data list could not be read from the database.");
            throw new DataBaseException("The movie data list could not be read from the database.");
        }

        if (movieList.isEmpty()) {
            log.error("Movie data list in the data base is empty.");
            throw new NotFoundException("List of Movies not found in the data base.");
        }

        log.info("Reading the movie data list in the database was successful.");
        return movieList;
    }
}