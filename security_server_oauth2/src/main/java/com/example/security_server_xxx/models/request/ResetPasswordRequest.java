package com.example.security_server_xxx.models.request;

import javax.validation.constraints.NotNull;

public class ResetPasswordRequest {

    @NotNull
    private String newPassword;
    @NotNull
    private String repeatedNewPassword;
    @NotNull
    private String code;

    public ResetPasswordRequest() {}

    public ResetPasswordRequest(String newPassword, String repeatedNewPassword, String code) {
        this.newPassword = newPassword;
        this.repeatedNewPassword = repeatedNewPassword;
        this.code = code;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getRepeatedNewPassword() {
        return repeatedNewPassword;
    }

    public String getCode() {
        return code;
    }
}
