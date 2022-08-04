package com.example.moview.moview;

import com.example.moview.moview.dto.movie.MovieCreateDto;
import com.example.moview.moview.model.Genre;
import com.example.moview.moview.model.Movie;
import com.example.moview.moview.model.Review;
import com.example.moview.moview.model.User;
import com.example.moview.moview.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.OptionalDouble;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MovieTest extends AbstractTest {

    @Autowired
    private ReviewRepository reviewRepository;


    @Test
    public void createMovie_Success() throws Exception {
        final Genre genre = createDefaultGenre();

        final String title = "title";
        final String description = "description";
        final String releaseDate = "01.01.1999";
        final String duration = "01:00:00";
        final Long genreId = genre.getId();

        final MovieCreateDto movieCreateDto = MovieCreateDto.builder()
                .title(title)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .genreIds(Set.of(genreId))
                .build();

        final String request = objectWriter.writeValueAsString(movieCreateDto);
        mvc.perform(post("/movie").contentType(MediaType.APPLICATION_JSON).content(request))

                .andExpect(status().isCreated())

                .andExpect(jsonPath("$.title", is(title)))
                .andExpect(jsonPath("$.description", is(description)))
                .andExpect(jsonPath("$.releaseDate", is(releaseDate)))
                .andExpect(jsonPath("$.duration", is(duration)))
                .andExpect(jsonPath("$.rating", nullValue()))
                .andExpect(jsonPath("$.genres.length()", is(1)))

                .andExpect(jsonPath("$.genres.[0].id", is(genre.getId().intValue())));
    }

    @Test
    public void getMovieWithReviews_Success() throws Exception {
        final Genre genre = createDefaultGenre();
        final User user = createDefaultUser();
        final Movie movie = createDefaultMovie(genre.getId());

        final int review1Score = 50;
        final Review review1 = createDefaultReview(movie.getId(), user.getId(), 1, review1Score);
        final int review2Score = 51;
        final Review review2 = createDefaultReview(movie.getId(), user.getId(), 2, review2Score);

        mvc.perform(get("/movie/{id}", movie.getId())).andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id", is(movie.getId().intValue())))
                .andExpect(jsonPath("$.title", is(movieTitle)))
                .andExpect(jsonPath("$.description", is(movieDescription)))
                .andExpect(jsonPath("$.releaseDate", is(formatLocalDate(movieReleaseDate))))
                .andExpect(jsonPath("$.duration", is(formatDuration(movieDuration))))
                .andExpect(jsonPath("$.rating", is(calculateMovieRating(movie.getId()))))

                .andExpect(jsonPath("$.reviews.length()", is(2)))

                .andExpect(jsonPath("$.reviews[0].id", is(review2.getId().intValue())))
                .andExpect(jsonPath("$.reviews[0].movieId", is(movie.getId().intValue())))
                .andExpect(jsonPath("$.reviews[0].score", is(review2Score)))
                .andExpect(jsonPath("$.reviews[0].title", is(reviewTitle + "2")))
                .andExpect(jsonPath("$.reviews[0].content", is(reviewContent + "2")))
                .andExpect(jsonPath("$.reviews[0].publicationDate",
                        is(formatLocalDate(review2.getPublicationDate()))))

                .andExpect(jsonPath("$.reviews[1].id", is(review1.getId().intValue())))
                .andExpect(jsonPath("$.reviews[1].movieId", is(movie.getId().intValue())))
                .andExpect(jsonPath("$.reviews[1].score", is(review1Score)))
                .andExpect(jsonPath("$.reviews[1].title", is(reviewTitle + "1")))
                .andExpect(jsonPath("$.reviews[1].content", is(reviewContent + "1")))
                .andExpect(jsonPath("$.reviews[1].publicationDate",
                        is(formatLocalDate(review1.getPublicationDate()))))

                .andExpect(jsonPath("$.genres.length()", is(1)))
                .andExpect(jsonPath("$.genres[0].id", is(genre.getId().intValue())))
                .andExpect(jsonPath("$.genres[0].name", is(genreName)));
    }

    private Integer calculateMovieRating(final Long id) {
        final OptionalDouble optionalAvgMovieRating = reviewRepository.findAllByMovieId(id)
                .stream()
                .mapToLong(Review::getScore)
                .average();

        return optionalAvgMovieRating.isPresent()
                ? Double.valueOf(Math.ceil(optionalAvgMovieRating.getAsDouble())).intValue()
                : null;
    }
}