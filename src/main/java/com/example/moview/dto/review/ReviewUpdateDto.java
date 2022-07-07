package com.example.moview.dto.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewUpdateDto {

    private Long id;
    private Long movieId;
    private Integer score;
    private String title;
    private String content;
    private String publicationDate;
}