package com.example.moview.moview.dto.review;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Builder
@Jacksonized
public class ReviewDto {

    private Long id;
    private Long movieId;
    private Integer score;
    private String title;
    private String content;
    private String publicationDate;
}