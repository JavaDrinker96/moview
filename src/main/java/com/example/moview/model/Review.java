package com.example.moview.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class Review {

    private Long id;
    private Movie movie;
    private Integer score;
    private String title;
    private String content;
    private LocalDate publicationDate;
}