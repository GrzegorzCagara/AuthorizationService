package com.example.security_server_xxx.services;

import com.example.security_server_xxx.entity.Users;
import com.example.security_server_xxx.models.request.ResetPasswordRequest;

public interface UserService {

    Users registerUser(Users account);
    Users findAccountByUsername(String username);
    String generateResetToken(String login);
    String ifValidTokenChangePassword(ResetPasswordRequest token);
}
