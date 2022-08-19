package com.example.moview.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@AllArgsConstructor
@Jacksonized
public class AuthResponse {

    private String token;
}
