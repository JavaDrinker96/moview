package com.example.moview.moview.dto.movie;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder(builderClassName = "MovieShortDtoBuilder")
@JsonDeserialize(builder = MovieShortDto.MovieShortDtoBuilder.class)
public class MovieShortDto {

    private Long id;
    private String title;
    private String description;
    private String releaseDate;
    private String duration;
    private Integer rating;
    private Set<Long> genreIds;

    @JsonPOJOBuilder(withPrefix = "")
    public static class MovieShortDtoBuilder {
    }
}