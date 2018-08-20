package com.example.security.securityclientoauth2.dto;

import javax.validation.constraints.NotNull;

public class GenerateResetPasswordRequest {

    @NotNull
    private String login;



    public GenerateResetPasswordRequest() {}

    public String getLogin() {
        return login;
    }
}
