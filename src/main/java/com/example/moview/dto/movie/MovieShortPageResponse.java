package com.example.moview.dto.movie;

import com.example.moview.dto.movie.MovieShortDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "of")
public class MovieShortPageResponse {

    private final int pagesCount;
    private final List<MovieShortDto> movies;
}
