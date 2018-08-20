package com.example.security.securityclientoauth2.dto;

import com.example.security.securityclientoauth2.dto.enums.Role;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class Account {

    @NotNull
    private String login;
    @NotNull
    private String password;
    @NotNull
    private List<Role> roles = new ArrayList<>();

    public Account() {}

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public List<Role> getRoles() {
        return roles;
    }
}
