package com.example.moview.moview;

import com.example.moview.moview.dto.review.ReviewCreateDto;
import com.example.moview.moview.dto.review.ReviewUpdateDto;
import com.example.moview.moview.exception.ForbiddenAuthorException;
import com.example.moview.moview.model.Genre;
import com.example.moview.moview.model.Movie;
import com.example.moview.moview.model.Review;
import com.example.moview.moview.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import javax.persistence.EntityNotFoundException;

import static java.time.LocalDate.now;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReviewTest extends AbstractTest {

    @Test
    void createReview_Success() throws Exception {
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
    void createReviewWithNonExistUser_Exception() throws Exception {
        final Genre genre = createDefaultGenre();
        final Movie movie = createDefaultMovie(genre.getId());
        final String nonExistUserId = String.valueOf(1);

        final ReviewCreateDto reviewCreateDto = ReviewCreateDto.builder()
                .movieId(movie.getId())
                .score(defaultReviewScore)
                .title(reviewTitle)
                .content(reviewContent)
                .build();

        final RequestBuilder requestBuilder = post("/review")
                .header("Authorization", nonExistUserId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(reviewCreateDto));

        final MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException(), is(instanceOf(EntityNotFoundException.class)));
    }

    @Test
    void updateReview_Success() throws Exception {
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

    @Test
    void updateReviewNotByItsOwner_Exception() throws Exception {
        final Genre genre = createDefaultGenre();

        final Movie movie1 = createDefaultMovie(genre.getId());
        final Movie movie2 = createDefaultMovie(genre.getId());

        final User user1 = createDefaultUser(1);
        final User user2 = createDefaultUser(2);

        final String authorizationHeader = user2.getId().toString();

        final Review review = createDefaultReview(movie1.getId(), user1.getId(), defaultReviewScore);
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

        final MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isForbidden())
                .andReturn();

        assertThat(result.getResolvedException(), is(instanceOf(ForbiddenAuthorException.class)));
    }
}