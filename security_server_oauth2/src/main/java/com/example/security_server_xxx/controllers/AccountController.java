package com.example.security_server_xxx.controllers;



import com.example.security_server_xxx.entity.Users;
import com.example.security_server_xxx.models.request.*;
import com.example.security_server_xxx.models.response.GenerateTokenResponse;
import com.example.security_server_xxx.models.response.TokenResponse;
import com.example.security_server_xxx.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private static final String SERVER_URL = "http://localhost:8080";

    @Autowired
    UserService userService;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    TokenStore tokenStore;
    @Autowired
    PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public ResponseEntity registerAccount(@Valid @RequestBody Users account) {
        try {
            userService.registerUser(account);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("EMAIL_ALREADY_EXIST", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
//
//    @PreAuthorize("isFullyAuthenticated()")
//    @DeleteMapping("/api/account/remove")
//    public ResponseEntity<GeneralController.RestMsg> removeAccount(Principal principal){
//        boolean isDeleted = userService.removeAuthenticatedAccount();
//        if ( isDeleted ) {
//            return new ResponseEntity<>(
//                    new GeneralController.RestMsg(String.format("[%s] removed.", principal.getName())),
//                    HttpStatus.OK
//            );
//        } else {
//            return new ResponseEntity<GeneralController.RestMsg>(
//                    new GeneralController.RestMsg(String.format("An error ocurred while delete [%s]", principal.getName())),
//                    HttpStatus.BAD_REQUEST
//            );
//        }
//    }


    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        String loginUrl = SERVER_URL + "/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);



        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("grant_type", "password");
        map.add("username", loginRequest.getLogin());
        map.add("password", loginRequest.getPassword());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(loginUrl, request, TokenResponse.class);
        if (response.getStatusCodeValue() == 400) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
    }

    @PostMapping("/refresh/token")
    public ResponseEntity<TokenResponse> refreshToken(@Valid @RequestBody TokenRequest tokenRequest) {

        String loginUrl = SERVER_URL + "/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
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


    @PostMapping("/changePassword")
    public ResponseEntity changePassword(@Valid @RequestBody ChangePasswordRequest request,
                                         @RequestHeader("Authorization") String authHeader) {
        if (!isHeaderContainingBearer(authHeader)) {
            return new ResponseEntity("WONG_BEARER", HttpStatus.BAD_REQUEST);
        };
        String tokenValue = authHeader.replace("Bearer" , "").trim();
        OAuth2Authentication oAuth2Authentication = null;
        try {
            oAuth2Authentication = tokenStore.readAuthentication(tokenValue);
        } catch (Exception e) {
            return new ResponseEntity("WRONG_TOKEN", HttpStatus.UNAUTHORIZED);
        }

        String login = oAuth2Authentication.getName();
        Users user = userService.findAccountByUsername(login);
        if (user == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            user.setPassword(request.getNewPassword());
            userService.registerUser(user);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity("NOT_MATCHED_PASSWORDS", HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/generateResetCode")
    public ResponseEntity<GenerateTokenResponse> generateResetCode(@Valid @RequestBody GenerateResetPasswordRequest request) {
        String token = userService.generateResetToken(request.getLogin());
        return new ResponseEntity<>(new GenerateTokenResponse(token), HttpStatus.OK);
    }


    @PostMapping("/resetPassword")
    public ResponseEntity resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        if (!request.getRepeatedNewPassword().equals(request.getNewPassword())) {
            return new ResponseEntity("NOT_MATCHED_PASSWORDS", HttpStatus.BAD_REQUEST);
        }
        String token = userService.ifValidTokenChangePassword(request);
        if (token != null) {
            return new ResponseEntity(token, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }


    private boolean isHeaderContainingBearer(String header) {
        System.out.println("HEADER: " + header);
        if (header == null) {
            return false;
        }
        String bearer = header.substring(0, 6);
        return bearer.equals("Bearer");
    }

}
