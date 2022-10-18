package com.example.moview.mapper;

import com.example.moview.dto.movie.MovieCreateDto;
import com.example.moview.dto.movie.MovieDto;
import com.example.moview.dto.movie.MovieShortDto;
import com.example.moview.dto.movie.MovieUpdateDto;
import com.example.moview.dto.pagination.MovieReadPageDto;
import com.example.moview.model.Movie;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Mapper(uses = {GenreMapper.class}, componentModel = "spring")
public interface MovieMapper {

    String DATE_PATTERN = "dd.MM.yyyy";

    @Mapping(source = "entity.releaseDate", target = "releaseDate", dateFormat = DATE_PATTERN)
    @Mapping(source = "entity.duration", target = "duration", qualifiedByName = "durationToString")
    MovieDto entityToMovieDto(Movie entity, Integer imdbRating);

    @Mapping(source = "dto.releaseDate", target = "releaseDate", dateFormat = DATE_PATTERN)
    @Mapping(source = "dto.duration", target = "duration", qualifiedByName = "stringToDuration")
    @Mapping(source = "dto.genreIds", target = "genres")
    @Mapping(source = "userId", target = "user.id")
    Movie movieCreateDtoToEntity(MovieCreateDto dto, Long userId);

    @Mapping(source = "releaseDate", target = "releaseDate", dateFormat = DATE_PATTERN)
    @Mapping(source = "duration", target = "duration", qualifiedByName = "stringToDuration")
    @Mapping(source = "genreIds", target = "genres")
    Movie movieUpdateDtoToEntity(MovieUpdateDto dto);

    @Mapping(source = "releaseDate", target = "releaseDate", dateFormat = DATE_PATTERN)
    @Mapping(source = "duration", target = "duration", qualifiedByName = "durationToString")
    @Named("entityToMovieShortDto")
    MovieShortDto entityToMovieShortDto(Movie entity);

    @IterableMapping(qualifiedByName = "entityToMovieShortDto")
    @Named("entityListToMovieShortDtoList")
    List<MovieShortDto> entityListToMovieShortDtoList(List<Movie> entityList);

    Movie idToEntity(Long id);

    @Named("durationToString")
    default String durationToString(Duration duration) {
        return String.format("%02d:%02d:%02d", duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart());
    }

    @Named("stringToDuration")
    default Duration stringToDuration(String string) {
        return Duration.between(LocalTime.MIN, LocalTime.parse(string));
    }
}