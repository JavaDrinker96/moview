package com.example.moview.repository;


import com.example.moview.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByMovieId(Long movieId);

    @Query(value = "SELECT a.id FROM Review r LEFT JOIN r.author a WHERE r.id = :id")
    Long getAuthorIdById(@Param("id") Long id);
}