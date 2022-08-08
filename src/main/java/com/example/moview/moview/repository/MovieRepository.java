package com.example.moview.moview.repository;

import com.example.moview.moview.model.Movie;
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
    Page<Movie> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"reviews", "genres"})
    @Query(value = "SELECT m FROM Movie m LEFT JOIN m.reviews r WHERE r.author.id = :userId")
    List<Movie> getMoviesByUserId(@Param("userId") Long userId);
}