package com.example.moview.handler.error;

import com.example.moview.dto.ExceptionResponse;
import com.example.moview.exception.ContentPermissionException;
import com.example.moview.exception.ForbiddenUserException;
import com.example.moview.exception.NotValidTokenException;
import com.example.moview.exception.UnauthorizedUserException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

import static java.time.LocalDateTime.now;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        final ExceptionResponse response = buildErrorResponse(e, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(NotValidTokenException.class)
    protected ResponseEntity<Object> handleNotValidTokenException(final NotValidTokenException e) {
        final ExceptionResponse response = buildErrorResponse(e, HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(ContentPermissionException.class)
    protected ResponseEntity<Object> handleContentPermissionException(final ContentPermissionException e) {
        final ExceptionResponse response = buildErrorResponse(e, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleValidationException(final ConstraintViolationException e) {
        final ExceptionResponse response = buildErrorResponse(e, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        final ExceptionResponse response = buildErrorResponse(e, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<Object> handleNoSuchElementException(final NoSuchElementException e) {
        final ExceptionResponse response = buildErrorResponse(e, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(final EntityNotFoundException e) {
        final ExceptionResponse response = buildErrorResponse(e, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    protected ResponseEntity<Object> handleUnexpectedTypeException(final UnexpectedTypeException e) {
        final ExceptionResponse response = buildErrorResponse(e, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    protected ResponseEntity<Object> handleUnauthorizedAuthorException(final UnauthorizedUserException e) {
        final ExceptionResponse response = buildErrorResponse(e, HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(ForbiddenUserException.class)
    protected ResponseEntity<Object> handleForbiddenAuthorException(final ForbiddenUserException e) {
        final ExceptionResponse response = buildErrorResponse(e, HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(response, response.getStatus());
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