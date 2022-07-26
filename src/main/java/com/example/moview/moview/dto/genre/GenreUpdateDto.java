package com.example.moview.moview.dto.genre;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@Jacksonized
public class GenreUpdateDto {

    @NotNull
    @Min(1)
    private Long id;

    @NotBlank
    private String name;
}