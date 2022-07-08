package com.example.moview.moview.dto.review;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(builderClassName = "ReviewUpdateDtoBuilder")
@JsonDeserialize(builder = ReviewUpdateDto.ReviewUpdateDtoBuilder.class)
public class ReviewUpdateDto {

    private Long id;
    private Long movieId;
    private Integer score;
    private String title;
    private String content;
    private String publicationDate;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ReviewUpdateDtoBuilder {
    }
}