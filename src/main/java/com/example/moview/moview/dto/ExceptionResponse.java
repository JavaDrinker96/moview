package com.example.moview.moview.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import org.springframework.http.HttpStatus;


@Getter
@Setter
@Builder
@Jacksonized
public class ExceptionResponse {

    private String error;
    private String message;
    private HttpStatus status;
    private String timestamp;
}