package com.example.moview.moview.controller;

import com.example.moview.moview.dto.movie.MovieCreateDto;
import com.example.moview.moview.dto.movie.MovieDto;
import com.example.moview.moview.dto.movie.MovieShortDto;
import com.example.moview.moview.dto.movie.MovieUpdateDto;
import com.example.moview.moview.dto.pagination.MovieReadPageDto;
import com.example.moview.moview.mapper.MovieMapper;
import com.example.moview.moview.model.Movie;
import com.example.moview.moview.service.MovieService;
import com.example.moview.moview.validator.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class MovieController {

    private final MovieService movieService;
    private final MovieMapper movieMapper;
    private final UserValidator userValidator;


    public MovieController(final MovieService movieService,
                           final MovieMapper movieMapper,
                           final UserValidator userValidator) {

        this.movieService = movieService;
        this.movieMapper = movieMapper;
        this.userValidator = userValidator;
    }

    @RequestMapping(value = "/movie", method = RequestMethod.POST)
    public ResponseEntity<MovieShortDto> create(@RequestBody @Valid final MovieCreateDto dto) {
        final Movie movie = movieMapper.createDtoToModel(dto);
        final Movie createdMovie = movieService.create(movie);
        final MovieShortDto dtoCreated = movieMapper.modelToShortDto(createdMovie);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoCreated);
    }

    @RequestMapping(value = "/movie/{id}", method = RequestMethod.GET)
    public ResponseEntity<MovieDto> deepRead(@PathVariable final Long id) {
        final Movie movie = movieService.read(id);
        final MovieDto dto = movieMapper.modelToDto(movie);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @RequestMapping(value = "/movie", method = RequestMethod.PUT)
    public ResponseEntity<MovieShortDto> update(@RequestBody @Valid final MovieUpdateDto dto) {
        final Movie movie = movieMapper.updateDtoToModel(dto);
        final Movie updatedMovie = movieService.update(movie);
        final MovieShortDto dtoUpdated = movieMapper.modelToShortDto(updatedMovie);
        return ResponseEntity.status(HttpStatus.OK).body(dtoUpdated);
    }

    @RequestMapping(value = "/movie/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable final Long id) {
        movieService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(value = "/movie/all", method = RequestMethod.GET)
    public ResponseEntity<List<MovieShortDto>> readAll(@RequestBody @Valid final MovieReadPageDto dto) {
        final PageRequest pageRequest = movieMapper.pageDtoToPageRequest(dto);
        final Page<Movie> moviePage = movieService.readAll(pageRequest);
        final List<MovieShortDto> dtoList = movieMapper.modelListToShortDtoList(moviePage.getContent());
        return ResponseEntity.status(HttpStatus.OK).body(dtoList);
    }

    @RequestMapping(value = "/movie/top", method = RequestMethod.GET)
    public ResponseEntity<List<MovieShortDto>> getRecommendation(@RequestHeader("Authorization") final Long userId,
                                                                 @RequestBody final List<Long> genreIds) {

        userValidator.validateUserExisting(userId);
        final List<Movie> movieList = movieService.getUsersTop(userId, genreIds);
        final List<MovieShortDto> dtoList = movieMapper.modelListToShortDtoList(movieList);
        return ResponseEntity.status(HttpStatus.OK).body(dtoList);
    }
}