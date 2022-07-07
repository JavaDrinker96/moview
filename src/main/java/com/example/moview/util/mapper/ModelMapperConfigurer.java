package com.example.moview.util.mapper;

import com.example.moview.dto.movie.MovieCreateDto;
import com.example.moview.dto.movie.MovieDto;
import com.example.moview.dto.movie.MovieShortDto;
import com.example.moview.dto.movie.MovieUpdateDto;
import com.example.moview.dto.review.ReviewCreateDto;
import com.example.moview.dto.review.ReviewDto;
import com.example.moview.dto.review.ReviewUpdateDto;
import com.example.moview.model.Movie;
import com.example.moview.model.Review;
import com.example.moview.util.datetime.DateTimeUtil;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import java.util.ArrayList;
import java.util.List;

import static com.example.moview.util.datetime.DateTimeUtil.parseDate;
import static com.example.moview.util.datetime.DateTimeUtil.parseDuration;

public class ModelMapperConfigurer {

    public static void movieConfigure(final ModelMapper modelMapper) {
        Converter<MovieCreateDto, Movie> movieCreateDtoToMovieConverter = new Converter<MovieCreateDto, Movie>() {
            public Movie convert(final MappingContext<MovieCreateDto, Movie> context) {
                final MovieCreateDto dto = context.getSource();

                return Movie.builder()
                        .title(dto.getTitle())
                        .description(dto.getDescription())
                        .releaseDate(parseDate(dto.getReleaseDate()))
                        .duration(parseDuration(dto.getDuration()))
                        .build();
            }
        };

        modelMapper.addConverter(movieCreateDtoToMovieConverter);

        Converter<Movie, MovieDto> movieToMovieDto = new Converter<Movie, MovieDto>() {
            @Override
            public MovieDto convert(final MappingContext<Movie, MovieDto> mappingContext) {
                final Movie movie = mappingContext.getSource();
                final List<ReviewDto> reviewDtoList = new ArrayList<>();

                for (final Review review : movie.getReviews()) {
                    reviewDtoList.add(ReviewDto.builder()
                            .id(review.getId())
                            .score(review.getScore())
                            .title(review.getTitle())
                            .content(review.getContent())
                            .publicationDate(DateTimeUtil.formatLocalDateToString(review.getPublicationDate()))
                            .build());
                }

                return MovieDto.builder()
                        .id(movie.getId())
                        .title(movie.getTitle())
                        .description(movie.getDescription())
                        .releaseDate(DateTimeUtil.formatLocalDateToString(movie.getReleaseDate()))
                        .duration(DateTimeUtil.formatDurationToString(movie.getDuration()))
                        .rating(movie.getRating())
                        .reviews(reviewDtoList)
                        .build();
            }
        };

        modelMapper.addConverter(movieToMovieDto);

        Converter<MovieUpdateDto, Movie> movieDtoToMovie = new Converter<MovieUpdateDto, Movie>() {
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

        modelMapper.addConverter(movieDtoToMovie);

        Converter<Movie, MovieShortDto> movieShortDtoToMovie = new Converter<Movie, MovieShortDto>() {
            @Override
            public MovieShortDto convert(final MappingContext<Movie, MovieShortDto> mappingContext) {
                final Movie movie = mappingContext.getSource();

                return MovieShortDto.builder()
                        .id(movie.getId())
                        .title(movie.getTitle())
                        .description(movie.getDescription())
                        .releaseDate(DateTimeUtil.formatLocalDateToString(movie.getReleaseDate()))
                        .duration(DateTimeUtil.formatDurationToString(movie.getDuration()))
                        .rating(movie.getRating())
                        .build();
            }
        };

        modelMapper.addConverter(movieShortDtoToMovie);
    }

    public static void reviewConfigure(final ModelMapper modelMapper) {
        Converter<ReviewCreateDto, Review> reviewCreateDtoToReview = new Converter<ReviewCreateDto, Review>() {
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

        modelMapper.addConverter(reviewCreateDtoToReview);

        Converter<ReviewUpdateDto, Review> reviewUpdateDtoReview = new Converter<ReviewUpdateDto, Review>() {
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

        modelMapper.addConverter(reviewUpdateDtoReview);
    }
}


