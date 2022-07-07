package com.example.moview.service;

import com.example.moview.dto.movie.MovieCreateDto;
import com.example.moview.dto.movie.MovieDto;
import com.example.moview.dto.movie.MovieShortDto;
import com.example.moview.dto.movie.MovieUpdateDto;
import com.example.moview.model.Movie;
import com.example.moview.repository.MovieRepository;
import com.example.moview.repository.MovieRepositoryImpl;
import com.example.moview.util.mapper.JsonMapper;
import com.example.moview.util.mapper.ModelMapperConfigurer;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class MovieServiceImpl implements MovieService {

    private final ModelMapper modelMapper;
    private final MovieRepository repository;

    public MovieServiceImpl() {
        this.modelMapper = new ModelMapper();
        ModelMapperConfigurer.movieConfigure(modelMapper);
        this.repository = new MovieRepositoryImpl();
    }

    @Override
    public void create(final String jsonMovie) {
        final MovieCreateDto createDto = JsonMapper.readValue(jsonMovie, MovieCreateDto.class);
        final Movie movie = modelMapper.map(createDto, Movie.class);
        repository.create(movie);
    }

    @Override
    public void update(final String jsonMovie) {
        final MovieUpdateDto dto = JsonMapper.readValue(jsonMovie, MovieUpdateDto.class);
        final Movie movie = modelMapper.map(dto, Movie.class);
        repository.update(movie);
    }

    @Override
    public void delete(final Long id) {
        repository.delete(id);
    }

    @Override
    public String read(final Long id) {
        final Movie movie = repository.read(id);
        final MovieDto dto = modelMapper.map(movie, MovieDto.class);
        return JsonMapper.writeValueAsString(dto);
    }

    @Override
    public String readAll() {
        final List<Movie> movieList = repository.readAll();
        final List<MovieShortDto> dtoList = new ArrayList<>();

        for (final Movie movie : movieList) {
            dtoList.add(modelMapper.map(movie, MovieShortDto.class));
        }

        return JsonMapper.writeValueAsString(dtoList);
    }
}