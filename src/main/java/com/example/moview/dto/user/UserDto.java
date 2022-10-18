package com.example.moview.dto.user;

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
    private String name;
    private String birthday;
    private String email;
}