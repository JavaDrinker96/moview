package com.example.moview.moview.servlet;

import com.example.moview.moview.config.mode_mapper.ModelMapperConfigurer;
import com.example.moview.moview.dto.review.ReviewCreateDto;
import com.example.moview.moview.dto.review.ReviewUpdateDto;
import com.example.moview.moview.model.Review;
import com.example.moview.moview.service.ReviewService;
import com.example.moview.moview.service.ReviewServiceImpl;
import com.example.moview.moview.util.io.IoUtil;
import com.example.moview.moview.util.json.JsonMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;

@WebServlet("/review")
public class ReviewServlet extends HttpServlet {

    private final ReviewService service;
    private final ModelMapper modelMapper;

    public ReviewServlet() {
        this.service = new ReviewServiceImpl();
        this.modelMapper = new ModelMapper();
        ModelMapperConfigurer.reviewConfigure(modelMapper);
    }

    @Override
    @SneakyThrows
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) {
        final ReviewCreateDto createDto = JsonMapper.readValue(IoUtil.readJsonRequest(request), ReviewCreateDto.class);
        final Review review = modelMapper.map(createDto, Review.class);
        service.create(review);
    }

    @Override
    @SneakyThrows
    protected void doPut(final HttpServletRequest request, final HttpServletResponse response) {
        final ReviewUpdateDto updateDto = JsonMapper.readValue(IoUtil.readJsonRequest(request), ReviewUpdateDto.class);
        final Review review = modelMapper.map(updateDto, Review.class);
        service.update(review);
    }

    @Override
    @SneakyThrows
    protected void doDelete(final HttpServletRequest request, final HttpServletResponse response) {
        final Long id = Long.valueOf(request.getParameter("id"));
        service.delete(id);
    }
}
