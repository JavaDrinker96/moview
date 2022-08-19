package com.example.moview.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.*;

@Getter
@Setter
@Builder
@Jacksonized
public class UserUpdateDto {

    @NotNull
    @Min(1)
    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{4}$")
    private String birthday;

    @NotNull
    @Email
    private String email;

    @NotBlank
    private String password;
}