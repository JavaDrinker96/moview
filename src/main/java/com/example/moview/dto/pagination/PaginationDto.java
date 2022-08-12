package com.example.moview.dto.pagination;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@SuperBuilder
public abstract class PaginationDto {

    @NotNull
    @Min(0)
    protected Integer page;

    @NotNull
    @Min(1)
    protected Integer size;

    @NotNull
    protected Sort.Direction direction;
}