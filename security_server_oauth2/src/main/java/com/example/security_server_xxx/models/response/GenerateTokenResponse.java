package com.example.security_server_xxx.models.response;


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
