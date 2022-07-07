package com.example.moview.dto.review;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private Long id;
    private Integer score;
    private String title;
    private String content;
    private String publicationDate;
}
