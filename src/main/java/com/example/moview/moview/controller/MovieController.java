package com.example.moview.moview.controller;

import com.example.moview.moview.dto.movie.MovieCreateDto;
import com.example.moview.moview.dto.movie.MovieDto;
import com.example.moview.moview.dto.movie.MovieReadPageDto;
import com.example.moview.moview.dto.movie.MovieShortDto;
import com.example.moview.moview.dto.movie.MovieUpdateDto;
import com.example.moview.moview.model.Movie;
import com.example.moview.moview.service.MovieService;
import com.example.moview.moview.validator.UserValidator;
import org.modelmapper.ModelMapper;
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
import java.util.stream.Collectors;

@RestController
public class MovieController {

    private final MovieService movieService;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;


    public MovieController(final MovieService movieService,
                           final ModelMapper modelMapper,
                           final UserValidator userValidator) {

        this.movieService = movieService;
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
    }

    @RequestMapping(value = "/movie", method = RequestMethod.POST)
    public ResponseEntity<MovieShortDto> create(@RequestBody @Valid final MovieCreateDto dto) {
        final Movie movie = modelMapper.map(dto, Movie.class);
        final Movie createdMovie = movieService.create(movie);
        final MovieShortDto dtoCreated = modelMapper.map(createdMovie, MovieShortDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoCreated);
    }

    @RequestMapping(value = "/movie/{id}", method = RequestMethod.GET)
    public ResponseEntity<MovieDto> deepRead(@PathVariable final Long id) {
        final Movie movie = movieService.read(id);
        final MovieDto dto = modelMapper.map(movie, MovieDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @RequestMapping(value = "/movie", method = RequestMethod.PUT)
    public ResponseEntity<MovieShortDto> update(@RequestBody @Valid final MovieUpdateDto dto) {
        final Movie movie = modelMapper.map(dto, Movie.class);
        final Movie updatedMovie = movieService.update(movie);
        final MovieShortDto dtoUpdated = modelMapper.map(updatedMovie, MovieShortDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(dtoUpdated);
    }

    @RequestMapping(value = "/movie/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable final Long id) {
        movieService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(value = "/movie/all", method = RequestMethod.GET)
    public ResponseEntity<List<MovieShortDto>> readAll(@RequestBody @Valid final MovieReadPageDto dto) {
        final PageRequest pageRequest = modelMapper.map(dto, PageRequest.class);
        final Page<Movie> moviePage = movieService.readAll(pageRequest);
        final List<MovieShortDto> dtoList = moviePage.stream()
                .map(x -> modelMapper.map(x, MovieShortDto.class)).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(dtoList);
    }

    @RequestMapping(value = "/movie/top", method = RequestMethod.GET)
    public ResponseEntity<List<MovieShortDto>> getRecommendation(@RequestHeader("Authorization") final Long userId,
                                                            @RequestBody final List<Long> genreIds) {

        userValidator.validateUserExisting(userId);
        final List<Movie> movieList = movieService.getUsersTop(userId, genreIds);
        final List<MovieShortDto> dtoList = movieList.stream()
                .map(x -> modelMapper.map(x, MovieShortDto.class)).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(dtoList);
    }
}