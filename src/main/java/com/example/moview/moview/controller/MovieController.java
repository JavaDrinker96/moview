package com.example.moview.moview.controller;

import com.example.moview.moview.dto.movie.MovieCreateDto;
import com.example.moview.moview.dto.movie.MovieDto;
import com.example.moview.moview.dto.movie.MovieShortDto;
import com.example.moview.moview.dto.movie.MovieUpdateDto;
import com.example.moview.moview.dto.pagination.MovieReadPageDto;
import com.example.moview.moview.facade.MovieFacade;
import com.example.moview.moview.mapper.MovieMapper;
import com.example.moview.moview.model.Movie;
import com.example.moview.moview.service.MovieService;
import com.example.moview.moview.validator.GenreValidator;
import com.example.moview.moview.validator.UserValidator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {

    private final MovieFacade movieFacade;
    private final MovieService movieService;
    private final MovieMapper movieMapper;
    private final UserValidator userValidator;
    private final GenreValidator genreValidator;


    public MovieController(final MovieFacade movieFacade,
                           final MovieService movieService,
                           final MovieMapper movieMapper,
                           final UserValidator userValidator,
                           final GenreValidator genreValidator) {

        this.movieFacade = movieFacade;
        this.movieService = movieService;
        this.movieMapper = movieMapper;
        this.userValidator = userValidator;
        this.genreValidator = genreValidator;
    }

    @PostMapping
    public ResponseEntity<MovieShortDto> create(@RequestBody @Valid final MovieCreateDto dto) {
        final Movie movie = movieFacade.create(movieMapper.movieCreateDtoToEntity(dto));
        final MovieShortDto movieShortDto = movieMapper.entityToMovieShortDto(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(movieShortDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> deepRead(@PathVariable final Long id) {
        final MovieDto movieDto = movieFacade.read(id);
        return ResponseEntity.ok(movieDto);
    }

    @PutMapping
    public ResponseEntity<MovieShortDto> update(@RequestBody @Valid final MovieUpdateDto dto) {
        final Movie movie = movieFacade.update(movieMapper.movieUpdateDtoToEntity(dto));
        final MovieShortDto movieShortDto = movieMapper.entityToMovieShortDto(movie);
        return ResponseEntity.ok(movieShortDto);
    }

    @DeleteMapping("/{id}")
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