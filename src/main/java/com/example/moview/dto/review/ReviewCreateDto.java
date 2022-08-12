package com.example.moview.dto.review;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@Builder
@Jacksonized
public class ReviewCreateDto {

    @NotNull
    @Min(1)
    private Long movieId;

    @NotNull
    @Min(1)
    @Max(100)
    private Integer score;

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}