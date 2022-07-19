package com.example.moview.moview.repository;


import com.example.moview.moview.model.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends BaseRepository<Review> {

    @Query(value = "SELECT * FROM review r WHERE r.movie_id = :id", nativeQuery = true)
    List<Review> findAllByMovieId(@Param("id") Long movieId);

    @Query(value = "SELECT app_user_id FROM review WHERE id = :id", nativeQuery = true)
    Long getAuthorIdById(@Param("id") Long id);
}