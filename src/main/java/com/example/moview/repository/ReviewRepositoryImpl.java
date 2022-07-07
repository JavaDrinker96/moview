package com.example.moview.repository;

import com.example.moview.config.db.DataSource;
import com.example.moview.exception.DataBaseException;
import com.example.moview.model.Review;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReviewRepositoryImpl implements ReviewRepository {

    static final Logger log = (Logger) LogManager.getRootLogger();

    @Override
    public void create(final Review review) {
        log.info("Saving review data to the database.");
        try (final Connection conn = DataSource.getConnection()) {
            final PreparedStatement ps = conn.prepareStatement("INSERT INTO review ( movie_id, score, title, " +
                    "content, publication_date) VALUES (?,?,?,?,?)");

            ps.setLong(1, review.getMovie().getId());
            ps.setInt(2, review.getScore());
            ps.setString(3, review.getTitle());
            ps.setString(4, review.getContent());
            ps.setDate(5, Date.valueOf(review.getPublicationDate()));
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("The review data could not be written to the database.");
            throw new DataBaseException("The review data could not be written to the database.");
        }
    }

    @Override
    public void update(final Review newReview) {
        log.info("Updating review data to the database.");
        try (final Connection conn = DataSource.getConnection()) {
            final PreparedStatement ps = conn.prepareStatement("UPDATE review SET score = ?, movie_id = ?, " +
                    "title = ?, content =?, publication_date = ? WHERE id = ?");

            ps.setInt(1, newReview.getScore());
            ps.setLong(2, newReview.getMovie().getId());
            ps.setString(3, newReview.getTitle());
            ps.setString(4, newReview.getContent());
            ps.setDate(5, Date.valueOf(newReview.getPublicationDate()));
            ps.setLong(6, newReview.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("The review data was not updated in the database.");
            throw new DataBaseException("The review data was not updated in the database.");
        }
    }

    @Override
    public void delete(final Long id) {
        log.info("Deleting review data in the database.");
        try (final Connection conn = DataSource.getConnection()) {
            final PreparedStatement ps = conn.prepareStatement("DELETE FROM review WHERE id = ?");
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("The review data was not deleted in the database.");
            throw new DataBaseException("The review data was not deleted in the database.");
        }
    }
}
