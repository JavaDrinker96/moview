package com.example.moview.moview.dto.genre;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder(builderClassName = "GenreUpdateDtoBuilder")
@JsonDeserialize(builder = GenreUpdateDto.GenreUpdateDtoBuilder.class)
public class GenreUpdateDto {

    @NotNull
    @Min(1)
    private Long id;

    @NotNull
    @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2}:\\d{2}$")
    private String created;

    @NotBlank
    private String name;

    @JsonPOJOBuilder(withPrefix = "")
    public static class GenreUpdateDtoBuilder{
    }
}
