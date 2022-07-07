package com.example.moview.servlet;

import com.example.moview.service.MovieService;
import com.example.moview.service.MovieServiceImpl;
import com.example.moview.util.io.IoUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/movie")
public class MovieServlet extends HttpServlet {

    private final MovieService service;

    public MovieServlet() {
        this.service = new MovieServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        final boolean allRead = Boolean.parseBoolean(request.getParameter("all"));
        final String jsonResultString = allRead
                ? service.readAll()
                : service.read(Long.valueOf(request.getParameter("id")));

        IoUtil.printJsonResponse(jsonResultString, 200, response);
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) {
        service.create(IoUtil.readJsonRequest(request));
    }

    @Override
    protected void doPut(final HttpServletRequest request, final HttpServletResponse response) {
        service.update(IoUtil.readJsonRequest(request));
    }

    @Override
    protected void doDelete(final HttpServletRequest request, final HttpServletResponse response) {
        final Long id = Long.valueOf(request.getParameter("id"));
        service.delete(id);
    }
}