package com.example.moview.moview.config.mode_mapper;

import com.example.moview.moview.dto.movie.MovieCreateDto;
import com.example.moview.moview.dto.movie.MovieDto;
import com.example.moview.moview.dto.movie.MovieShortDto;
import com.example.moview.moview.dto.movie.MovieUpdateDto;
import com.example.moview.moview.dto.review.ReviewCreateDto;
import com.example.moview.moview.dto.review.ReviewDto;
import com.example.moview.moview.dto.review.ReviewUpdateDto;
import com.example.moview.moview.dto.user.UserCreateDto;
import com.example.moview.moview.dto.user.UserDto;
import com.example.moview.moview.dto.user.UserUpdateDto;
import com.example.moview.moview.model.Movie;
import com.example.moview.moview.model.Review;
import com.example.moview.moview.model.User;
import com.example.moview.moview.util.datetime.DateTimeConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Configuration
public class ModelMapperConfig {

    private final DateTimeConverter dateTimeConverter;

    public ModelMapperConfig(final DateTimeConverter dateTimeConverter) {
        this.dateTimeConverter = dateTimeConverter;
    }

    @Bean
    public ModelMapper modelMapper() {
        final ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STANDARD)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(PRIVATE);

        addMovieConverters(mapper);
        addReviewConverters(mapper);
        addUserConverters(mapper);

        return mapper;
    }

    private void addMovieConverters(final ModelMapper mapper) {
        Converter<Movie, MovieDto> movie2MovieDtoConverter = new Converter<>() {
            @Override
            public MovieDto convert(final MappingContext<Movie, MovieDto> mappingContext) {
                final Movie movie = mappingContext.getSource();

                final List<ReviewDto> reviewDtoList = Objects.isNull(movie.getReviews())
                        ? new ArrayList<>()
                        : movie.getReviews().stream()
                        .map(review -> ReviewDto.builder()
                                .id(review.getId())
                                .created(dateTimeConverter.formatLocalDateTimeToString(review.getCreated()))
                                .updated(dateTimeConverter.formatLocalDateTimeToString(review.getUpdated()))
                                .movieId(movie.getId())
                                .score(review.getScore())
                                .title(review.getTitle())
                                .content(review.getContent())
                                .publicationDate(dateTimeConverter.formatLocalDateToString(review.getPublicationDate()))
                                .build())
                        .collect(Collectors.toList());

                return MovieDto.builder()
                        .id(movie.getId())
                        .created(dateTimeConverter.formatLocalDateTimeToString(movie.getCreated()))
                        .updated(dateTimeConverter.formatLocalDateTimeToString(movie.getUpdated()))
                        .title(movie.getTitle())
                        .description(movie.getDescription())
                        .releaseDate(dateTimeConverter.formatLocalDateToString(movie.getReleaseDate()))
                        .duration(dateTimeConverter.formatDurationToString(movie.getDuration()))
                        .rating(movie.getRating())
                        .reviews(reviewDtoList)
                        .build();
            }
        };

        Converter<MovieCreateDto, Movie> movieCreateDto2MovieConverter = new Converter<>() {
            @Override
            public Movie convert(MappingContext<MovieCreateDto, Movie> mappingContext) {
                final MovieCreateDto dto = mappingContext.getSource();

                return Movie.builder()
                        .title(dto.getTitle())
                        .description(dto.getDescription())
                        .releaseDate(dateTimeConverter.parseLocalDate(dto.getReleaseDate()))
                        .duration(dateTimeConverter.parseDuration(dto.getDuration()))
                        .build();
            }
        };

        Converter<MovieUpdateDto, Movie> movieUpdateDto2MovieConverter = new Converter<>() {
            @Override
            public Movie convert(final MappingContext<MovieUpdateDto, Movie> mappingContext) {
                final MovieUpdateDto movieDto = mappingContext.getSource();

                return Movie.builder()
                        .id(movieDto.getId())
                        .created(dateTimeConverter.parseLocalDateTime(movieDto.getCreated()))
                        .title(movieDto.getTitle())
                        .description(movieDto.getTitle())
                        .releaseDate(dateTimeConverter.parseLocalDate(movieDto.getReleaseDate()))
                        .duration(dateTimeConverter.parseDuration(movieDto.getDuration()))
                        .build();
            }
        };

        Converter<Movie, MovieShortDto> movie2MovieShortDtoConverter = new Converter<>() {
            @Override
            public MovieShortDto convert(final MappingContext<Movie, MovieShortDto> mappingContext) {
                final Movie movie = mappingContext.getSource();

                return MovieShortDto.builder()
                        .id(movie.getId())
                        .created(dateTimeConverter.formatLocalDateTimeToString(movie.getCreated()))
                        .updated(dateTimeConverter.formatLocalDateTimeToString(movie.getUpdated()))
                        .title(movie.getTitle())
                        .description(movie.getDescription())
                        .releaseDate(dateTimeConverter.formatLocalDateToString(movie.getReleaseDate()))
                        .duration(dateTimeConverter.formatDurationToString(movie.getDuration()))
                        .rating(movie.getRating())
                        .build();
            }
        };

        mapper.addConverter(movie2MovieDtoConverter);
        mapper.addConverter(movieCreateDto2MovieConverter);
        mapper.addConverter(movieUpdateDto2MovieConverter);
        mapper.addConverter(movie2MovieShortDtoConverter);
    }

    private void addReviewConverters(final ModelMapper mapper) {
        Converter<ReviewCreateDto, Review> reviewCreateDto2ReviewConverter = new Converter<>() {
            @Override
            public Review convert(final MappingContext<ReviewCreateDto, Review> mappingContext) {
                final ReviewCreateDto dto = mappingContext.getSource();

                return Review.builder()
                        .movie(Movie.builder().id(dto.getMovieId()).build())
                        .score(dto.getScore())
                        .title(dto.getTitle())
                        .content(dto.getContent())
                        .publicationDate(dateTimeConverter.parseLocalDate(dto.getPublicationDate()))
                        .build();
            }
        };

        Converter<ReviewUpdateDto, Review> reviewUpdateDto2ReviewConverter = new Converter<>() {
            @Override
            public Review convert(final MappingContext<ReviewUpdateDto, Review> mappingContext) {
                final ReviewUpdateDto dto = mappingContext.getSource();

                return Review.builder()
                        .id(dto.getId())
                        .created(dateTimeConverter.parseLocalDateTime(dto.getCreated()))
                        .movie(Movie.builder().id(dto.getMovieId()).build())
                        .score(dto.getScore())
                        .title(dto.getTitle())
                        .content(dto.getContent())
                        .publicationDate(dateTimeConverter.parseLocalDate(dto.getPublicationDate()))
                        .build();
            }
        };

        Converter<Review, ReviewDto> review2ReviewDtoConverter = new Converter<>() {
            @Override
            public ReviewDto convert(final MappingContext<Review, ReviewDto> mappingContext) {
                final Review review = mappingContext.getSource();

                return ReviewDto.builder()
                        .id(review.getId())
                        .created(dateTimeConverter.formatLocalDateTimeToString(review.getCreated()))
                        .updated(dateTimeConverter.formatLocalDateTimeToString(review.getUpdated()))
                        .movieId(review.getMovie().getId())
                        .score(review.getScore())
                        .title(review.getTitle())
                        .content(review.getContent())
                        .publicationDate(dateTimeConverter.formatLocalDateToString(review.getPublicationDate()))
                        .build();
            }
        };

        mapper.addConverter(reviewCreateDto2ReviewConverter);
        mapper.addConverter(reviewUpdateDto2ReviewConverter);
        mapper.addConverter(review2ReviewDtoConverter);
    }

    private void addUserConverters(final ModelMapper mapper) {
        Converter<UserCreateDto, User> user2UserCreateDtoConverter = new Converter<>() {
            @Override
            public User convert(final MappingContext<UserCreateDto, User> mappingContext) {
                final UserCreateDto dto = mappingContext.getSource();

                return User.builder()
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .birthday(dateTimeConverter.parseLocalDate(dto.getBirthday()))
                        .email(dto.getEmail())
                        .build();
            }
        };

        Converter<User, UserDto> user2UserDtoConverter = new Converter<>() {
            @Override
            public UserDto convert(final MappingContext<User, UserDto> mappingContext) {
                final User user = mappingContext.getSource();

                return UserDto.builder()
                        .id(user.getId())
                        .created(dateTimeConverter.formatLocalDateTimeToString(user.getCreated()))
                        .updated(dateTimeConverter.formatLocalDateTimeToString(user.getUpdated()))
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .birthday(dateTimeConverter.formatLocalDateToString(user.getBirthday()))
                        .email(user.getEmail())
                        .build();
            }
        };

        Converter<UserUpdateDto, User> userUpdateDto2UserConverter = new Converter<>() {
            @Override
            public User convert(final MappingContext<UserUpdateDto, User> mappingContext) {
                final UserUpdateDto dto = mappingContext.getSource();

                return User.builder()
                        .id(dto.getId())
                        .created(dateTimeConverter.parseLocalDateTime(dto.getCreated()))
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .birthday(dateTimeConverter.parseLocalDate(dto.getBirthday()))
                        .email(dto.getEmail())
                        .build();
            }
        };

        mapper.addConverter(user2UserCreateDtoConverter);
        mapper.addConverter(user2UserDtoConverter);
        mapper.addConverter(userUpdateDto2UserConverter);
    }
}


