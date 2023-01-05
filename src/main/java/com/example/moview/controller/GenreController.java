package com.example.moview.controller;

import com.example.moview.dto.genre.GenreCreateDto;
import com.example.moview.dto.genre.GenreDto;
import com.example.moview.dto.genre.GenreUpdateDto;
import com.example.moview.mapper.GenreMapper;
import com.example.moview.model.Genre;
import com.example.moview.service.GenreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/genre")
@RequiredArgsConstructor
@Api(tags = "Endpoints for movies genres")
public class GenreController {

    private final GenreService genreService;
    private final GenreMapper genreMapper;


    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiOperation("Add new genre")
    public GenreDto create(@RequestBody @Valid final GenreCreateDto dto) {
        final Genre genre = genreMapper.genreCreateDtoToEntity(dto);
        return genreMapper.entityToGenreDto(genreService.create(genre));
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation("Get genre by id")
    public GenreDto read(@PathVariable final Long id) {
        return genreMapper.entityToGenreDto(genreService.readById(id));
    }

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation("Update genre")
    public GenreDto update(@RequestBody @Valid final GenreUpdateDto dto) {
        final Genre genre = genreMapper.genreUpdateDtoToEntity(dto);
        return genreMapper.entityToGenreDto(genreService.update(genre));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation("Delete genre by id")
    public void delete(@PathVariable final Long id) {
        genreService.delete(id);
    }

    @GetMapping("/all")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation("Get all genres")
    public List<GenreDto> readAll() {
        return genreMapper.entityListToGenreDtoList(genreService.readAll());
    }

}