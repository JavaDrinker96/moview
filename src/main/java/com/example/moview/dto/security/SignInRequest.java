package com.example.moview.dto.security;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SignInRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
