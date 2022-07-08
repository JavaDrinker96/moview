package com.example.moview.moview.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
public class Review {

    private Long id;
    private Movie movie;
    private Integer score;
    private String title;
    private String content;
    private LocalDate publicationDate;
}