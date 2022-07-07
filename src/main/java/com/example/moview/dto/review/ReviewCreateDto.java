package com.example.moview.dto.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCreateDto {

    private Long movieId;
    private Integer score;
    private String title;
    private String content;
    private String publicationDate;
}
