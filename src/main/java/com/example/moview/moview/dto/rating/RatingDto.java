package com.example.moview.moview.dto.rating;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Builder
@Jacksonized
public class RatingDto {

    private Integer value;
    private RatingStatus status;
}