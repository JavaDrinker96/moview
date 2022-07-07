package com.example.moview.servlet;

import com.example.moview.service.ReviewService;
import com.example.moview.service.ReviewServiceImpl;
import com.example.moview.util.io.IoUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/review")
public class ReviewServlet extends HttpServlet {

    private final ReviewService service;

    public ReviewServlet() {
        this.service = new ReviewServiceImpl();
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
