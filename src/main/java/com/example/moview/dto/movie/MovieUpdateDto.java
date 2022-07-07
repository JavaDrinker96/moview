package com.example.moview.dto.movie;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieUpdateDto {

    private Long id;
    private String title;
    private String description;
    private String releaseDate;
    private String duration;
}
