package com.example.moview.moview.exception;

public class UnauthorizedAuthorException extends RuntimeException {

    public UnauthorizedAuthorException(final String message) {
        super(message);
    }
}