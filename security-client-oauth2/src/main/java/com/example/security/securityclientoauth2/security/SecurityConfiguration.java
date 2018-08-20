package com.example.security.securityclientoauth2.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

//    @Override
//    public void configure(HttpSecurity http) throws Exception
//    {
//        http.addFilterBefore(corsFilter(), ChannelProcessingFilter.class).logout().deleteCookies("JESSIONID")
//                .logoutUrl("/api/logout").logoutSuccessHandler(logoutSuccessHandler()).and().formLogin().loginPage("/login")
//                .loginProcessingUrl("/api/login").failureHandler(authenticationFailureHandler())
//                .successHandler(authenticationSuccessHandler()).and().csrf().disable().exceptionHandling()
//                .authenticationEntryPoint(authenticationEntryPoint()).accessDeniedHandler(accessDeniedHandler());
//    }
//
//    /**
//     * @return Custom {@link AuthenticationFailureHandler} to send suitable response to REST clients in the event of a
//     *         failed authentication attempt.
//     */
//    @Bean
//    public AuthenticationFailureHandler authenticationFailureHandler()
//    {
//        return new RestAuthenticationFailureHandler();
//    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/api/account/login", "/api/account/register", "/api/account/refresh/token", "/api/account/generateResetCode",
                "/api/account/resetPassword");
    }
}
