package com.example.moview.moview.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class OmdbMovie {

    private Integer rating;
    private String description;
}
