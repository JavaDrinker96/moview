package com.example.moview.moview.dto.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MovieProperties {

    TITLE("title"),
    RATING("rating"),
    RELEASE_DATE("releaseDate");

    final String name;
}