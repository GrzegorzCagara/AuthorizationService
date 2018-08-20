package com.example.security_server_xxx.services;


import com.example.security_server_xxx.models.CustomUserDetails;
import com.example.security_server_xxx.entity.Users;
import com.example.security_server_xxx.repositories.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepo accountRepo;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Users> optionalUser = accountRepo.findByLogin(login);
        optionalUser.orElseThrow(() -> new UsernameNotFoundException("Login not found"));
        return optionalUser.map(users -> new CustomUserDetails(users)).get();
    }




}
