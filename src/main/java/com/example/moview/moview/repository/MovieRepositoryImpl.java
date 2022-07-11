package com.example.moview.moview.repository;

import com.example.moview.moview.config.db.DataSourceJdbc;
import com.example.moview.moview.exception.NotFoundException;
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

            ps.setString(1, movie.title());
            ps.setString(2, movie.description());
            ps.setDate(3, Date.valueOf(movie.releaseDate()));
            ps.setLong(4, movie.duration().getSeconds());
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

            ps.setString(1, newMovie.title());
            ps.setString(2, newMovie.description());
            ps.setDate(3, Date.valueOf(newMovie.releaseDate()));
            ps.setLong(4, newMovie.duration().getSeconds());
            ps.setLong(5, newMovie.id());
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
        try (Connection conn = DataSourceJdbc.getConnection()) {
            final String query = "SELECT m.id as m_id,m.title as m_title, description, release_date, duration, " +
                    "rating, r.id as r_id, movie_id, score, r.title as r_title, content, publication_date " +
                    "FROM movie m LEFT JOIN review r on m.id = r.movie_id WHERE m.id=?";

            log.debug(String.format("Reading movie and its reviews with id = %d from the database.", id));
            final PreparedStatement movieStatement = conn.prepareStatement(query);
            movieStatement.setLong(1, id);
            final ResultSet resultSet = movieStatement.executeQuery();

            if (!resultSet.next()) {
                throw new NotFoundException(String.format("Movie with id = %s not found in the data base.", id));
            }

            final Movie movie = buildReadMovie(resultSet);
            fillMovieWithReadReviews(movie, resultSet);
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
            final ResultSet resultSet = ps.executeQuery();

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

    private Movie buildReadMovie(final ResultSet resultSet) throws SQLException {
        return Movie.builder()
                .id(resultSet.getLong("m_id"))
                .title(resultSet.getString("m_title"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(Duration.ofSeconds(resultSet.getLong("duration")))
                .rating(resultSet.getInt("rating"))
                .reviews(new ArrayList<>())
                .build();
    }

    private void fillMovieWithReadReviews(final Movie movie, final ResultSet resultSet) throws SQLException {
        final Long zero = 0L;
        do {
            final Long reviewId = resultSet.getLong("r_id");
            if (reviewId.equals(zero)) continue;

            movie.reviews().add(
                    Review.builder()
                            .id(reviewId)
                            .movie(Movie.builder().id(movie.id()).build())
                            .score(resultSet.getInt("score"))
                            .title(resultSet.getString("r_title"))
                            .content(resultSet.getString("content"))
                            .publicationDate(resultSet.getDate("publication_date").toLocalDate())
                            .build());
        } while (resultSet.next());
    }
}