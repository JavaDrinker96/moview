package com.example.moview.moview.dto.movie;

import com.example.moview.moview.dto.genre.GenreDto;
import com.example.moview.moview.dto.rating.RatingDto;
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
    private RatingDto rating;
    private Set<GenreDto> genres;
}