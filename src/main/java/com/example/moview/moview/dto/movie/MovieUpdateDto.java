package com.example.moview.moview.dto.movie;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder(builderClassName = "MovieUpdateDtoBuilder")
@JsonDeserialize(builder = MovieUpdateDto.MovieUpdateDtoBuilder.class)
public class MovieUpdateDto {

    @Min(1)
    @NotNull
    private Long id;

    @NotNull
    @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2}:\\d{2}$")
    private String created;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{4}$")
    private String releaseDate;

    @NotNull
    @Pattern(regexp = "^\\d{2}:\\d{2}:\\d{2}$")
    private String duration;

    @JsonPOJOBuilder(withPrefix = "")
    public static class MovieUpdateDtoBuilder {
    }
}
