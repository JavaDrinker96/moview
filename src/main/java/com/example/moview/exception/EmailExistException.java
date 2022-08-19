package com.example.moview.exception;

public class EmailExistException extends RuntimeException {

    public EmailExistException(final String message) {
        super(message);
    }
}
