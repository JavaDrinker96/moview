package com.example.moview.moview.dto.movie;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

@Getter
@Setter
@Builder(builderClassName = "MovieUpdateDtoBuilder")
@JsonDeserialize(builder = MovieUpdateDto.MovieUpdateDtoBuilder.class)
public class MovieUpdateDto {

    private Long id;
    private String title;
    private String description;
    private String releaseDate;
    private String duration;

    @JsonPOJOBuilder(withPrefix = "")
    public static class MovieUpdateDtoBuilder {
    }
}
