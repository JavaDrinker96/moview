package com.example.moview.moview;

import com.example.moview.moview.config.db.HibernateTestConfig;
import com.example.moview.moview.config.spring.SpringConfig;
import com.example.moview.moview.dto.movie.MovieCreateDto;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        HibernateTestConfig.class,
        SpringConfig.class
})
@WebAppConfiguration
public class MovieTest {

    private final String DATE_PATTERN = "dd.MM.yyyy";
    private final String DURATION_PATTERN = "%02d:%02d:%02d";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private GenreService genreService;

    @Autowired
    private UserService userService;

    @Autowired
    private MovieService movieService;

    @Autowired
    ReviewService reviewService;

    private MockMvc mvc;
    private ObjectWriter objectWriter;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        objectWriter = mapper.writer().withDefaultPrettyPrinter();
    }

    @Test
    public void createMovie_Success() throws Exception {
        final String genreName = "genreName";
        final Genre genre = genreService.create(Genre.builder().name(genreName).build());

        final String title = "title";
        final String description = "description";
        final String releaseDate = "01.01.1999";
        final String duration = "01:00:00";
        final String genreId = "1";

        final MovieCreateDto movieCreateDto = MovieCreateDto.builder()
                .title(title)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .genreIds(Set.of(Long.valueOf(genreId)))
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
        final String genreName = "genreName";
        final Genre genre = genreService.create(Genre.builder().name(genreName).build());

        final String userName = "userName";
        final String userLastName = "userLastName";
        final LocalDate userBirthday = LocalDate.of(2000, 1, 1);
        final String userEmail = "email@gmail.com";
        final User user = userService.create(User.builder()
                .firstName(userName)
                .lastName(userLastName)
                .birthday(userBirthday)
                .email(userEmail)
                .build());

        final String movieTitle = "movieTitle";
        final String movieDescription = "movieDescription";
        final LocalDate movieReleaseDate = LocalDate.of(1999, 1, 1);
        final Duration movieDuration = Duration.ofHours(1);
        final Set<Genre> movieGenres = Set.of(Genre.builder().id(genre.getId()).build());
        final Movie movie = movieService.create(Movie.builder()
                .title(movieTitle)
                .description(movieDescription)
                .releaseDate(movieReleaseDate)
                .duration(movieDuration)
                .genres(movieGenres)
                .build());

        final Integer reviewScore = 50;
        final String reviewTitle = "reviewTitle";
        final String reviewContent = "reviewContent";
        final Movie reviewMovie = Movie.builder().id(movie.getId()).build();
        final User reviewAuthor = User.builder().id(user.getId()).build();
        final Review review = reviewService.create(Review.builder()
                .score(reviewScore)
                .title(reviewTitle)
                .content(reviewContent)
                .movie(reviewMovie)
                .author(reviewAuthor)
                .build());
        mvc.perform(get("/movie/{id}", movie.getId().toString())).andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id", is(movie.getId().intValue())))
                .andExpect(jsonPath("$.title", is(movieTitle)))
                .andExpect(jsonPath("$.description", is(movieDescription)))
                .andExpect(jsonPath("$.releaseDate", is(formatLocalDate(movieReleaseDate))))
                .andExpect(jsonPath("$.duration", is(formatDuration(movieDuration))))
                .andExpect(jsonPath("$.rating", is(reviewScore)))

                .andExpect(jsonPath("$.reviews.length()", is(1)))
                .andExpect(jsonPath("$.reviews[0].id", is(review.getId().intValue())))
                .andExpect(jsonPath("$.reviews[0].movieId", is(movie.getId().intValue())))
                .andExpect(jsonPath("$.reviews[0].score", is(reviewScore)))
                .andExpect(jsonPath("$.reviews[0].title", is(reviewTitle)))
                .andExpect(jsonPath("$.reviews[0].content", is(reviewContent)))
                .andExpect(jsonPath("$.reviews[0].publicationDate",
                        is(formatLocalDate(review.getPublicationDate()))))


                .andExpect(jsonPath("$.genres.length()", is(1)))
                .andExpect(jsonPath("$.genres[0].id", is(genre.getId().intValue())));
    }

    private String formatLocalDate(final LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    private String formatDuration(final Duration duration) {
        return String.format(DURATION_PATTERN, duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart());
    }
}
