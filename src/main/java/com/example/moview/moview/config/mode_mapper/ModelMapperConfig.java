package com.example.moview.moview.config.mode_mapper;

import com.example.moview.moview.dto.movie.MovieCreateDto;
import com.example.moview.moview.dto.movie.MovieDto;
import com.example.moview.moview.dto.movie.MovieShortDto;
import com.example.moview.moview.dto.movie.MovieUpdateDto;
import com.example.moview.moview.dto.review.ReviewCreateDto;
import com.example.moview.moview.dto.review.ReviewDto;
import com.example.moview.moview.dto.review.ReviewUpdateDto;
import com.example.moview.moview.model.Movie;
import com.example.moview.moview.model.Review;
import com.example.moview.moview.util.datetime.DateTimeConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        return mapper;
    }

    private void addMovieConverters(final ModelMapper mapper) {
        Converter<Movie, MovieDto> movieToMovieDtoConverter = new Converter<>() {
            @Override
            public MovieDto convert(final MappingContext<Movie, MovieDto> mappingContext) {
                final Movie movie = mappingContext.getSource();

                final List<ReviewDto> reviewDtoList = Optional.ofNullable(movie.getReviews()).orElse(new ArrayList<>())
                        .stream().map(review -> ReviewDto.builder()
                                .id(review.getId())
                                .created(dateTimeConverter.formatLocalDateTimeToString(review.getCreated()))
                                .updated(dateTimeConverter.formatLocalDateTimeToString(review.getUpdated()))
                                .movieId(movie.getId())
                                .score(review.getScore())
                                .title(review.getTitle())
                                .content(review.getContent())
                                .publicationDate(dateTimeConverter.formatLocalDateToString(review.getPublicationDate()))
                                .build())
                        .toList();

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

        Converter<MovieCreateDto, Movie> movieCreateDtoToMovieConverter = new Converter<>() {
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

        Converter<MovieUpdateDto, Movie> movieUpdateDtoToMovieConverter = new Converter<>() {
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

        Converter<Movie, MovieShortDto> movieToMovieShortDtoConverter = new Converter<>() {
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

        mapper.addConverter(movieToMovieDtoConverter);
        mapper.addConverter(movieCreateDtoToMovieConverter);
        mapper.addConverter(movieUpdateDtoToMovieConverter);
        mapper.addConverter(movieToMovieShortDtoConverter);
    }

    private void addReviewConverters(final ModelMapper mapper) {
        Converter<ReviewCreateDto, Review> reviewCreateDtoToReviewConverter = new Converter<>() {
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

        Converter<ReviewUpdateDto, Review> reviewUpdateDtoToReviewConverter = new Converter<>() {
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

        Converter<Review, ReviewDto> reviewToReviewDtoConverter = new Converter<>() {
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

        mapper.addConverter(reviewCreateDtoToReviewConverter);
        mapper.addConverter(reviewUpdateDtoToReviewConverter);
        mapper.addConverter(reviewToReviewDtoConverter);
    }
}


