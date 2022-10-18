package com.example.moview.mapper;

import com.example.moview.dto.review.ReviewCreateDto;
import com.example.moview.dto.review.ReviewDto;
import com.example.moview.dto.review.ReviewUpdateDto;
import com.example.moview.model.Review;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(uses = {MovieMapper.class}, componentModel = "spring")
public interface ReviewMapper {

    String DATE_PATTERN = "dd.MM.yyyy";

    @Mapping(source = "dto.movieId", target = "movie")
    @Mapping(source = "authorId", target = "author.id")
    Review reviewCreateDtoToEntity(ReviewCreateDto dto, Long authorId);

    @Mapping(source = "dto.movieId", target = "movie")
    @Mapping(source = "authorId", target = "author.id")
    Review reviewUpdateDtoToEntity(ReviewUpdateDto dto, Long authorId);

    @Mapping(source = "publicationDate", target = "publicationDate", dateFormat = DATE_PATTERN)
    @Mapping(source = "movie.id", target = "movieId")
    @Named("entityToReviewDto")
    ReviewDto entityToReviewDto(Review model);

    @Mapping(source = "publicationDate", target = "publicationDate", dateFormat = DATE_PATTERN)
    Set<ReviewDto> entitySetToReviewDtoSet(Set<Review> entitySet);

    @IterableMapping(qualifiedByName = "entityToReviewDto")
    List<ReviewDto> entityListToDtoList(List<Review> entityList);
}