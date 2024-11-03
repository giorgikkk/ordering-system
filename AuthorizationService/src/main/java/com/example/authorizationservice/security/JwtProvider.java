package com.example.authorizationservice.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {
    private final String SECRET_KEY;
    private static final long EXPIRATION_TIME = 86400000; // 1 day

    public JwtProvider() {
        SECRET_KEY = generateSecretKey();
    }

    private String generateSecretKey() {
        final SecureRandom secureRandom = new SecureRandom();
        final byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getEncoder().encodeToString(randomBytes);
    }

    public String generateToken(final Authentication authentication) {
        final Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", authentication.getAuthorities());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(authentication.getName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String getUserNameFromJwtToken(final String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(final String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
