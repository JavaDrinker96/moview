package com.example.moview.exception.handler;

import com.example.moview.dto.ExceptionResponse;
import com.example.moview.exception.ForbiddenAuthorException;
import com.example.moview.exception.UnauthorizedAuthorException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

import static java.time.LocalDateTime.now;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            DataIntegrityViolationException.class,
            NoSuchElementException.class,
            EntityNotFoundException.class,
            UnexpectedTypeException.class
    })
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    protected ExceptionResponse handleBadRequestException(RuntimeException e) {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedAuthorException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    protected ExceptionResponse handleUnauthorizedAuthorException(final UnauthorizedAuthorException e) {
        return buildErrorResponse(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenAuthorException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    protected ExceptionResponse handleForbiddenAuthorException(final ForbiddenAuthorException e) {
        return buildErrorResponse(e, HttpStatus.FORBIDDEN);
    }

    private ExceptionResponse buildErrorResponse(final Throwable e, final HttpStatus status) {
        final String DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm:ss";

        return ExceptionResponse.builder()
                .error(e.getClass().getSimpleName())
                .message(e.getMessage())
                .status(status)
                .timestamp(now().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)))
                .build();
    }

}