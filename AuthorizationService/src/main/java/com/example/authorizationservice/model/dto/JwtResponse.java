package com.example.authorizationservice.model.dto;

public class JwtResponse {
    private final String token;

    public JwtResponse(final String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
