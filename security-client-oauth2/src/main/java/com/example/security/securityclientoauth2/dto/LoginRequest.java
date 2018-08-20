package com.example.security.securityclientoauth2.dto;

import javax.validation.constraints.NotNull;

public class LoginRequest {

    @NotNull
    private String login;
    @NotNull
    private String password;

    public LoginRequest() {}

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
