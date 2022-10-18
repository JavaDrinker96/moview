package com.example.moview.dto.pagination;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ReviewReadPageDto extends PaginationDto{

    @NotNull
    private ReviewProperties property;

    @NotNull
    private Long movieId;

}
