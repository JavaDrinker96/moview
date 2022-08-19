package com.example.moview.exception;

public class ForbiddenUserException extends RuntimeException {

    public ForbiddenUserException(final String message) {
        super(message);
    }
}