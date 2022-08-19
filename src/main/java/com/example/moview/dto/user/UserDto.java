package com.example.moview.dto.user;

import com.example.moview.model.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.Set;

@Getter
@Setter
@Builder
@Jacksonized
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String birthday;
    private String email;
    private Set<Role> roles;
}