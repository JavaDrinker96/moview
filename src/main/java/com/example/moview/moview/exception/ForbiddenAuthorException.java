package com.example.moview.moview.exception;

public class ForbiddenAuthorException extends RuntimeException {

    public ForbiddenAuthorException(final String message) {
        super(message);
    }
}