package com.example.moview.moview.dto.movie;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(builderClassName = "MovieShortDtoBuilder")
@JsonDeserialize(builder = MovieShortDto.MovieShortDtoBuilder.class)
public class MovieShortDto {

    private Long id;
    private String created;
    private String updated;
    private String title;
    private String description;
    private String releaseDate;
    private String duration;
    private Integer rating;

    @JsonPOJOBuilder(withPrefix = "")
    public static class MovieShortDtoBuilder {
    }
}
