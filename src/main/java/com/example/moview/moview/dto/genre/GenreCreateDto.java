package com.example.moview.moview.dto.genre;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder(builderClassName = "GenreCreateDtoBuilder")
@JsonDeserialize(builder = GenreCreateDto.GenreCreateDtoBuilder.class)
public class GenreCreateDto {

    @NotBlank
    private String name;

    @JsonPOJOBuilder(withPrefix = "")
    public static class GenreCreateDtoBuilder{
    }
}
