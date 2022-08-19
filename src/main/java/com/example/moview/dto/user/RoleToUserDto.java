package com.example.moview.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@Jacksonized
public class RoleToUserDto {

    @Email
    private String userEmail;

    @NotBlank
    private String roleName;
}
