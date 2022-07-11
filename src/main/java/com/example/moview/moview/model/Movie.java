package com.example.moview.moview.model;

import lombok.Builder;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Builder
public record Movie(
        Long id,
        String title,
        String description,
        LocalDate releaseDate,
        Duration duration,
        Integer rating,
        List<Review> reviews
) {
}
