package com.example.moview.moview;

import com.example.moview.moview.dto.review.ReviewCreateDto;
import com.example.moview.moview.dto.review.ReviewUpdateDto;
import com.example.moview.moview.model.Genre;
import com.example.moview.moview.model.Movie;
import com.example.moview.moview.model.Review;
import com.example.moview.moview.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;

import static java.time.LocalDate.now;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReviewTest extends AbstractTest {

    @Test
    public void createReview_Success() throws Exception {
        final Genre genre = createDefaultGenre();
        final Movie movie = createDefaultMovie(genre.getId());
        final User user = createDefaultUser();
        final String authorizationHeader = user.getId().toString();

        final ReviewCreateDto reviewCreateDto = ReviewCreateDto.builder()
                .movieId(movie.getId())
                .score(defaultReviewScore)
                .title(reviewTitle)
                .content(reviewContent)
                .build();

        final RequestBuilder requestBuilder = post("/review")
                .header("Authorization", authorizationHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(reviewCreateDto));

        mvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.movieId", is(movie.getId().intValue())))
                .andExpect(jsonPath("$.score", is(defaultReviewScore)))
                .andExpect(jsonPath("$.title", is(reviewTitle)))
                .andExpect(jsonPath("$.content", is(reviewContent)))
                .andExpect(jsonPath("$.publicationDate", is(formatLocalDate(now()))));
    }

    @Test
    public void updateReview_Success() throws Exception {
        final Genre genre = createDefaultGenre();
        final Movie movie1 = createDefaultMovie(genre.getId());
        final Movie movie2 = createDefaultMovie(genre.getId());
        final User user = createDefaultUser();
        final String authorizationHeader = user.getId().toString();

        final Review review = createDefaultReview(movie1.getId(), user.getId(), defaultReviewScore);
        final int newScore = 100;
        final String newTitle = "newTitle";
        final String newContent = "newContent";


        final ReviewUpdateDto reviewUpdateDto = ReviewUpdateDto.builder()
                .id(review.getId())
                .movieId(movie2.getId())
                .score(newScore)
                .title(newTitle)
                .content(newContent)
                .build();

        final RequestBuilder requestBuilder = put("/review")
                .header("Authorization", authorizationHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(reviewUpdateDto));

        mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(review.getId().intValue())))
                .andExpect(jsonPath("$.movieId", is(movie2.getId().intValue())))
                .andExpect(jsonPath("$.score", is(newScore)))
                .andExpect(jsonPath("$.title", is(newTitle)))
                .andExpect(jsonPath("$.content", is(newContent)));
    }
}
