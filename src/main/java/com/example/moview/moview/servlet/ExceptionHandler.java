package com.example.moview.moview.servlet;

import com.example.moview.moview.dto.ErrorDto;
import com.example.moview.moview.util.datetime.DateTimeUtil;
import com.example.moview.moview.util.io.IoUtil;
import com.example.moview.moview.util.json.JsonMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.time.LocalDateTime;

@WebServlet("/exception-handler")
public class ExceptionHandler extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        handleException(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        handleException(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        handleException(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        handleException(request, response);
    }

    @SneakyThrows
    private void handleException(final HttpServletRequest request, final HttpServletResponse response) {
        final Exception exception = (Exception) request.getAttribute("jakarta.servlet.error.exception");
        final Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");

        final ErrorDto errorDto = ErrorDto.builder()
                .error(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .status(determineStatus(exception, statusCode))
                .timestamp(DateTimeUtil.formatLocalDateTimeToString(LocalDateTime.now()))
                .build();
        final String jsonErrorDto = JsonMapper.writeValueAsString(errorDto);
        IoUtil.printJsonResponse(jsonErrorDto, statusCode, response);
    }

    private Integer determineStatus(final Exception exception, final Integer defaultStatus) {
        final String exceptionName = exception.getClass().getSimpleName();
        return switch (exceptionName) {
            case "NumberFormatException", "MappingException", "JsonMappingException" -> 400;
            case "ClassNotFoundException" -> 404;
            default -> defaultStatus;
        };
    }
}
