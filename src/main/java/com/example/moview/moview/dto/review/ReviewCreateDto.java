package com.example.moview.moview.dto.review;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@Builder(builderClassName = "ReviewCreateDtoBuilder")
@JsonDeserialize(builder = ReviewCreateDto.ReviewCreateDtoBuilder.class)
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

    @NotNull
    @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{4}$")
    private String publicationDate;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ReviewCreateDtoBuilder {
    }
}
