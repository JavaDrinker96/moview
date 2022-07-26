package com.example.moview.moview.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

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
}