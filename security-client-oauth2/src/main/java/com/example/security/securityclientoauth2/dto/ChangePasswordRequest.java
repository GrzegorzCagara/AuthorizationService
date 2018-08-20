package com.example.security.securityclientoauth2.dto;

import javax.validation.constraints.NotNull;

public class ChangePasswordRequest {

    @NotNull
    private String oldPassword;
    @NotNull
    private String newPassword;

    public ChangePasswordRequest() {}

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    @Override
    public String toString() {
        return "ChangePasswordRequest{" +
                "oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
