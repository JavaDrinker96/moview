package com.example.moview.moview.dto.movie;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

@Getter
@Setter
@Builder(builderClassName = "MovieCreateDtoBuilder")
@JsonDeserialize(builder = MovieCreateDto.MovieCreateDtoBuilder.class)
public class MovieCreateDto {

    private String title;
    private String description;
    private String releaseDate;
    private String duration;

    @JsonPOJOBuilder(withPrefix = "")
    public static class MovieCreateDtoBuilder {
    }
}
