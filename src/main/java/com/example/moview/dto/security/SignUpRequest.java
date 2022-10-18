package com.example.moview.dto.security;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
public class SignUpRequest {

    private String firstName;

    private String lastName;

    private String name;

    @NotBlank
    @Email
    private String email;

    private String birthday;

    @NotBlank
    private String password;
}
