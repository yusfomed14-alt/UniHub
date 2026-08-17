package com.unihub.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import javax.crypto.SecretKey;

@Component

public class JwtUtil {
    @Value("${app.jwt.secret}")
    private String secretKey;

    @Value("${app.jwt.expiration-ms}")
    private Long expirationMs;

    @Value("${app.jwt.refresh-expiration-ms}")

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+expirationMs))
                .signWith(getSigningKey())
                .compact();
    }
    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+expirationMs))
                .signWith(getSigningKey())
                .compact();
    }
    public String extractEmail(String token) {

        return extractClaims(token).getSubject();
    }
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
