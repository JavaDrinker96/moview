package com.example.moview;

import com.example.moview.model.Genre;
import com.example.moview.model.Movie;
import com.example.moview.model.Review;
import com.example.moview.model.User;
import com.example.moview.service.GenreService;
import com.example.moview.service.MovieService;
import com.example.moview.service.ReviewService;
import com.example.moview.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;


public abstract class AbstractTest {

    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private static final String DURATION_PATTERN = "%02d:%02d:%02d";


    //data for Genre
    protected static final String genreName = "genreName";

    //data for User
    protected static final String userName = "userName";
    protected static final String userLastName = "userLastName";
    protected static final LocalDate userBirthday = LocalDate.of(2000, 1, 1);
    protected static final String userEmail = "email@gmail.com";

    //data for Movie
    protected static final String movieTitle = "movieTitle";
    protected static final String movieDescription = "movieDescription";
    protected static final LocalDate movieReleaseDate = LocalDate.of(1999, 1, 1);
    protected static final Duration movieDuration = Duration.ofHours(1);

    //data for Review
    protected static final String reviewTitle = "reviewTitle";
    protected static final String reviewContent = "reviewContent";
    protected static final int defaultReviewScore = 50;


    @Autowired
    protected WebApplicationContext context;
    @Autowired
    protected MovieService movieService;
    @Autowired
    protected UserService userService;
    @Autowired
    protected GenreService genreService;
    @Autowired
    protected ReviewService reviewService;

    protected MockMvc mvc;
    protected ObjectWriter objectWriter;

    @BeforeEach
    protected void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        objectWriter = mapper.writer().withDefaultPrettyPrinter();
    }

    protected String formatLocalDate(final LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    protected String formatDuration(final Duration duration) {
        return String.format(DURATION_PATTERN, duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart());
    }

    protected Genre createDefaultGenre() {
        return genreService.create(Genre.builder().name(genreName).build());
    }

    protected Genre createDefaultGenre(final int number) {
        final StringBuilder sbGenreName = new StringBuilder(genreName);
        return genreService.create(Genre.builder().name(sbGenreName.append(number).toString()).build());
    }

    protected User createDefaultUser() {
        return userService.create(User.builder()
                .firstName(userName)
                .lastName(userLastName)
                .birthday(userBirthday)
                .email(userEmail)
                .build());
    }

    protected User createDefaultUser(final int number) {
        final StringBuilder sbUserName = new StringBuilder(userName);
        final StringBuilder sbUserLastName = new StringBuilder(userLastName);
        final StringBuilder sbUserEmail = new StringBuilder(userEmail);
        return userService.create(User.builder()
                .firstName(sbUserName.append(number).toString())
                .lastName(sbUserLastName.append(number).toString())
                .birthday(userBirthday)
                .email(sbUserEmail.insert(5, number).toString())
                .build());
    }

    protected Movie createDefaultMovie(final Long genreId) {

        final Set<Genre> movieGenres = Set.of(Genre.builder().id(genreId).build());
        return movieService.create(Movie.builder()
                .title(movieTitle)
                .description(movieDescription)
                .releaseDate(movieReleaseDate)
                .duration(movieDuration)
                .genres(movieGenres)
                .build());
    }

    protected Movie createDefaultMovie(final Long genreId, final int number) {
        final StringBuilder sbMovieTitle = new StringBuilder(movieTitle);
        final StringBuilder sbMovieDescription = new StringBuilder(movieDescription);
        final Set<Genre> movieGenres = Set.of(Genre.builder().id(genreId).build());
        return movieService.create(Movie.builder()
                .title(sbMovieTitle.append(number).toString())
                .description(sbMovieDescription.append(number).toString())
                .releaseDate(movieReleaseDate)
                .duration(movieDuration)
                .genres(movieGenres)
                .build());
    }

    protected Review createDefaultReview(final Long movieId, final Long authorId, final int reviewScore) {
        return reviewService.create(Review.builder()
                .score(reviewScore)
                .title(reviewTitle)
                .content(reviewContent)
                .movie(Movie.builder().id(movieId).build())
                .author(User.builder().id(authorId).build())
                .build());
    }

    protected Review createDefaultReview(final Long movieId,
                                         final Long authorId,
                                         final int number,
                                         final int reviewScore) {

        final StringBuilder sbReviewTitle = new StringBuilder(reviewTitle);
        final StringBuilder sbReviewContent = new StringBuilder(reviewContent);
        return reviewService.create(Review.builder()
                .score(reviewScore)
                .title(sbReviewTitle.append(number).toString())
                .content(sbReviewContent.append(number).toString())
                .movie(Movie.builder().id(movieId).build())
                .author(User.builder().id(authorId).build())
                .build());
    }
}