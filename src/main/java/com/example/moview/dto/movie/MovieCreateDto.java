package com.example.moview.dto.movie;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Getter
@Setter
@Builder
@Jacksonized
public class MovieCreateDto {

    @NotBlank
    private String title;

    private String description;

    @NotNull
    @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{4}$")
    private String releaseDate;

    @NotNull
    @Pattern(regexp = "^\\d{2}:\\d{2}:\\d{2}$")
    private String duration;

    @NotNull
    private Set<Long> genreIds;
}