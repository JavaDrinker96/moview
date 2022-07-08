package com.example.moview.moview.repository;

import com.example.moview.moview.config.db.DataSourceJdbc;
import com.example.moview.moview.model.Movie;
import com.example.moview.moview.model.Review;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class MovieRepositoryImpl implements MovieRepository {

    private static final Logger log = (Logger) LogManager.getRootLogger();

    @Override
    public void create(final Movie movie) throws ClassNotFoundException, SQLException {
        log.debug(String.format("Saving %s to the database.", movie.toString()));
        try (Connection conn = DataSourceJdbc.getConnection()) {
            final PreparedStatement ps = conn.prepareStatement("INSERT INTO movie (title, description, " +
                    "release_date, duration) VALUES (?,?,?,?)");

            ps.setString(1, movie.getTitle());
            ps.setString(2, movie.getDescription());
            ps.setDate(3, Date.valueOf(movie.getReleaseDate()));
            ps.setLong(4, movie.getDuration().getSeconds());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(final Movie newMovie) throws SQLException, ClassNotFoundException {
        log.debug(String.format("Updating %s in the database.", newMovie.toString()));
        try (Connection conn = DataSourceJdbc.getConnection()) {
            final PreparedStatement ps =
                    conn.prepareStatement("UPDATE movie SET title = ?, description = ?, release_date = ?, " +
                            "duration = ? WHERE id = ?");

            ps.setString(1, newMovie.getTitle());
            ps.setString(2, newMovie.getDescription());
            ps.setDate(3, Date.valueOf(newMovie.getReleaseDate()));
            ps.setLong(4, newMovie.getDuration().getSeconds());
            ps.setLong(5, newMovie.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(final Long id) throws SQLException, ClassNotFoundException {
        log.debug(String.format("Deleting movie with id = %d in the database.", id));
        try (Connection conn = DataSourceJdbc.getConnection()) {
            final PreparedStatement ps = conn.prepareStatement("DELETE FROM movie WHERE id = ?");
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public Movie read(final Long id) throws ClassNotFoundException, SQLException {
        log.debug(String.format("Reading movie  with id = %d from the database.", id));
        try (Connection conn = DataSourceJdbc.getConnection()) {
            final String movieQuery = "SELECT * FROM movie WHERE id = ?";
            final PreparedStatement movieStatement = conn.prepareStatement(movieQuery);
            movieStatement.setLong(1, id);
            ResultSet movieSet = movieStatement.executeQuery();

            if (!movieSet.next()) {
                throw new ClassNotFoundException(String.format("Movie with id = %s not found in the data base.", id));
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

            return movie;
        }
    }

    @Override
    public List<Movie> readAll() throws SQLException, ClassNotFoundException {
        final List<Movie> movieList = new ArrayList<>();
        log.info("Reading movie data list from the database.");
        try (Connection conn = DataSourceJdbc.getConnection()) {
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
        }

        return movieList;
    }
}