package com.example.moview.moview.mapper;

import com.example.moview.moview.dto.genre.GenreCreateDto;
import com.example.moview.moview.dto.genre.GenreDto;
import com.example.moview.moview.dto.genre.GenreUpdateDto;
import com.example.moview.moview.model.Genre;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    Genre genreCreateDtoToEntity(GenreCreateDto dto);

    GenreDto entityToGenreDto(Genre entity);

    Genre genreUpdateDtoToEntity(GenreUpdateDto dto);

    List<GenreDto> entityListToGenreDtoList(List<Genre> entityList);

    Set<GenreDto> entitySetToGenreDtoSet(Set<Genre> entitySet);
}