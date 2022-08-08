package com.example.moview.moview.dto.genre;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Builder
@Jacksonized
public class GenreDto {

    private Long id;
    private String name;
}