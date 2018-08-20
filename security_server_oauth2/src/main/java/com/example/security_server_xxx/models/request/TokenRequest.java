package com.example.security_server_xxx.models.request;

import javax.validation.constraints.NotNull;

public class TokenRequest {

    @NotNull
    private String token;

    public TokenRequest() {}

    public String getToken() {
        return token;
    }
}
