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

    Genre createDtoToModel(GenreCreateDto dto);

    GenreDto modelToDto(Genre model);

    Genre updateDtoToModel(GenreUpdateDto dto);

    List<GenreDto> modelListToDtoList(List<Genre> modelList);

    Set<GenreDto> modelSetToDtoSet(Set<Genre> modelSet);
}