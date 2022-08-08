package com.example.moview.moview;

import com.example.moview.moview.dto.movie.MovieCreateDto;
import com.example.moview.moview.model.Genre;
import com.example.moview.moview.model.Movie;
import com.example.moview.moview.model.Review;
import com.example.moview.moview.model.User;
import com.example.moview.moview.pojo.OmdbMovie;
import com.example.moview.moview.repository.ReviewRepository;
import com.example.moview.moview.service.OmdbService;
import com.example.moview.moview.service.OmdbServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.OptionalDouble;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
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
    void createMovieWithoutGenre_Exception() throws Exception {
        final String title = "title";
        final String description = "description";
        final String releaseDate = "01.01.1999";
        final String duration = "01:00:00";
        final Long nonExistGenreId = 1L;

        final String request = objectWriter.writeValueAsString(MovieCreateDto.builder()
                .title(title)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .genreIds(Set.of(nonExistGenreId))
                .build());

        final RequestBuilder requestBuilder = post("/movie")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request);

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

        final String request = objectWriter.writeValueAsString(MovieCreateDto.builder()
                .title(title)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .genreIds(Set.of(nonExistGenreId))
                .build());

        final RequestBuilder requestBuilder = post("/movie")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request);

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

    @Test
    void getNonExistMovie_Exception() throws Exception {
        final Long nonExistId = 1L;
        final MvcResult result = mvc.perform(get("/movie/{id}", nonExistId))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResolvedException(), is(instanceOf(NoSuchElementException.class)));
    }

    @Test
    void getRecommendation_Success() throws Exception {
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

        final Integer movie1ExpectedRating = 84;
        final Integer movie2ExpectedRating = 87;
        final Integer movie3ExpectedRating = 81;
        final Integer movie4ExpectedRating = 81;
        final Integer movie5ExpectedRating = 90;

        final RequestBuilder requestBuilder = get("/movie/top")
                .header("Authorization", authorizationHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(genreIdsForRecommendation));

        mvc.perform(requestBuilder)
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.length()", is(5)))

                .andExpect(jsonPath("$.[0].id", is(movie5.getId().intValue())))
                .andExpect(jsonPath("$.[0].title", is(movieTitle + "5")))
                .andExpect(jsonPath("$.[0].description", is(movieDescription + "5")))
                .andExpect(jsonPath("$.[0].releaseDate", is(formatLocalDate(movieReleaseDate))))
                .andExpect(jsonPath("$.[0].duration", is(formatDuration(movieDuration))))
                .andExpect(jsonPath("$.[0].rating", is(movie5ExpectedRating)))

                .andExpect(jsonPath("$.[0].genres[0].id", is(genre1.getId().intValue())))
                .andExpect(jsonPath("$.[0].genres[0].name", is(genreName + "1")))
                .andExpect(jsonPath("$.[0].genres.length()", is(1)))

                .andExpect(jsonPath("$.[1].id", is(movie2.getId().intValue())))
                .andExpect(jsonPath("$.[1].title", is(movieTitle + "2")))
                .andExpect(jsonPath("$.[1].description", is(movieDescription + "2")))
                .andExpect(jsonPath("$.[1].releaseDate", is(formatLocalDate(movieReleaseDate))))
                .andExpect(jsonPath("$.[1].duration", is(formatDuration(movieDuration))))
                .andExpect(jsonPath("$.[1].rating", is(movie2ExpectedRating)))

                .andExpect(jsonPath("$.[1].genres[0].id", is(genre2.getId().intValue())))
                .andExpect(jsonPath("$.[1].genres[0].name", is(genreName + "2")))
                .andExpect(jsonPath("$.[1].genres.length()", is(1)))

                .andExpect(jsonPath("$.[2].id", is(movie1.getId().intValue())))
                .andExpect(jsonPath("$.[2].title", is(movieTitle + "1")))
                .andExpect(jsonPath("$.[2].description", is(movieDescription + "1")))
                .andExpect(jsonPath("$.[2].releaseDate", is(formatLocalDate(movieReleaseDate))))
                .andExpect(jsonPath("$.[2].duration", is(formatDuration(movieDuration))))
                .andExpect(jsonPath("$.[2].rating", is(movie1ExpectedRating)))

                .andExpect(jsonPath("$.[2].genres[0].id", is(genre1.getId().intValue())))
                .andExpect(jsonPath("$.[2].genres[0].name", is(genreName + "1")))
                .andExpect(jsonPath("$.[2].genres.length()", is(1)))

                .andExpect(jsonPath("$.[3].id", is(movie4.getId().intValue())))
                .andExpect(jsonPath("$.[3].title", is(movieTitle + "4")))
                .andExpect(jsonPath("$.[3].description", is(movieDescription + "4")))
                .andExpect(jsonPath("$.[3].releaseDate", is(formatLocalDate(movieReleaseDate))))
                .andExpect(jsonPath("$.[3].duration", is(formatDuration(movieDuration))))
                .andExpect(jsonPath("$.[3].rating", is(movie4ExpectedRating)))

                .andExpect(jsonPath("$.[3].genres[0].id", is(genre1.getId().intValue())))
                .andExpect(jsonPath("$.[3].genres[0].name", is(genreName + "1")))
                .andExpect(jsonPath("$.[3].genres.length()", is(1)))

                .andExpect(jsonPath("$.[4].id", is(movie3.getId().intValue())))
                .andExpect(jsonPath("$.[4].title", is(movieTitle + "3")))
                .andExpect(jsonPath("$.[4].description", is(movieDescription + "3")))
                .andExpect(jsonPath("$.[4].releaseDate", is(formatLocalDate(movieReleaseDate))))
                .andExpect(jsonPath("$.[4].duration", is(formatDuration(movieDuration))))
                .andExpect(jsonPath("$.[4].rating", is(movie3ExpectedRating)))

                .andExpect(jsonPath("$.[4].genres[0].id", is(genre3.getId().intValue())))
                .andExpect(jsonPath("$.[4].genres[0].name", is(genreName + "3")))
                .andExpect(jsonPath("$.[4].genres.length()", is(1)));
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


    @Test
    void test() throws JsonProcessingException {
        final OmdbService omdbService = new OmdbServiceImpl();
        System.out.println(omdbService.getRatingByMovieName("Lost Ark"));
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