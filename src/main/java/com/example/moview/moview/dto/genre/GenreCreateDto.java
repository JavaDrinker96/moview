package com.example.moview.moview.dto.genre;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@Jacksonized
public class GenreCreateDto {

    @NotBlank
    private String name;
}