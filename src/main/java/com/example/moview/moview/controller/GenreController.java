package com.example.moview.moview.controller;

import com.example.moview.moview.dto.genre.GenreCreateDto;
import com.example.moview.moview.dto.genre.GenreDto;
import com.example.moview.moview.dto.genre.GenreUpdateDto;
import com.example.moview.moview.mapper.GenreMapper;
import com.example.moview.moview.model.Genre;
import com.example.moview.moview.service.GenreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class GenreController {

    private final GenreService genreService;
    private final GenreMapper genreMapper;

    public GenreController(final GenreService genreService, final GenreMapper genreMapper) {
        this.genreService = genreService;
        this.genreMapper = genreMapper;
    }

    @RequestMapping(value = "/genre", method = RequestMethod.POST)
    public ResponseEntity<GenreDto> create(@RequestBody @Valid final GenreCreateDto dto) {
        final Genre genre = genreMapper.createDtoToModel(dto);
        final Genre createdGenre = genreService.create(genre);
        final GenreDto dtoCreated = genreMapper.modelToDto(createdGenre);
        return ResponseEntity.status(HttpStatus.OK).body(dtoCreated);
    }

    @RequestMapping(value = "/genre/{id}", method = RequestMethod.GET)
    public ResponseEntity<GenreDto> read(@PathVariable final Long id) {
        final Genre genre = genreService.read(id);
        final GenreDto dto = genreMapper.modelToDto(genre);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @RequestMapping(value = "/genre", method = RequestMethod.PUT)
    public ResponseEntity<GenreDto> update(@RequestBody @Valid final GenreUpdateDto dto) {
        final Genre genre = genreMapper.updateDtoToModel(dto);
        final Genre updatedGenre = genreService.update(genre);
        final GenreDto dtoUpdated = genreMapper.modelToDto(updatedGenre);
        return ResponseEntity.status(HttpStatus.OK).body(dtoUpdated);
    }

    @RequestMapping(value = "/genre/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable final Long id) {
        genreService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(value = "/genre/all", method = RequestMethod.GET)
    public ResponseEntity<List<GenreDto>> readAll() {
        final List<Genre> genreList = genreService.readAll();
        final List<GenreDto> dtoList = genreMapper.modelListToDtoList(genreList);
        return ResponseEntity.status(HttpStatus.OK).body(dtoList);
    }
}