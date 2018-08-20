package com.example.security.securityclientoauth2.dto;


public class GenerateTokenResponse {
    private String token;

    public GenerateTokenResponse() {}

    public GenerateTokenResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
