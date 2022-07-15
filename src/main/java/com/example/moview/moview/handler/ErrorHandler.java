package com.example.moview.moview.handler;

import com.example.moview.moview.dto.ErrorDto;
import com.example.moview.moview.exception.NullParameterException;
import com.example.moview.moview.util.datetime.DateTimeConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ErrorHandler {

    private final DateTimeConverter dateTimeConverter;

    public ErrorHandler(DateTimeConverter dateTimeConverter) {
        this.dateTimeConverter = dateTimeConverter;
    }

    @ExceptionHandler(NullParameterException.class)
    protected ResponseEntity<Object> handleNullParameterException(final NullParameterException e) {
        final ErrorDto response = ErrorDto.builder()
                .error(NullParameterException.class.getName())
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(dateTimeConverter.formatLocalDateTimeToString(LocalDateTime.now()))
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
