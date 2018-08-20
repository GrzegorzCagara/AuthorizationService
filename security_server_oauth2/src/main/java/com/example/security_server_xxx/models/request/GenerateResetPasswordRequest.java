package com.example.security_server_xxx.models.request;

import javax.validation.constraints.NotNull;

public class GenerateResetPasswordRequest {

    @NotNull
    private String login;



    public GenerateResetPasswordRequest() {}

    public String getLogin() {
        return login;
    }
}
