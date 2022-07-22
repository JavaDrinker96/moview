package com.example.moview.moview.config.mode_mapper;

import com.example.moview.moview.dto.genre.GenreCreateDto;
import com.example.moview.moview.dto.genre.GenreDto;
import com.example.moview.moview.dto.genre.GenreUpdateDto;
import com.example.moview.moview.dto.movie.MovieCreateDto;
import com.example.moview.moview.dto.movie.MovieDto;
import com.example.moview.moview.dto.movie.MovieReadPageDto;
import com.example.moview.moview.dto.movie.MovieShortDto;
import com.example.moview.moview.dto.movie.MovieUpdateDto;
import com.example.moview.moview.dto.review.ReviewCreateDto;
import com.example.moview.moview.dto.review.ReviewDto;
import com.example.moview.moview.dto.review.ReviewUpdateDto;
import com.example.moview.moview.dto.user.UserCreateDto;
import com.example.moview.moview.dto.user.UserDto;
import com.example.moview.moview.dto.user.UserUpdateDto;
import com.example.moview.moview.model.Genre;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
        addGenreConverters(mapper);

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
                                .movieId(movie.getId())
                                .score(review.getScore())
                                .title(review.getTitle())
                                .content(review.getContent())
                                .publicationDate(dateTimeConverter.formatLocalDateToString(review.getPublicationDate()))
                                .build())
                        .collect(Collectors.toList());

                final Set<GenreDto> genreDtoSet = Objects.isNull(movie.getGenres())
                        ? new HashSet<>()
                        : movie.getGenres().stream()
                        .map(genre -> GenreDto.builder()
                                .id(genre.getId())
                                .name(genre.getName())
                                .build())
                        .collect(Collectors.toSet());

                return MovieDto.builder()
                        .id(movie.getId())
                        .title(movie.getTitle())
                        .description(movie.getDescription())
                        .releaseDate(dateTimeConverter.formatLocalDateToString(movie.getReleaseDate()))
                        .duration(dateTimeConverter.formatDurationToString(movie.getDuration()))
                        .rating(movie.getRating())
                        .reviews(reviewDtoList)
                        .genres(genreDtoSet)
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
                        .genres(dto.getGenreIds().stream()
                                .map(id -> Genre.builder().id(id).build())
                                .collect(Collectors.toSet()))
                        .build();
            }
        };

        Converter<MovieUpdateDto, Movie> movieUpdateDto2MovieConverter = new Converter<>() {
            @Override
            public Movie convert(final MappingContext<MovieUpdateDto, Movie> mappingContext) {
                final MovieUpdateDto movieDto = mappingContext.getSource();

                return Movie.builder()
                        .id(movieDto.getId())
                        .title(movieDto.getTitle())
                        .description(movieDto.getTitle())
                        .releaseDate(dateTimeConverter.parseLocalDate(movieDto.getReleaseDate()))
                        .duration(dateTimeConverter.parseDuration(movieDto.getDuration()))
                        .genres(movieDto.getGenreIds().stream()
                                .map(id -> Genre.builder().id(id).build())
                                .collect(Collectors.toSet()))
                        .build();
            }
        };

        Converter<Movie, MovieShortDto> movie2MovieShortDtoConverter = new Converter<>() {
            @Override
            public MovieShortDto convert(final MappingContext<Movie, MovieShortDto> mappingContext) {
                final Movie movie = mappingContext.getSource();

                return MovieShortDto.builder()
                        .id(movie.getId())
                        .title(movie.getTitle())
                        .description(movie.getDescription())
                        .releaseDate(dateTimeConverter.formatLocalDateToString(movie.getReleaseDate()))
                        .duration(dateTimeConverter.formatDurationToString(movie.getDuration()))
                        .rating(movie.getRating())
                        .genres(movie.getGenres().stream()
                                .map(genre -> GenreDto.builder()
                                        .id(genre.getId())
                                        .name(genre.getName()).build())
                                .collect(Collectors.toSet()))
                        .build();
            }
        };

        Converter<MovieReadPageDto, PageRequest> movieReadPageDto2PageRequestConverter = new Converter<>() {
            @Override
            public PageRequest convert(final MappingContext<MovieReadPageDto, PageRequest> mappingContext) {
                final MovieReadPageDto dto = mappingContext.getSource();

                return PageRequest.of(dto.getPage(), dto.getSize(),
                        Sort.by(dto.getDirection(), dto.getProperty().getPropertyName())
                );
            }
        };

        mapper.addConverter(movie2MovieDtoConverter);
        mapper.addConverter(movieCreateDto2MovieConverter);
        mapper.addConverter(movieUpdateDto2MovieConverter);
        mapper.addConverter(movie2MovieShortDtoConverter);
        mapper.addConverter(movieReadPageDto2PageRequestConverter);
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
                        .build();
            }
        };

        Converter<ReviewUpdateDto, Review> reviewUpdateDto2ReviewConverter = new Converter<>() {
            @Override
            public Review convert(final MappingContext<ReviewUpdateDto, Review> mappingContext) {
                final ReviewUpdateDto dto = mappingContext.getSource();

                return Review.builder()
                        .id(dto.getId())
                        .movie(Movie.builder().id(dto.getMovieId()).build())
                        .score(dto.getScore())
                        .title(dto.getTitle())
                        .content(dto.getContent())
                        .build();
            }
        };

        Converter<Review, ReviewDto> review2ReviewDtoConverter = new Converter<>() {
            @Override
            public ReviewDto convert(final MappingContext<Review, ReviewDto> mappingContext) {
                final Review review = mappingContext.getSource();

                return ReviewDto.builder()
                        .id(review.getId())
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

    private void addGenreConverters(final ModelMapper mapper) {
        Converter<GenreCreateDto, Genre> genreCreateDto2GenreConverter = new Converter<>() {
            @Override
            public Genre convert(final MappingContext<GenreCreateDto, Genre> mappingContext) {
                final GenreCreateDto dto = mappingContext.getSource();

                return Genre.builder()
                        .name(dto.getName())
                        .build();
            }
        };

        Converter<Genre, GenreDto> genre2GenreDtoConverter = new Converter<>() {
            @Override
            public GenreDto convert(final MappingContext<Genre, GenreDto> mappingContext) {
                final Genre genre = mappingContext.getSource();

                return GenreDto.builder()
                        .id(genre.getId())
                        .name(genre.getName())
                        .build();
            }
        };

        Converter<GenreUpdateDto, Genre> genreUpdateDto2GenreConverter = new Converter<>() {
            @Override
            public Genre convert(final MappingContext<GenreUpdateDto, Genre> mappingContext) {
                final GenreUpdateDto dto = mappingContext.getSource();

                return Genre.builder()
                        .id(dto.getId())
                        .name(dto.getName())
                        .build();
            }
        };

        mapper.addConverter(genreCreateDto2GenreConverter);
        mapper.addConverter(genre2GenreDtoConverter);
        mapper.addConverter(genreUpdateDto2GenreConverter);
    }
}