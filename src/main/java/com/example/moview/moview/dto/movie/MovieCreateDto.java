package com.example.moview.moview.dto.movie;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder(builderClassName = "MovieCreateDtoBuilder")
@JsonDeserialize(builder = MovieCreateDto.MovieCreateDtoBuilder.class)
public class MovieCreateDto {

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{4}$")
    private String releaseDate;

    @NotNull
    @Pattern(regexp = "^\\d{2}:\\d{2}:\\d{2}$")
    private String duration;

    @JsonPOJOBuilder(withPrefix = "")
    public static class MovieCreateDtoBuilder {
    }
}
