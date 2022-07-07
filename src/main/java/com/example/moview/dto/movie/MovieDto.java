package com.example.moview.dto.movie;

import com.example.moview.dto.review.ReviewDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class MovieDto {

    private Long id;
    private String title;
    private String description;
    private String releaseDate;
    private String duration;
    private Integer rating;
    private List<ReviewDto> reviews;
}
