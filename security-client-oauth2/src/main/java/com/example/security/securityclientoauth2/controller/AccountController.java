package com.example.security.securityclientoauth2.controller;

import com.example.security.securityclientoauth2.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/account")
public class AccountController {

    private static final String SERVER_URL = "http://localhost:8080";

    @Autowired
    private RestTemplate restTemplate;
    @Qualifier("authRest")
    @Autowired
    private RestTemplate authRest;
    @Autowired
    private TokenStore tokenStore;


    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest req) {
        String loginUrl = SERVER_URL + "/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("grant_type", "password");
        map.add("username", loginRequest.getLogin());
        map.add("password", loginRequest.getPassword());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<TokenResponse> response = authRest.postForEntity(loginUrl, request, TokenResponse.class);
        if (response.getStatusCodeValue() == 400) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/register")
    public ResponseEntity registerAccount(@Valid @RequestBody Account registerReq) {
        String registerUrl = SERVER_URL + "/api/account/register";

        ResponseEntity<String> response = restTemplate.postForEntity(registerUrl, registerReq, String.class);
        if (response.getStatusCode().equals(HttpStatus.CONFLICT)) {
            return new ResponseEntity<>("EMAIL_ALREADY_EXIST", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/refresh/token")
    public ResponseEntity<TokenResponse> refreshToken(@Valid @RequestBody TokenRequest tokenRequest) {
        String loginUrl = SERVER_URL + "/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("grant_type", "refresh_token");
        map.add("client_id", "trusted-app");
        map.add("refresh_token", tokenRequest.getToken());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(loginUrl, request, TokenResponse.class);
        if (response.getStatusCodeValue() != 200) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/changePassword")
    public ResponseEntity changePassword(@Valid @RequestBody ChangePasswordRequest request,
                                         @RequestHeader("Authorization") String authHeader) {
        String changePasswordUrl = SERVER_URL + "/api/account/changePassword";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<ChangePasswordRequest> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(changePasswordUrl, httpEntity, String.class);
        switch (response.getStatusCode()) {
            case BAD_REQUEST:
                return new ResponseEntity<>(response.getBody(), HttpStatus.BAD_REQUEST);
            case UNAUTHORIZED:
                return new ResponseEntity<>(response.getBody(), HttpStatus.UNAUTHORIZED);
            case NOT_FOUND:
                return new ResponseEntity<>(response.getBody(), HttpStatus.NOT_FOUND);
            case OK:
                return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
            default:
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/generateResetCode")
    public ResponseEntity generateResetCode(@Valid @RequestBody GenerateResetPasswordRequest request) {
        String changePasswordUrl = SERVER_URL + "/api/account/generateResetCode";
        ResponseEntity<GenerateTokenResponse> response = restTemplate.postForEntity(changePasswordUrl, request, GenerateTokenResponse.class);
        GenerateTokenResponse body = response.getBody();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity resetPassword(@RequestBody ResetPasswordRequest request) {
        String changePasswordUrl = SERVER_URL + "/api/account/resetPassword";
        ResponseEntity<String> response = restTemplate.postForEntity(changePasswordUrl, request, String.class);
        switch (response.getStatusCode()) {
            case OK:
                return new ResponseEntity<>(HttpStatus.OK);
            case BAD_REQUEST:
                return new ResponseEntity<>(response.getBody(), HttpStatus.BAD_REQUEST);
            default:
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
