package com.example.moview.moview.repository;


import com.example.moview.moview.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value = "SELECT r FROM Review r WHERE r.movie.id = :id")
    List<Review> findAllByMovieId(@Param("id") Long movieId);

    @Query(value = "SELECT a.id FROM Review r LEFT JOIN r.author a WHERE r.id = :id")
    Long getAuthorIdById(@Param("id") Long id);
}