package com.example.moview.dto.movie;

import com.example.moview.dto.genre.GenreDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.Set;

@Getter
@Setter
@Builder
@Jacksonized
public class MovieShortDto {

    private Long id;
    private String title;
    private String description;
    private String releaseDate;
    private String duration;
    private Integer rating;
    private Set<GenreDto> genres;
}