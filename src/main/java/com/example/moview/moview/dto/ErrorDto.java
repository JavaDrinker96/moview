package com.example.moview.moview.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter
@Setter
@Builder(builderClassName = "ErrorDtoBuilder")
@JsonDeserialize(builder = ErrorDto.ErrorDtoBuilder.class)
public class ErrorDto {

    private String error;
    private String message;
    private HttpStatus status;
    private String timestamp;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ErrorDtoBuilder {
    }
}
