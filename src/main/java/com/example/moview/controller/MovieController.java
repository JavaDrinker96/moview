package com.example.moview.controller;

import com.example.moview.dto.movie.*;
import com.example.moview.dto.pagination.MovieReadPageDto;
import com.example.moview.facade.MovieFacade;
import com.example.moview.mapper.MovieMapper;
import com.example.moview.mapper.PaginationMapper;
import com.example.moview.model.Movie;
import com.example.moview.security.CurrentUser;
import com.example.moview.security.UserPrincipal;
import com.example.moview.service.MovieService;
import com.example.moview.validator.GenreValidator;
import com.example.moview.validator.MovieValidator;
import com.example.moview.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieFacade movieFacade;
    private final MovieService movieService;
    private final MovieMapper movieMapper;
    private final UserValidator userValidator;
    private final GenreValidator genreValidator;
    private final MovieValidator movieValidator;
    private final PaginationMapper paginationMapper;


    @PostMapping
    public ResponseEntity<MovieShortDto> create(@CurrentUser final UserPrincipal userPrincipal,
                                                @RequestBody @Valid final MovieCreateDto dto) {

        userValidator.validateUserExisting(userPrincipal);

        final Movie movie = movieFacade.create(movieMapper.movieCreateDtoToEntity(dto, userPrincipal.getId()));
        final MovieShortDto movieShortDto = movieMapper.entityToMovieShortDto(movie);

        return ResponseEntity.status(HttpStatus.CREATED).body(movieShortDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> deepRead(@PathVariable final Long id) {
        final MovieDto movieDto = movieFacade.read(id);
        return ResponseEntity.ok(movieDto);
    }

    @PutMapping
    public ResponseEntity<MovieShortDto> update(@CurrentUser final UserPrincipal userPrincipal,
                                                @RequestBody @Valid final MovieUpdateDto dto) {

        userValidator.validateUserExisting(userPrincipal);
        movieValidator.validateUserPermissionToInteract(userPrincipal, dto.getId());

        final Movie movie = movieFacade.update(movieMapper.movieUpdateDtoToEntity(dto));
        final MovieShortDto movieShortDto = movieMapper.entityToMovieShortDto(movie);
        return ResponseEntity.ok(movieShortDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable final Long id,
                       @CurrentUser UserPrincipal userPrincipal) {

        userValidator.validateUserExisting(userPrincipal);
        movieValidator.validateUserPermissionToInteract(userPrincipal, id);

        movieService.delete(id);
    }

    @GetMapping("/all")
    public ResponseEntity<MovieShortPageResponse> findByTitle(@CurrentUser final UserPrincipal userPrincipal,
                                                              MovieReadPageDto dto) {

        final PageRequest pageRequest = paginationMapper.dtoToRequest(dto, dto.getProperty().getName());

        final Page<Movie> moviePage = dto.isForCurrentUser()
                ? movieService.findByTitleAndUserId(dto.getTitle(), userPrincipal.getId(), pageRequest)
                : movieService.findByTitle(dto.getTitle(), pageRequest);

        final List<MovieShortDto> movieShortDtoList = movieMapper.entityListToMovieShortDtoList(moviePage.getContent());
        return ResponseEntity.ok(MovieShortPageResponse.of(moviePage.getTotalPages(), movieShortDtoList));
    }

    @GetMapping("/top")
    public ResponseEntity<List<MovieShortDto>> getRecommendation(@CurrentUser final UserPrincipal userPrincipal,
                                                                 @RequestBody final List<Long> genreIds) {

        userValidator.validateUserExisting(userPrincipal);
        genreValidator.validateGenresIds(genreIds);
        final List<Movie> movieList = movieService.getUsersTop(userPrincipal.getId(), genreIds);
        final List<MovieShortDto> dtoList = movieMapper.entityListToMovieShortDtoList(movieList);
        return ResponseEntity.ok(dtoList);
    }
}