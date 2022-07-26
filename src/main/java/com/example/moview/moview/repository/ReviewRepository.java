package com.example.moview.moview.repository;


import com.example.moview.moview.model.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends BaseRepository<Review> {

    @Query(value = "SELECT r FROM Review r WHERE r.movie.id = :id")
    List<Review> findAllByMovieId(@Param("id") Long movieId);

    @Query(value = "SELECT r.author.id FROM Review r WHERE r.id = :id")
    Long getAuthorIdById(@Param("id") Long id);
}