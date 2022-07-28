package com.example.moview.moview.handler.error;

import com.example.moview.moview.dto.ExceptionResponse;
import com.example.moview.moview.exception.NotFoundException;
import com.example.moview.moview.exception.NullParameterException;
import com.example.moview.moview.util.datetime.DateTimeConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;

import static java.time.LocalDateTime.now;

@ControllerAdvice
public class ErrorHandler {

    private final DateTimeConverter dateTimeConverter;

    public ErrorHandler(DateTimeConverter dateTimeConverter) {
        this.dateTimeConverter = dateTimeConverter;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        final ExceptionResponse response = buildErrorResponse(e, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(final ConstraintViolationException e) {
        final ExceptionResponse response = buildErrorResponse(e, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(NullParameterException.class)
    protected ResponseEntity<Object> handleNullParameterException(final NullParameterException e) {
        final ExceptionResponse response = buildErrorResponse(e, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(final NotFoundException e) {
        final ExceptionResponse response = buildErrorResponse(e, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    protected ResponseEntity<Object> handleUnexpectedTypeException(final UnexpectedTypeException e) {
        final ExceptionResponse response = buildErrorResponse(e, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, response.getStatus());
    }

    private ExceptionResponse buildErrorResponse(final Throwable e, final HttpStatus status) {
        return ExceptionResponse.builder()
                .error(e.getClass().getSimpleName())
                .message(e.getMessage())
                .status(status)
                .timestamp(dateTimeConverter.formatLocalDateTimeToString(now()))
                .build();
    }
}
