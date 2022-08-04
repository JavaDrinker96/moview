package com.example.moview.moview.mapper;

import com.example.moview.moview.dto.review.ReviewCreateDto;
import com.example.moview.moview.dto.review.ReviewDto;
import com.example.moview.moview.dto.review.ReviewUpdateDto;
import com.example.moview.moview.model.Movie;
import com.example.moview.moview.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    String DATE_PATTERN = "dd.MM.yyyy";

    @Mapping(source = "movieId", target = "movie", qualifiedByName = "movieIdToMovie")
    Review createDtoToModel(ReviewCreateDto dto);

    @Mapping(source = "movieId", target = "movie", qualifiedByName = "movieIdToMovie")
    Review updateDtoToModel(ReviewUpdateDto dto);

    @Mapping(source = "publicationDate", target = "publicationDate", dateFormat = DATE_PATTERN)
    @Mapping(source = "movie", target = "movieId", qualifiedByName = "movieToMovieId")
    ReviewDto modelToDto(Review model);

    @Mapping(source = "publicationDate", target = "publicationDate", dateFormat = DATE_PATTERN)
    List<ReviewDto> modelListToDtoList(List<Review> modelList);

    @Mapping(source = "publicationDate", target = "publicationDate", dateFormat = DATE_PATTERN)
    Set<ReviewDto> modelSetToDtoSet(Set<Review> modelSet);

    @Named("movieToMovieId")
    default Long movieToMovieId(Movie movie) {
        return movie.getId();
    }

    @Named("movieIdToMovie")
    default Movie movieToMovieId(Long movieId) {
        return Movie.builder().id(movieId).build();
    }
}