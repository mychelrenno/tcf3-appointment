package com.devrenno.appointment.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    
    @Value("${jwt.secret.key}")
    private String secretKey;

    private static final String ROLE = "role";

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public Claims extractClaims(String token) {
        try {
            // Usando a mesma API que o user service para garantir compatibilidade
            return Jwts.parser()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            logger.error("Erro ao extrair claims do token: {}", e.getMessage());
            throw e;
        }
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
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        
        try {
            Claims claims = extractClaims(token);
            
            // Verifica se o token não está expirado
            if (claims.getExpiration() != null && claims.getExpiration().before(new Date())) {
                return false;
            }
            
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
