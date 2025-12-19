package com.devrenno.appointment.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret.key}")
    private String secretKey;

    private static final String ROLE = "role";

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    private String getToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractRole(String token) {
        return extractClaims(token).get(ROLE, String.class);
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public boolean isValid(String token) {
        boolean isValid = false;
        try {
            Claims claims = extractClaims(token);
            isValid = true;
        } catch (JwtException e) {
            return isValid;
        }
        return isValid;
    }
}
