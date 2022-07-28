package com.example.moview.moview.repository;


import com.example.moview.moview.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value = "SELECT * FROM review r WHERE r.movie_id = :id", nativeQuery = true)
    List<Review> findAllByMovieId(@Param("id") Long movieId);
}
