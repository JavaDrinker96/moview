package com.example.moview.moview.handler.error;

import com.example.moview.moview.dto.ExceptionResponse;
import com.example.moview.moview.exception.ForbiddenAuthorException;
import com.example.moview.moview.exception.NullParameterException;
import com.example.moview.moview.exception.UnauthorizedAuthorException;
import com.example.moview.moview.util.datetime.DateTimeConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import java.time.LocalDateTime;

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

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(final EntityNotFoundException e) {
        final ExceptionResponse response = buildErrorResponse(e, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, response.getStatus());
    }

//    @ExceptionHandler(DataIntegrityViolationException.class)
//    protected ResponseEntity<Object> handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
//        final ExceptionResponse response = buildErrorResponse(e, HttpStatus.BAD_REQUEST);
//        return new ResponseEntity<>(response, response.getStatus());
//    }

    @ExceptionHandler(UnexpectedTypeException.class)
    protected ResponseEntity<Object> handleUnexpectedTypeException(final UnexpectedTypeException e) {
        final ExceptionResponse response = buildErrorResponse(e, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(UnauthorizedAuthorException.class)
    protected ResponseEntity<Object> handleUnauthorizedAuthorException(final UnauthorizedAuthorException e) {
        final ExceptionResponse response = buildErrorResponse(e, HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(ForbiddenAuthorException.class)
    protected ResponseEntity<Object> handleForbiddenAuthorException(final ForbiddenAuthorException e){
        final ExceptionResponse response = buildErrorResponse(e, HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(response, response.getStatus());
    }

    private ExceptionResponse buildErrorResponse(final Throwable e, final HttpStatus status) {
        return ExceptionResponse.builder()
                .error(e.getClass().getSimpleName())
                .message(e.getMessage())
                .status(status)
                .timestamp(dateTimeConverter.formatLocalDateTimeToString(LocalDateTime.now()))
                .build();
    }
}