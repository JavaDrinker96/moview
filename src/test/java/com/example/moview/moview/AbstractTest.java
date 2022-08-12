package com.example.moview.moview;

import com.example.moview.moview.config.db.HibernateTestConfig;
import com.example.moview.moview.config.spring.SpringConfig;
import com.example.moview.moview.model.Genre;
import com.example.moview.moview.model.Movie;
import com.example.moview.moview.model.Review;
import com.example.moview.moview.model.User;
import com.example.moview.moview.service.GenreService;
import com.example.moview.moview.service.MovieService;
import com.example.moview.moview.service.ReviewService;
import com.example.moview.moview.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        HibernateTestConfig.class,
        SpringConfig.class
})
@WebAppConfiguration
public abstract class AbstractTest {

    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private static final String DURATION_PATTERN = "%02d:%02d:%02d";


    //data for Genre
    protected static final String defaultGenreName = "genreName";

    //data for User
    protected static final String defaultUserName = "userName";
    protected static final String defaultUserLastName = "userLastName";
    protected static final LocalDate defaultUserBirthday = LocalDate.of(2000, 1, 1);
    protected static final String defaultUserEmail = "email@gmail.com";

    //data for Movie
    protected static final String defaultMovieTitle = "movieTitle";
    protected static final String defaultMovieDescription = "movieDescription";
    protected static final LocalDate defaultMovieReleaseDate = LocalDate.of(1999, 1, 1);
    protected static final Duration defaultMovieDuration = Duration.ofHours(1);

    //data for Review
    protected static final String defaultReviewTitle = "reviewTitle";
    protected static final String defaultReviewContent = "reviewContent";
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
        return genreService.create(Genre.builder().name(defaultGenreName).build());
    }

    protected Genre createDefaultGenre(final int number) {
        final StringBuilder sbGenreName = new StringBuilder(defaultGenreName);
        return genreService.create(Genre.builder().name(sbGenreName.append(number).toString()).build());
    }

    protected User createDefaultUser() {
        return userService.create(User.builder()
                .firstName(defaultUserName)
                .lastName(defaultUserLastName)
                .birthday(defaultUserBirthday)
                .email(defaultUserEmail)
                .build());
    }

    protected User createDefaultUser(final int number) {
        final StringBuilder sbUserName = new StringBuilder(defaultUserName);
        final StringBuilder sbUserLastName = new StringBuilder(defaultUserLastName);
        final StringBuilder sbUserEmail = new StringBuilder(defaultUserEmail);
        return userService.create(User.builder()
                .firstName(sbUserName.append(number).toString())
                .lastName(sbUserLastName.append(number).toString())
                .birthday(defaultUserBirthday)
                .email(sbUserEmail.insert(5, number).toString())
                .build());
    }

    protected Movie createDefaultMovie(final Long genreId) {

        final Set<Genre> movieGenres = Set.of(Genre.builder().id(genreId).build());
        return movieService.create(Movie.builder()
                .title(defaultMovieTitle)
                .description(defaultMovieDescription)
                .releaseDate(defaultMovieReleaseDate)
                .duration(defaultMovieDuration)
                .genres(movieGenres)
                .build());
    }

    protected Movie createDefaultMovie(final Long genreId, final int number) {
        final StringBuilder sbMovieTitle = new StringBuilder(defaultMovieTitle);
        final StringBuilder sbMovieDescription = new StringBuilder(defaultMovieDescription);
        final Set<Genre> movieGenres = Set.of(Genre.builder().id(genreId).build());
        return movieService.create(Movie.builder()
                .title(sbMovieTitle.append(number).toString())
                .description(sbMovieDescription.append(number).toString())
                .releaseDate(defaultMovieReleaseDate)
                .duration(defaultMovieDuration)
                .genres(movieGenres)
                .build());
    }

    protected Review createDefaultReview(final Long movieId, final Long authorId, final int reviewScore) {
        return reviewService.create(Review.builder()
                .score(reviewScore)
                .title(defaultReviewTitle)
                .content(defaultReviewContent)
                .movie(Movie.builder().id(movieId).build())
                .author(User.builder().id(authorId).build())
                .build());
    }

    protected Review createDefaultReview(final Long movieId,
                                         final Long authorId,
                                         final int number,
                                         final int reviewScore) {

        final StringBuilder sbReviewTitle = new StringBuilder(defaultReviewTitle);
        final StringBuilder sbReviewContent = new StringBuilder(defaultReviewContent);
        return reviewService.create(Review.builder()
                .score(reviewScore)
                .title(sbReviewTitle.append(number).toString())
                .content(sbReviewContent.append(number).toString())
                .movie(Movie.builder().id(movieId).build())
                .author(User.builder().id(authorId).build())
                .build());
    }
}