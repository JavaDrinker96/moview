package com.example.moview.exception;

public class UnauthorizedUserException extends RuntimeException {

    public UnauthorizedUserException(final String message) {
        super(message);
    }
}