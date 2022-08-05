package com.example.moview.moview.dto.review;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;


@Getter
@Setter
@Jacksonized
public class ReviewUpdateDto {

    @NotNull
    @Min(1)
    private Long id;

    @Min(1)
    @NotNull
    private Long movieId;

    @NotNull
    @Min(1)
    @Max(100)
    private Integer score;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @Null
    private Long authorId;
}