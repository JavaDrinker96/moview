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
import com.example.moview.moview.util.datetime.DateTimeUtil;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.moview.moview.util.datetime.DateTimeUtil.parseDate;
import static com.example.moview.moview.util.datetime.DateTimeUtil.parseDuration;

public class ModelMapperConfigurer {

    public static void movieConfigure(final ModelMapper modelMapper) {
        modelMapper.addConverter(createMovieCreateDtoToMovieConverter());
        modelMapper.addConverter(createMovieToMovieDtoConverter());
        modelMapper.addConverter(createMovieUpdateDtoToMovie());
        modelMapper.addConverter(createMovieToMovieShortDtoConverter());

    }

    public static void reviewConfigure(final ModelMapper modelMapper) {
        modelMapper.addConverter(createReviewCreateDtoToReviewConverter());
        modelMapper.addConverter(createReviewUpdateDtoToReviewConverter());
    }

    private static Converter<Movie, MovieDto> createMovieToMovieDtoConverter() {
        return new Converter<>() {
            @Override
            public MovieDto convert(final MappingContext<Movie, MovieDto> mappingContext) {
                final Movie movie = mappingContext.getSource();
                final List<ReviewDto> reviewDtoList = movie.reviews().stream()
                        .map(review -> ReviewDto.builder()
                                .id(review.id())
                                .score(review.score())
                                .title(review.title())
                                .content(review.content())
                                .publicationDate(DateTimeUtil.formatLocalDateToString(review.publicationDate()))
                                .build())
                        .collect(Collectors.toList());

                return MovieDto.builder()
                        .id(movie.id())
                        .title(movie.title())
                        .description(movie.description())
                        .releaseDate(DateTimeUtil.formatLocalDateToString(movie.releaseDate()))
                        .duration(DateTimeUtil.formatDurationToString(movie.duration()))
                        .rating(movie.rating())
                        .reviews(reviewDtoList)
                        .build();
            }
        };
    }

    private static Converter<MovieCreateDto, Movie> createMovieCreateDtoToMovieConverter() {
        return new Converter<>() {
            @Override
            public Movie convert(MappingContext<MovieCreateDto, Movie> mappingContext) {
                final MovieCreateDto dto = mappingContext.getSource();

                return Movie.builder()
                        .title(dto.getTitle())
                        .description(dto.getDescription())
                        .releaseDate(parseDate(dto.getReleaseDate()))
                        .duration(parseDuration(dto.getDuration()))
                        .build();
            }
        };
    }

    private static Converter<MovieUpdateDto, Movie> createMovieUpdateDtoToMovie() {
        return new Converter<>() {
            @Override
            public Movie convert(final MappingContext<MovieUpdateDto, Movie> mappingContext) {
                final MovieUpdateDto movieDto = mappingContext.getSource();

                return Movie.builder()
                        .id(movieDto.getId())
                        .title(movieDto.getTitle())
                        .description(movieDto.getTitle())
                        .releaseDate(DateTimeUtil.parseDate(movieDto.getReleaseDate()))
                        .duration(DateTimeUtil.parseDuration(movieDto.getDuration()))
                        .build();
            }
        };
    }

    private static Converter<Movie, MovieShortDto> createMovieToMovieShortDtoConverter() {
        return new Converter<>() {
            @Override
            public MovieShortDto convert(final MappingContext<Movie, MovieShortDto> mappingContext) {
                final Movie movie = mappingContext.getSource();

                return MovieShortDto.builder()
                        .id(movie.id())
                        .title(movie.title())
                        .description(movie.description())
                        .releaseDate(DateTimeUtil.formatLocalDateToString(movie.releaseDate()))
                        .duration(DateTimeUtil.formatDurationToString(movie.duration()))
                        .rating(movie.rating())
                        .build();
            }
        };
    }

    private static Converter<ReviewCreateDto, Review> createReviewCreateDtoToReviewConverter() {
        return new Converter<>() {
            @Override
            public Review convert(final MappingContext<ReviewCreateDto, Review> mappingContext) {
                final ReviewCreateDto dto = mappingContext.getSource();

                return Review.builder()
                        .movie(Movie.builder().id(dto.getMovieId()).build())
                        .score(dto.getScore())
                        .title(dto.getTitle())
                        .content(dto.getContent())
                        .publicationDate(DateTimeUtil.parseDate(dto.getPublicationDate()))
                        .build();
            }
        };
    }

    private static Converter<ReviewUpdateDto, Review> createReviewUpdateDtoToReviewConverter() {
        return new Converter<>() {
            @Override
            public Review convert(final MappingContext<ReviewUpdateDto, Review> mappingContext) {
                final ReviewUpdateDto dto = mappingContext.getSource();

                return Review.builder()
                        .id(dto.getId())
                        .movie(Movie.builder().id(dto.getMovieId()).build())
                        .score(dto.getScore())
                        .title(dto.getTitle())
                        .content(dto.getContent())
                        .publicationDate(DateTimeUtil.parseDate(dto.getPublicationDate()))
                        .build();
            }
        };
    }
}


