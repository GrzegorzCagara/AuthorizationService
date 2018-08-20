package com.example.security.securityclientoauth2.dto;

import javax.validation.constraints.NotNull;

public class TokenRequest {

    @NotNull
    private String token;

    public TokenRequest() {}

    public String getToken() {
        return token;
    }
}
