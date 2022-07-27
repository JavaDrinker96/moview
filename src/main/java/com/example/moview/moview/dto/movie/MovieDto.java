package com.example.moview.moview.dto.movie;

import com.example.moview.moview.dto.genre.GenreDto;
import com.example.moview.moview.dto.review.ReviewDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.Set;

@Getter
@Setter
@Builder
@Jacksonized
public class MovieDto {

    private Long id;
    private String title;
    private String description;
    private String releaseDate;
    private String duration;
    private Integer rating;
    private Set<ReviewDto> reviews;
    private Set<GenreDto> genres;
}