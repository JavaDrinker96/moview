package com.example.moview.exception;

public class UnauthorizedAuthorException extends RuntimeException {

    public UnauthorizedAuthorException(final String message) {
        super(message);
    }
}