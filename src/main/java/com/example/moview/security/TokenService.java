package com.example.moview.security;

import com.example.moview.dto.security.TokenResponse;
import com.example.moview.exception.NotValidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class TokenService {

    @Value("${app.auth.token.access-expiration-ms}")
    private String accessTokenExpirationMs;

    @Value("${app.auth.token.refresh-expiration-ms}")
    private String refreshTokenExpirationMs;

    @Value("${app.auth.token.secret}")
    private String tokenSecret;

    public TokenResponse generateTokensByAuthentication(final Authentication authentication) {

        final String accessToken = buildTokenByAuthentication(authentication, false);
        final String refreshToken = buildTokenByAuthentication(authentication, true);

        return TokenResponse.of(accessToken, refreshToken, getAccessTokenExpiresMs());
    }

    public TokenResponse refreshTokens(final String refreshToken) {

        validateToken(refreshToken);
        final Long userId = getUserIdFromToken(refreshToken);

        final String newAccessToken = buildToken(userId, false);
        final String newRefreshToken = buildToken(userId, true);

        return TokenResponse.of(newAccessToken, newRefreshToken, getAccessTokenExpiresMs());
    }

    public String generateAccessToken(final Authentication authentication) {

        return buildTokenByAuthentication(authentication, false);
    }

    public void validateToken(final String authToken) {
        try {
            Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(authToken);
        } catch (JwtException ex) {
            throw new NotValidTokenException(ex.getMessage());
        }
    }

    private long getAccessTokenExpiresMs() {
        return Long.parseLong(accessTokenExpirationMs);
    }

    private String buildTokenByAuthentication(final Authentication authentication, final boolean isRefresh) {

        final UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return buildToken(userPrincipal.getId(), isRefresh);
    }

    public Long getUserIdFromToken(final String token) {
        final Claims claims = Jwts.parser()
                .setSigningKey(tokenSecret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    //TODO: fix it

    private String buildToken(final Long userId, final boolean isRefresh) {

        final Date now = new Date();
        final Long expirationMs = Long.valueOf(isRefresh ? refreshTokenExpirationMs : accessTokenExpirationMs);
        final Date expireDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(Long.toString(userId))
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }
}
