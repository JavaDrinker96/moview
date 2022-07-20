package com.example.moview.moview.dto.movie;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder(builderClassName = "MovieReadPageDtoBuilder")
@JsonDeserialize(builder = MovieReadPageDto.MovieReadPageDtoBuilder.class)
public class MovieReadPageDto {

    @NotNull
    @Min(0)
    private Integer page;

    @NotNull
    @Min(1)
    private Integer size;

    @NotNull
    private Sort.Direction direction;

    @NotNull
    private SortedProperty property;

    @Getter
    @AllArgsConstructor
    public enum SortedProperty {
        NAME("title"),
        RATING("rating"),
        RELEASE_DATE("releaseDate");

        final String propertyName;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class MovieReadPageDtoBuilder {
    }
}
