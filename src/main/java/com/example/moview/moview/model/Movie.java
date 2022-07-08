package com.example.moview.moview.model;

import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class Movie {

    private Long id;
    private String title;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;
    private Integer rating;
    private List<Review> reviews;
}
