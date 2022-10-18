package com.example.moview.dto.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "of")
public class TokenResponse {

    private final String accessToken;

    private final String refreshToken;

    private final long expiresOn;

}
