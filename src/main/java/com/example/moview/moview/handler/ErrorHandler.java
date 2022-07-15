package com.example.moview.moview.handler;

import com.example.moview.moview.dto.ErrorDto;
import com.example.moview.moview.exception.NotFoundException;
import com.example.moview.moview.exception.NullParameterException;
import com.example.moview.moview.util.datetime.DateTimeConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import java.time.LocalDateTime;

@ControllerAdvice
public class ErrorHandler {

    private final DateTimeConverter dateTimeConverter;

    public ErrorHandler(DateTimeConverter dateTimeConverter) {
        this.dateTimeConverter = dateTimeConverter;
    }

    @ExceptionHandler(NullParameterException.class)
    protected ResponseEntity<Object> handleNullParameterException(final NullParameterException e) {
        final ErrorDto response = buildResponse(e, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(final NotFoundException e) {
        final ErrorDto response = buildResponse(e, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    protected ResponseEntity<Object> handleUnexpectedTypeException(final UnexpectedTypeException e) {
        final ErrorDto response = buildResponse(e, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(final ConstraintViolationException e) {
        final ErrorDto response = buildResponse(e, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, response.getStatus());
    }

    private ErrorDto buildResponse(final RuntimeException e, final HttpStatus status) {
        return ErrorDto.builder()
                .error(e.getClass().getName())
                .message(e.getMessage())
                .status(status)
                .timestamp(dateTimeConverter.formatLocalDateTimeToString(LocalDateTime.now()))
                .build();
    }
}
