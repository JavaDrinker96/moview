package com.example.moview.moview.dto.review;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder(builderClassName = "ReviewUpdateDtoBuilder")
@JsonDeserialize(builder = ReviewUpdateDto.ReviewUpdateDtoBuilder.class)
public class ReviewUpdateDto {

    @Min(1)
    @NotNull
    private Long id;

    @NotNull
    @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2}:\\d{2}$")
    private String created;

    @Min(1)
    @NotNull
    private Long movieId;

    @NotNull
    @Size(min = 1, max = 100)
    private Integer score;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{4}$")
    private String publicationDate;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ReviewUpdateDtoBuilder {
    }
}