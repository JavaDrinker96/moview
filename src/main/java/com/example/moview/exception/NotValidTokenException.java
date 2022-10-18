package com.example.moview.exception;

public class NotValidTokenException extends RuntimeException {

    public NotValidTokenException(final String message) {
        super(message);
    }
}
