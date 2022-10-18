package com.example.moview.repository;

import com.example.moview.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @EntityGraph(attributePaths = {"reviews", "genres"})
    Optional<Movie> findById(Long id);

    @EntityGraph(attributePaths = {"genres"})
    Page<Movie> findAllByTitleContaining(String title, Pageable pageable);

    @EntityGraph(attributePaths = {"genres"})
    @Query(value = "SELECT m FROM Movie m WHERE m.title LIKE CONCAT('%',:title,'%') AND m.user.id =:userId")
    Page<Movie> findAllByTitleAndUserId(String title, Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"reviews", "genres"})
    @Query(value = "SELECT m FROM Movie m LEFT JOIN m.reviews r WHERE r.author.id = :userId")
    List<Movie> getMoviesByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT m.user.id FROM Movie m WHERE m.id =:movieId")
    Optional<Long> findUserIdByMovieId(Long movieId);
}