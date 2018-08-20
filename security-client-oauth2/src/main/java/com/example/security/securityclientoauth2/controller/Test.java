package com.example.security.securityclientoauth2.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
public class Test {


    //@PreAuthorize("hasRole('DUPA')")
    @Secured("ROLE_USER")
    @GetMapping("/api/test")
    public void test(Principal principal) {

        System.out.println("TESTES");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Test: " + principal.getName());
        System.out.println("AUTHL " + authentication.getName());
        System.out.println("AUTORITIS: " + authentication.getAuthorities());
    }
}
