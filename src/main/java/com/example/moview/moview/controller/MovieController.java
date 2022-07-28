package com.example.moview.moview.controller;

import com.example.moview.moview.dto.movie.MovieCreateDto;
import com.example.moview.moview.dto.movie.MovieDto;
import com.example.moview.moview.dto.movie.MovieShortDto;
import com.example.moview.moview.dto.movie.MovieUpdateDto;
import com.example.moview.moview.model.Movie;
import com.example.moview.moview.service.MovieService;
import org.modelmapper.ModelMapper;
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
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;
    private final ModelMapper modelMapper;

    public MovieController(final MovieService movieService, final ModelMapper modelMapper) {
        this.movieService = movieService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<MovieShortDto> create(@RequestBody @Valid final MovieCreateDto dto) {
        final Movie movie = modelMapper.map(dto, Movie.class);
        final MovieShortDto movieShortDto = modelMapper.map(movieService.create(movie), MovieShortDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(movieShortDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> deepRead(@PathVariable final Long id) {
        final MovieDto movieDto = modelMapper.map(movieService.read(id), MovieDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(movieDto);
    }

    @PutMapping
    public ResponseEntity<MovieDto> update(@RequestBody @Valid final MovieUpdateDto dto) {
        final Movie movie = modelMapper.map(dto, Movie.class);
        final MovieDto movieDto = modelMapper.map(movieService.update(movie), MovieDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(movieDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable final Long id) {
        movieService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<MovieShortDto>> readAll() {
        final List<MovieShortDto> movieShortDtoList = movieService.readAll().stream()
                .map(x -> modelMapper.map(x, MovieShortDto.class)).toList();
        return ResponseEntity.status(HttpStatus.OK).body(movieShortDtoList);
    }
}
