package com.example.security.securityclientoauth2.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableResourceServer
public class ResourceConfig extends ResourceServerConfigurerAdapter {

    @Value("${security.oauth2.resource.id}")
    private String resourceId;


    private String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3l5T+qOJl7dqVQTnG1YlwGYQOY1WLWnNQHmzfAIbUZ9ceWN++DPSsiFHDgYqXCLkOZ02gzWyVQTWPohXMf7hqB/NIGDJcQng5z/cHJgRysge+Gh0PWtz0J0bTmuvxlWZq4/eRx/CwqLzMVq3td2WjDdT1Un4EMuJuAQS5kEGBZldLjbTVgowHp8pRrQQXO7GDK4oubUdKpKbI+hQ5THmvhEd5yo/PeNSXRkKxIGT6ZVKKqd27nfQgLWdQzJ0Hnmz/g0bB1IqEOKjmr0yDczMRhqpk3BT3X3D9hj5FF9tATnM4tjTbZkj6+LyOvNpk+dMkpvfDFOIfzsWqCtnCY41cQIDAQAB-----END PUBLIC KEY-----";


    // To allow the rResourceServerConfigurerAdapter to understand the token,
    // it must share the same characteristics with AuthorizationServerConfigurerAdapter.
    // So, we must wire it up the beans in the ResourceServerSecurityConfigurer.
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources
                .resourceId(resourceId)
                .tokenServices(tokenServices())
                .tokenStore(tokenStore())
        .authenticationEntryPoint(customAuthEntryPoint())
        .accessDeniedHandler(customDeniedHandler());
    }
    @Bean
    public AuthenticationEntryPoint customAuthEntryPoint(){
        return new AuthFailureHandler();
    }

    @Bean
    public AccessDeniedHandler customDeniedHandler(){
        return new AccessDeniedHandlerImpl();
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http

                //.requestMatcher(new OAuthRequestedMatcher())
                .csrf().disable()
                .anonymous().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
               // .antMatchers("/api/test").hasRole("DUPA")
        .antMatchers("/api/**").authenticated();
        ;

                // when restricting access to 'Roles' you must remove the "ROLE_" part role
                // for "ROLE_USER" use only "USER"
//                .antMatchers("/account/api/hello").access("hasAnyRole('USER')")
//                .antMatchers("/api/me").hasAnyRole("USER", "ADMIN");
//                .antMatchers("/account/api/admin").hasRole("SRAKA")
//                // use the full name when specifying authority access
//                .antMatchers("/api/registerUser").hasAuthority("ROLE_REGISTER")
//                // restricting all access to /api/** to authenticated users
                //.antMatchers("/api/**").authenticated();
    }





    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());

    }


    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
       // converter.setSigningKey(privateKey);
        converter.setVerifierKey(publicKey);
        return converter;
    }
    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setTokenEnhancer(accessTokenConverter());
        return defaultTokenServices;
    }



    private static class OAuthRequestedMatcher implements RequestMatcher {
        public boolean matches(HttpServletRequest request) {
            System.out.println("CALLING API");
            // Determine if the resource called is "/api/**"
            String path = request.getServletPath();
            if ( path.length() >= 5 ) {
                path = path.substring(0, 5);
                boolean isApi = path.equals("/api/");
                return isApi;
            } else return false;
        }
    }

}
