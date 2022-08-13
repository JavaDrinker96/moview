package com.example.moview.controller;

import com.example.moview.dto.genre.GenreUpdateDto;
import com.example.moview.mapper.GenreMapper;
import com.example.moview.model.Genre;
import com.example.moview.dto.genre.GenreCreateDto;
import com.example.moview.dto.genre.GenreDto;
import com.example.moview.service.GenreService;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation("Create Genre.")
    public ResponseEntity<GenreDto> create(@RequestBody @Valid final GenreCreateDto dto) {
        final Genre genre = genreMapper.genreCreateDtoToEntity(dto);
        final GenreDto genreDto = genreMapper.entityToGenreDto(genreService.create(genre));
        return ResponseEntity.status(HttpStatus.CREATED).body(genreDto);
    }

    @GetMapping("/{id}")
    @ApiOperation("Find Genre genre by id.")
    public ResponseEntity<GenreDto> read(@PathVariable final Long id) {
        final GenreDto genreDto = genreMapper.entityToGenreDto(genreService.read(id));
        return ResponseEntity.ok(genreDto);
    }

    @PutMapping
    @ApiOperation("Update Genre.")
    public ResponseEntity<GenreDto> update(@RequestBody @Valid final GenreUpdateDto dto) {
        final Genre genre = genreMapper.genreUpdateDtoToEntity(dto);
        final GenreDto genreDto = genreMapper.entityToGenreDto(genreService.update(genre));
        return ResponseEntity.ok(genreDto);
    }

    @ApiOperation("Delete Genre by id.")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable final Long id) {
        genreService.delete(id);
    }

    @ApiOperation("Find all Genres.")
    @GetMapping("/all")
    public ResponseEntity<List<GenreDto>> readAll() {
        final List<GenreDto> dtoList = genreMapper.entityListToGenreDtoList(genreService.readAll());
        return ResponseEntity.ok(dtoList);
    }
}