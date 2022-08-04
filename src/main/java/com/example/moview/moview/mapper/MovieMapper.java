package com.example.moview.moview.mapper;

import com.example.moview.moview.dto.movie.MovieCreateDto;
import com.example.moview.moview.dto.movie.MovieDto;
import com.example.moview.moview.dto.movie.MovieShortDto;
import com.example.moview.moview.dto.movie.MovieUpdateDto;
import com.example.moview.moview.dto.pagination.MovieReadPageDto;
import com.example.moview.moview.model.Genre;
import com.example.moview.moview.model.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(uses = {ReviewMapper.class, GenreMapper.class}, componentModel = "spring")
public interface MovieMapper {

    String DATE_PATTERN = "dd.MM.yyyy";

    @Mapping(source = "releaseDate", target = "releaseDate", dateFormat = DATE_PATTERN)
    @Mapping(source = "duration", target = "duration", qualifiedByName = "durationToString")
    MovieDto modelToDto(Movie model);

    @Mapping(source = "releaseDate", target = "releaseDate", dateFormat = DATE_PATTERN)
    @Mapping(source = "duration", target = "duration", qualifiedByName = "stringToDuration")
    @Mapping(source = "genreIds", target = "genres", qualifiedByName = "genreIdsToGenres")
    Movie createDtoToModel(MovieCreateDto dto);

    @Mapping(source = "releaseDate", target = "releaseDate", dateFormat = DATE_PATTERN)
    @Mapping(source = "duration", target = "duration", qualifiedByName = "stringToDuration")
    @Mapping(source = "genreIds", target = "genres", qualifiedByName = "genreIdsToGenres")
    Movie updateDtoToModel(MovieUpdateDto dto);

    @Mapping(source = "releaseDate", target = "releaseDate", dateFormat = DATE_PATTERN)
    @Mapping(source = "duration", target = "duration", qualifiedByName = "durationToString")
    MovieShortDto modelToShortDto(Movie model);

    @Mapping(source = "releaseDate", target = "releaseDate", dateFormat = DATE_PATTERN)
    @Mapping(source = "duration", target = "duration", qualifiedByName = "durationToString")
    List<MovieShortDto> modelListToShortDtoList(List<Movie> modelList);

    default PageRequest pageDtoToPageRequest(MovieReadPageDto dto) {
        return PageRequest.of(dto.getPage(), dto.getSize(),
                Sort.by(dto.getDirection(), dto.getProperty().getName()));
    }

    @Named("durationToString")
    default String durationToString(Duration duration) {
        return String.format("%02d:%02d:%02d", duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart());
    }

    @Named("stringToDuration")
    default Duration stringToDuration(String string) {
        return Duration.between(LocalTime.MIN, LocalTime.parse(string));
    }

    @Named("genreIdsToGenres")
    default Set<Genre> genreIdsToGenres(Set<Long> genreIds) {
        return genreIds.stream()
                .map(id -> Genre.builder().id(id).build())
                .collect(Collectors.toSet());
    }
}