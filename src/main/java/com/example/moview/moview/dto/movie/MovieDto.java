package com.example.moview.moview.dto.movie;

import com.example.moview.moview.dto.review.ReviewDto;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder(builderClassName = "MovieDtoBuilder")
@JsonDeserialize(builder = MovieDto.MovieDtoBuilder.class)
public class MovieDto {

    private Long id;
    private String created;
    private String updated;
    private String title;
    private String description;
    private String releaseDate;
    private String duration;
    private Integer rating;
    private List<ReviewDto> reviews;

    @JsonPOJOBuilder(withPrefix = "")
    public static class MovieDtoBuilder {
    }
}
