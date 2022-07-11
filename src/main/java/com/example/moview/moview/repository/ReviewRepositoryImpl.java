package com.example.moview.moview.repository;

import com.example.moview.moview.config.db.DataSourceJdbc;
import com.example.moview.moview.model.Review;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReviewRepositoryImpl implements ReviewRepository {

    private static final Logger log = (Logger) LogManager.getRootLogger();

    @Override
    public void create(final Review review) throws ClassNotFoundException, SQLException {
        log.debug(String.format("Saving %s to the database.", review.toString()));
        try (final Connection conn = DataSourceJdbc.getConnection()) {
            final PreparedStatement ps = conn.prepareStatement("INSERT INTO review ( movie_id, score, title, " +
                    "content, publication_date) VALUES (?,?,?,?,?)");

            ps.setLong(1, review.movie().id());
            ps.setInt(2, review.score());
            ps.setString(3, review.title());
            ps.setString(4, review.content());
            ps.setDate(5, Date.valueOf(review.publicationDate()));
            ps.executeUpdate();
        }
    }

    @Override
    public void update(final Review newReview) throws SQLException, ClassNotFoundException {
        log.debug(String.format("Updating %s in the database.", newReview.toString()));
        try (final Connection conn = DataSourceJdbc.getConnection()) {
            final PreparedStatement ps = conn.prepareStatement("UPDATE review SET score = ?, movie_id = ?, " +
                    "title = ?, content =?, publication_date = ? WHERE id = ?");

            ps.setInt(1, newReview.score());
            ps.setLong(2, newReview.movie().id());
            ps.setString(3, newReview.title());
            ps.setString(4, newReview.content());
            ps.setDate(5, Date.valueOf(newReview.publicationDate()));
            ps.setLong(6, newReview.id());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(final Long id) throws SQLException, ClassNotFoundException {
        log.debug(String.format("Deleting review with id = %d in the database.", id));
        try (final Connection conn = DataSourceJdbc.getConnection()) {
            final PreparedStatement ps = conn.prepareStatement("DELETE FROM review WHERE id = ?");
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}
