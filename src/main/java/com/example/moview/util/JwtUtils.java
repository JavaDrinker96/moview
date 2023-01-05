package com.example.moview.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class JwtUtils {

    private static final String AUTHORITIES_CLAIM_NAME = "authorities";

    @Value("${jwt.secret}")
    private String jwtSigningKey;

    @Value("${jwt.expiration-min.access}")
    private Integer accessExpirationMinutes;

    @Value("${jwt.expiration-min.refresh}")
    private Integer refreshExpirationMinutes;


    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails, accessExpirationMinutes);
    }

    public String generateAccessToken(UserDetails userDetails, Map<String, Object> claims) {
        return createToken(claims, userDetails, accessExpirationMinutes);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails, refreshExpirationMinutes);
    }

    public String generateRefreshToken(UserDetails userDetails, Map<String, Object> claims) {
        return createToken(claims, userDetails, refreshExpirationMinutes);
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Boolean hasClaim(String token, String claimName) {
        Claims claims = extractAllClaims(token);
        return Objects.nonNull(claims.get(claimName));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(jwtSigningKey).parseClaimsJws(token).getBody();
    }

    private String createToken(Map<String, Object> claims, UserDetails userDetails, Integer expirationMinutes) {
        return Jwts.builder().setClaims(claims)
                .setSubject(userDetails.getUsername())
                .claim(AUTHORITIES_CLAIM_NAME, userDetails.getAuthorities())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(expirationMinutes)))
                .signWith(SignatureAlgorithm.HS256, jwtSigningKey).compact();
    }

}