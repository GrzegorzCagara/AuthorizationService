package com.example.security_server_xxx;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@SpringBootApplication
public class SecurityServerXxxApplication {


    public static void main(String[] args) {
        SpringApplication.run(SecurityServerXxxApplication.class, args);
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .basicAuthorization("trusted-app", "secret")
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .errorHandler(new DefaultResponseErrorHandler() {
                    public boolean hasError(ClientHttpResponse response) throws IOException {
                        HttpStatus statusCode = response.getStatusCode();
                        return statusCode.series() == HttpStatus.Series.SERVER_ERROR;
                    }
                })
                .build();
    }


}
