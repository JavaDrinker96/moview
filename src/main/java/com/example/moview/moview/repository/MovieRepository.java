package com.example.moview.moview.repository;

import com.example.moview.moview.model.Movie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends BaseRepository<Movie> {

    /**
     * @param userId             is user id
     * @param avgUsersMovieScore is the lower bound of the average rating assigned by the user for movie search
     * @return movies that the user gave high ratings on average are sorted in descending order of rating
     */
    @Query(value = "SELECT m.* FROM review r JOIN movie m ON r.movie_id = m.id GROUP BY m.id, r.app_user_id, m.rating" +
            " HAVING r.app_user_id = :userId AND avg(score) >= :avgUsersMovieScore ORDER BY m.rating DESC",
            nativeQuery = true)
    List<Movie> getUserMoviesByHisScoreDESC(@Param("userId") Long userId,
                                            @Param("avgUsersMovieScore") Integer avgUsersMovieScore);
}