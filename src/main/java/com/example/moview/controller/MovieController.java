package com.example.moview.controller;

import com.example.moview.dto.movie.MovieCreateDto;
import com.example.moview.dto.movie.MovieDto;
import com.example.moview.dto.movie.MovieShortDto;
import com.example.moview.dto.movie.MovieUpdateDto;
import com.example.moview.dto.pagination.MovieReadPageDto;
import com.example.moview.facade.MovieFacade;
import com.example.moview.mapper.MovieMapper;
import com.example.moview.model.Movie;
import com.example.moview.service.MovieService;
import com.example.moview.validator.GenreValidator;
import com.example.moview.validator.UserValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
@Api(tags = "Endpoints for movies")
public class MovieController {

    private final MovieFacade movieFacade;
    private final MovieService movieService;
    private final MovieMapper movieMapper;
    private final UserValidator userValidator;
    private final GenreValidator genreValidator;


    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiOperation("Add new movie")
    public MovieShortDto create(@RequestBody @Valid final MovieCreateDto dto) {
        final Movie movie = movieFacade.create(movieMapper.movieCreateDtoToEntity(dto));
        return movieMapper.entityToMovieShortDto(movie);
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation("Get movie by id")
    public MovieDto deepRead(@PathVariable final Long id) {
        return movieFacade.read(id);
    }

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation("Change movie")
    public MovieShortDto update(@RequestBody @Valid final MovieUpdateDto dto) {
        final Movie movie = movieFacade.update(movieMapper.movieUpdateDtoToEntity(dto));
        return movieMapper.entityToMovieShortDto(movie);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation("Delete movie by id")
    public void delete(@PathVariable final Long id) {
        movieService.delete(id);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MovieShortDto>> readAll(@RequestBody @Valid final MovieReadPageDto dto) {
        final Page<Movie> page = movieService.readAll(movieMapper.pageDtoToPageRequest(dto));
        final List<MovieShortDto> movieShortDtoList = movieMapper.entityListToMovieShortDtoList(page.getContent());
        return ResponseEntity.ok(movieShortDtoList);
    }

    @GetMapping("/top")
    public ResponseEntity<List<MovieShortDto>> getRecommendation(@RequestHeader("Authorization") final Long userId,
                                                                 @RequestBody final List<Long> genreIds) {

        userValidator.validateUserExisting(userId);
        genreValidator.validateGenresIds(genreIds);
        final List<Movie> movieList = movieService.getUsersTop(userId, genreIds);
        final List<MovieShortDto> dtoList = movieMapper.entityListToMovieShortDtoList(movieList);
        return ResponseEntity.ok(dtoList);
    }

}