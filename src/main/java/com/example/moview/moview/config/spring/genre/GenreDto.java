package com.example.moview.moview.config.spring.genre;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(builderClassName = "GenreDtoBuilder")
@JsonDeserialize(builder = GenreDto.GenreDtoBuilder.class)
public class GenreDto {

    private Long id;
    private String created;
    private String updated;
    private String name;

    @JsonPOJOBuilder(withPrefix = "")
    public static class GenreDtoBuilder {
    }
}
