package com.example.moview.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter
@Setter
public class ExceptionResponse {

    private String error;
    private String message;
    private HttpStatus status;
    private String timestamp;

}