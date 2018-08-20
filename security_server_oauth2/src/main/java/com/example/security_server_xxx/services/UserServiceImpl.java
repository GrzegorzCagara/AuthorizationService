package com.example.security_server_xxx.services;

import com.example.security_server_xxx.entity.PasswordResetToken;
import com.example.security_server_xxx.entity.Users;
import com.example.security_server_xxx.models.request.ResetPasswordRequest;
import com.example.security_server_xxx.repositories.AccountRepo;
import com.example.security_server_xxx.repositories.PasswordTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PasswordTokenRepository passwordTokenRepository;

    @Override
    public Users findAccountByUsername(String username) {
        Optional<Users> account = accountRepo.findByLogin( username );
        if ( account.isPresent() ) {
            return account.get();
        } else {
            throw new UsernameNotFoundException(String.format("Username[%s] not found", username));
        }
    }
    @Override
    public Users registerUser(Users account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepo.save(account);
    }

    @Override
    public String generateResetToken(String login) {
        Users user = findAccountByUsername(login);
        String token = UUID.randomUUID().toString();
        createPasswordResetTokenForUser(user, token);
        return token;
    }


    @Override
    public String ifValidTokenChangePassword(ResetPasswordRequest request) {
        PasswordResetToken passToken =
                passwordTokenRepository.findByToken(request.getCode());
        if (passToken == null) {
            return "INVALID_TOKEN";
        }
        if (((passToken.getExpiryDate() + PasswordResetToken.getEXPIRATION()) - System.currentTimeMillis()) <= 0) {
            return "EXPIRED_TOKEN";
        }
        Users user = passToken.getUsers();
        if (user == null) {
            return "NOT_FOUND_USER";
        }
        user.setPassword(request.getNewPassword());
        registerUser(user);
        passwordTokenRepository.delete(passToken);
        return null;
    }
    private void createPasswordResetTokenForUser(Users user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
    }

    @Transactional
    public boolean removeAuthenticatedAccount() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users acct = findAccountByUsername(username);
        int del = accountRepo.deleteUsersById(acct.getId());
        return del > 0;
    }
}
