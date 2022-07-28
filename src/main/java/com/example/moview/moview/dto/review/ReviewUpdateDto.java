package com.example.moview.moview.dto.review;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Getter
@Setter
@Builder(builderClassName = "ReviewUpdateDtoBuilder")
@JsonDeserialize(builder = ReviewUpdateDto.ReviewUpdateDtoBuilder.class)
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

    @NotNull
    @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{4}$")
    private String publicationDate;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ReviewUpdateDtoBuilder {
    }
}