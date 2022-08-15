package com.example.moview.moview.mapper;

import com.example.moview.moview.dto.rating.RatingDto;
import com.example.moview.moview.dto.rating.RatingStatus;
import org.mapstruct.Mapper;

import java.util.Objects;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface RatingMapper {

    default RatingDto ratingToDto(Optional<Integer> rating) {
        return RatingDto.builder()
                .value(rating.isPresent() ? rating.get() : null)
                .status(rating.isPresent() ? RatingStatus.PRESENT : RatingStatus.ABSENT)
                .build();
    }

    default RatingDto ratingToDto(Integer rating) {
        return RatingDto.builder()
                .value(rating)
                .status(Objects.nonNull(rating) ? RatingStatus.PRESENT : RatingStatus.ABSENT)
                .build();
    }
}
