package com.example.moview.dto.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized
public class AuthRequest {

    private String email;
    private String password;
}
