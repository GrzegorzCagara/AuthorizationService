package com.example.security_server_xxx.models.request;

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
