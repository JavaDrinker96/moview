package com.example.moview.dto.review;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "of")
public class ReviewPageResponse {

    private final long totalElementsCount;
    private final List<ReviewDto> reviews;
}
