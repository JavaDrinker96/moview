package com.example.moview.moview;

import com.example.moview.moview.dto.movie.MovieCreateDto;
import com.example.moview.moview.model.Genre;
import com.example.moview.moview.model.Movie;
import com.example.moview.moview.model.Review;
import com.example.moview.moview.model.User;
import com.example.moview.moview.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.OptionalDouble;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MovieTest extends AbstractTest {

    @Autowired
    private ReviewRepository reviewRepository;


    @Test
    void createMovie_Success() throws Exception {
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

        final String requestBody = objectWriter.writeValueAsString(movieCreateDto);
        mvc.perform(post("/movie").contentType(MediaType.APPLICATION_JSON).content(requestBody))

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
    void createMovieWithoutGenre_Exception() throws Exception {
        final String title = "title";
        final String description = "description";
        final String releaseDate = "01.01.1999";
        final String duration = "01:00:00";
        final Long nonExistGenreId = 1L;

        final String requestBody = objectWriter.writeValueAsString(MovieCreateDto.builder()
                .title(title)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .genreIds(Set.of(nonExistGenreId))
                .build());

        final RequestBuilder requestBuilder = post("/movie")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        final MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException(), is(instanceOf(DataIntegrityViolationException.class)));
    }

    @Test
    void createMovieWithNonValidReleaseDate_Exception() throws Exception {
        final String title = "title";
        final String description = "description";
        final String releaseDate = "01.01-1999";
        final String duration = "01:00:00";
        final Long nonExistGenreId = 1L;

        final String requestBody = objectWriter.writeValueAsString(MovieCreateDto.builder()
                .title(title)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .genreIds(Set.of(nonExistGenreId))
                .build());

        final RequestBuilder requestBuilder = post("/movie")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        final MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException(), is(instanceOf(MethodArgumentNotValidException.class)));
    }

    @Test
    void getMovieWithReviews_Success() throws Exception {
        final Genre genre = createDefaultGenre();
        final User user = createDefaultUser();
        final Movie movie = createDefaultMovie(genre.getId());

        final int review1Score = 50;
        final Review review1 = createDefaultReview(movie.getId(), user.getId(), 1, review1Score);
        final int review2Score = 51;
        final Review review2 = createDefaultReview(movie.getId(), user.getId(), 2, review2Score);

        mvc.perform(get("/movie/{id}", movie.getId()))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id", is(movie.getId().intValue())))
                .andExpect(jsonPath("$.title", is(defaultMovieTitle)))
                .andExpect(jsonPath("$.description", is(defaultMovieDescription)))
                .andExpect(jsonPath("$.releaseDate", is(formatLocalDate(defaultMovieReleaseDate))))
                .andExpect(jsonPath("$.duration", is(formatDuration(defaultMovieDuration))))
                .andExpect(jsonPath("$.rating", is(calculateMovieRating(movie.getId()))))

                .andExpect(jsonPath("$.reviews.length()", is(2)))

                .andExpect(jsonPath("$.reviews[0].id", is(review2.getId().intValue())))
                .andExpect(jsonPath("$.reviews[0].movieId", is(movie.getId().intValue())))
                .andExpect(jsonPath("$.reviews[0].score", is(review2Score)))
                .andExpect(jsonPath("$.reviews[0].title", is(defaultReviewTitle + "2")))
                .andExpect(jsonPath("$.reviews[0].content", is(defaultReviewContent + "2")))
                .andExpect(jsonPath("$.reviews[0].publicationDate",
                        is(formatLocalDate(review2.getPublicationDate()))))

                .andExpect(jsonPath("$.reviews[1].id", is(review1.getId().intValue())))
                .andExpect(jsonPath("$.reviews[1].movieId", is(movie.getId().intValue())))
                .andExpect(jsonPath("$.reviews[1].score", is(review1Score)))
                .andExpect(jsonPath("$.reviews[1].title", is(defaultReviewTitle + "1")))
                .andExpect(jsonPath("$.reviews[1].content", is(defaultReviewContent + "1")))
                .andExpect(jsonPath("$.reviews[1].publicationDate",
                        is(formatLocalDate(review1.getPublicationDate()))))

                .andExpect(jsonPath("$.genres.length()", is(1)))
                .andExpect(jsonPath("$.genres[0].id", is(genre.getId().intValue())))
                .andExpect(jsonPath("$.genres[0].name", is(defaultGenreName)));
    }

    @Test
    void getNonExistMovie_Exception() throws Exception {
        final Long nonExistId = 1L;
        final MvcResult result = mvc.perform(get("/movie/{id}", nonExistId))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException(), is(instanceOf(NoSuchElementException.class)));
    }

    @Test
    void givenGenresIds_whenFindRecommendations_thenReturn5Movies() throws Exception {
        final Genre genre1 = createDefaultGenre(1);
        final Genre genre2 = createDefaultGenre(2);
        final Genre genre3 = createDefaultGenre(3);
        final List<Long> genreIdsForRecommendation = List.of(genre1.getId(), genre2.getId());

        final User user1 = createDefaultUser(1);
        final User user2 = createDefaultUser(2);
        final User user3 = createDefaultUser(3);
        final String authorizationHeader = user1.getId().toString();

        final Movie movie1 = createDefaultMovie(genre1.getId(), 1);
        createDefaultReview(movie1.getId(), user1.getId(), 1, 100);
        createDefaultReview(movie1.getId(), user2.getId(), 2, 50);
        createDefaultReview(movie1.getId(), user3.getId(), 3, 100);

        final Movie movie2 = createDefaultMovie(genre2.getId(), 2);
        createDefaultReview(movie2.getId(), user1.getId(), 6, 90);
        createDefaultReview(movie2.getId(), user2.getId(), 4, 90);
        createDefaultReview(movie2.getId(), user1.getId(), 5, 80);

        final Movie movie3 = createDefaultMovie(genre3.getId(), 3);
        createDefaultReview(movie3.getId(), user3.getId(), 7, 81);
        createDefaultReview(movie3.getId(), user1.getId(), 8, 80);

        final Movie movie4 = createDefaultMovie(genre1.getId(), 4);
        createDefaultReview(movie4.getId(), user1.getId(), 9, 81);
        createDefaultReview(movie4.getId(), user2.getId(), 10, 92);
        createDefaultReview(movie4.getId(), user3.getId(), 11, 70);

        final Movie movie5 = createDefaultMovie(genre1.getId(), 5);
        createDefaultReview(movie5.getId(), user1.getId(), 12, 90);

        final Movie movie6 = createDefaultMovie(genre2.getId(), 6);
        createDefaultReview(movie6.getId(), user3.getId(), 13, 80);

        final Movie movie7 = createDefaultMovie(genre3.getId(), 7);
        createDefaultReview(movie7.getId(), user1.getId(), 14, 80);

        final RequestBuilder requestBuilder = get("/movie/top")
                .header("Authorization", authorizationHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(genreIdsForRecommendation));

        final int lowerRatingLimit = 79;

        mvc.perform(requestBuilder)
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.length()", is(5)))

                .andExpect(jsonPath("$.[0].rating", greaterThan(lowerRatingLimit)))
                .andExpect(jsonPath("$.[1].rating", greaterThan(lowerRatingLimit)))
                .andExpect(jsonPath("$.[2].rating", greaterThan(lowerRatingLimit)))
                .andExpect(jsonPath("$.[3].rating", greaterThan(lowerRatingLimit)))
                .andExpect(jsonPath("$.[4].rating",greaterThan(lowerRatingLimit)));
    }

    @Test
    void getRecommendationByNonExistGenre_Exception() throws Exception {
        final Genre genre = createDefaultGenre(1);
        final User user = createDefaultUser(1);
        final Movie movie = createDefaultMovie(genre.getId(), 1);
        createDefaultReview(movie.getId(), user.getId(), 1, 100);

        final List<Long> genreIdsForRecommendation = List.of(genre.getId(), genre.getId() + 10L);

        final RequestBuilder requestBuilder = get("/movie/top")
                .header("Authorization", user.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(genreIdsForRecommendation));

        final MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException(), is(instanceOf(EntityNotFoundException.class)));
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