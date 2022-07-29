package com.example.moview.moview.controller;

import com.example.moview.moview.dto.genre.GenreCreateDto;
import com.example.moview.moview.dto.genre.GenreDto;
import com.example.moview.moview.dto.genre.GenreUpdateDto;
import com.example.moview.moview.mapper.GenreMapper;
import com.example.moview.moview.model.Genre;
import com.example.moview.moview.service.GenreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/genre")
public class GenreController {

    private final GenreService genreService;
    private final GenreMapper genreMapper;

    public GenreController(final GenreService genreService, final GenreMapper genreMapper) {
        this.genreService = genreService;
        this.genreMapper = genreMapper;
    }

    @PostMapping
    public ResponseEntity<GenreDto> create(@RequestBody @Valid final GenreCreateDto dto) {
        final Genre genre = genreMapper.createDtoToModel(dto);
        final GenreDto genreDto = genreMapper.modelToDto(genreService.create(genre));
        return ResponseEntity.status(HttpStatus.OK).body(genreDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDto> read(@PathVariable final Long id) {
        final GenreDto genreDto = genreMapper.modelToDto(genreService.read(id));
        return ResponseEntity.status(HttpStatus.CREATED).body(genreDto);
    }

    @PutMapping
    public ResponseEntity<GenreDto> update(@RequestBody @Valid final GenreUpdateDto dto) {
        final Genre genre = genreMapper.updateDtoToModel(dto);
        final GenreDto genreDto = genreMapper.modelToDto(genreService.update(genre));
        return ResponseEntity.status(HttpStatus.OK).body(genreDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable final Long id) {
        genreService.delete(id);
    }

    @GetMapping("/all")
    public ResponseEntity<List<GenreDto>> readAll() {
        final List<GenreDto> dtoList = genreMapper.modelListToDtoList(genreService.readAll());
        return ResponseEntity.status(HttpStatus.OK).body(dtoList);
    }
}