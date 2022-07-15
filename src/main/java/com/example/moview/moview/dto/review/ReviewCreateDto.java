package com.example.moview.moview.dto.review;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder(builderClassName = "ReviewCreateDtoBuilder")
@JsonDeserialize(builder = ReviewCreateDto.ReviewCreateDtoBuilder.class)
public class ReviewCreateDto {

    @NotNull
    private Long movieId;

    @NotNull
    private Integer score;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{4}$")
    private String publicationDate;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ReviewCreateDtoBuilder {
    }
}
