package com.example.moview.moview.model;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record Review(
        Long id,
        Movie movie,
        Integer score,
        String title,
        String content,
        LocalDate publicationDate
) {
}