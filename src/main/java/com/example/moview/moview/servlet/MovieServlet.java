package com.example.moview.moview.servlet;

import com.example.moview.moview.config.mode_mapper.ModelMapperConfigurer;
import com.example.moview.moview.dto.movie.MovieCreateDto;
import com.example.moview.moview.dto.movie.MovieDto;
import com.example.moview.moview.dto.movie.MovieShortDto;
import com.example.moview.moview.dto.movie.MovieUpdateDto;
import com.example.moview.moview.model.Movie;
import com.example.moview.moview.service.MovieService;
import com.example.moview.moview.service.MovieServiceImpl;
import com.example.moview.moview.util.io.IoUtil;
import com.example.moview.moview.util.json.JsonMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/movie")
public class MovieServlet extends HttpServlet {

    private final MovieService service;
    private final ModelMapper modelMapper;

    public MovieServlet() {
        this.service = new MovieServiceImpl();
        this.modelMapper = new ModelMapper();
        ModelMapperConfigurer.movieConfigure(modelMapper);
    }

    @Override
    @SneakyThrows
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {
        final boolean allRead = Boolean.parseBoolean(request.getParameter("all"));
        final String jsonResultString;

        if (allRead) {
            final List<Movie> movieList = service.readAll();
            final List<MovieShortDto> dtoList = mapMovieListToMovieShortDtoList(movieList);
            jsonResultString = JsonMapper.writeValueAsString(dtoList);
        } else {
            final Movie movie = service.read(Long.valueOf(request.getParameter("id")));
            final MovieDto dto = modelMapper.map(movie, MovieDto.class);
            jsonResultString = JsonMapper.writeValueAsString(dto);
        }

        IoUtil.printJsonResponse(jsonResultString, 200, response);
    }

    @Override
    @SneakyThrows
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) {
        final MovieCreateDto createDto = JsonMapper.readValue(IoUtil.readJsonRequest(request), MovieCreateDto.class);
        final Movie movie = modelMapper.map(createDto, Movie.class);
        service.create(movie);
    }

    @Override
    @SneakyThrows
    protected void doPut(final HttpServletRequest request, final HttpServletResponse response) {
        final MovieUpdateDto updateDto = JsonMapper.readValue(IoUtil.readJsonRequest(request), MovieUpdateDto.class);
        final Movie movie = modelMapper.map(updateDto, Movie.class);
        service.update(movie);
    }

    @Override
    @SneakyThrows
    protected void doDelete(final HttpServletRequest request, final HttpServletResponse response) {
        final Long id = Long.valueOf(request.getParameter("id"));
        service.delete(id);
    }

    private List<MovieShortDto> mapMovieListToMovieShortDtoList(final List<Movie> movieList) {
        return movieList.stream()
                .map(movie -> modelMapper.map(movie, MovieShortDto.class))
                .collect(Collectors.toList());
    }
}