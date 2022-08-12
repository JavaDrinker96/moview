package com.example.moview.dto.pagination;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Jacksonized
@SuperBuilder
public class MovieReadPageDto extends PaginationDto {

    @NotNull
    private MovieProperties property;
}