package com.example.moview.dto.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReviewProperties {

    TITLE("titile"),
    DATE("publicationDate"),
    SCORE("score");

    final String name;
}