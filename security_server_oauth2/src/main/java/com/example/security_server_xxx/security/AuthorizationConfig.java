package com.example.security_server_xxx.security;

import com.example.security_server_xxx.services.CustomDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


@Configuration
@EnableAuthorizationServer
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private int accessTokenValiditySeconds = 10000;
    private int refreshTokenValiditySeconds = 30000;


    private String privateKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIIEowIBAAKCAQEA3l5T+qOJl7dqVQTnG1YlwGYQOY1WLWnNQHmzfAIbUZ9ceWN+\n" +
            "+DPSsiFHDgYqXCLkOZ02gzWyVQTWPohXMf7hqB/NIGDJcQng5z/cHJgRysge+Gh0\n" +
            "PWtz0J0bTmuvxlWZq4/eRx/CwqLzMVq3td2WjDdT1Un4EMuJuAQS5kEGBZldLjbT\n" +
            "VgowHp8pRrQQXO7GDK4oubUdKpKbI+hQ5THmvhEd5yo/PeNSXRkKxIGT6ZVKKqd2\n" +
            "7nfQgLWdQzJ0Hnmz/g0bB1IqEOKjmr0yDczMRhqpk3BT3X3D9hj5FF9tATnM4tjT\n" +
            "bZkj6+LyOvNpk+dMkpvfDFOIfzsWqCtnCY41cQIDAQABAoIBAFt49BTDVyNY6N3G\n" +
            "YYcmPetSUNRL5qpRvoQKTCHz/+bopjKtEhrpvn9WHxOOx7gVBksI99arrQs6LSOW\n" +
            "FEBVt9/gDRKHnfXo+WJHK7V7fL4s6kiHN2HYpNCpAAWXWLw+lDM1wFsDnqtQOrgP\n" +
            "uEiQuZA+Zjbz8Z5/C8ztqBSdpOuB8beIf+31AY0emaR8sMMvw7DAXZ3UuyzVVTlR\n" +
            "7OXUHf3iXR4t9IV8zBlqLTUAHtAbPrIRaOJ8BfjO3KMOIkJnmByKfnG2dHcXvZm3\n" +
            "nLL5Zeeki1i1R80jDIdAdv61AY4KUYQzxnu86ZcZWFDw5KI+5YmQXx4q4FhMw1Tx\n" +
            "VeHEysECgYEA8nu1vMy+Etjhjn1UXblZvhueEkdYvRZ5baEg1kYHywur3cofvIWE\n" +
            "lAIE0m4khyKqaIPXF/T5z3BMrDF3PYP0c/bd1Qj2vipKsUVYaTMfmZ6MgF5QnZ/j\n" +
            "xCJgKiXPWEmZfm6httkRks7Av7qQZ89UAWIx1V/r3DXIf5+NbD0iTEkCgYEA6sOT\n" +
            "aSV1QVid92/yoCH1Re0OFw12rcwOwDAWZbk9dmlDoor0lmHK/cRzzzTguMsJxgP8\n" +
            "mnU5g/AusveD6mmxqFRrJ5rQAq5UEDZ7hfgQwQkhhLoDV8CgnwNyyIBkMDn6RlvH\n" +
            "kPdx0XBdoz5eh2SThg8wXNo5BjYfV8jFE3Lqj+kCgYAd2PJ26fDfmwiwpGa1HrKY\n" +
            "PwgHEfcvV0vdHb9QANDGwooGQ/ICH6FBIrp7OImQhMjbEpdwYHw7d4RIkdIupUZ6\n" +
            "28FUjsTohYYqKfIHCP7zxttelb1wmZTMuLqDlaHfKTgWowGO40tRedeqAgnybUfW\n" +
            "38JolGUQ9yHV68rZrvDA2QKBgQCIVG7EPrZq7jU9vcrd5xpyMwldpbnnj3p5bPN0\n" +
            "uy/QFazrNVp4Ae5Jk83U5FYjufTBAwur/qsZzV6xGz+F3z8GC4/2fdaR1/6FM75j\n" +
            "u/yuvMi0NPk/+mLMNMpxWO3Ve7W1KhMoQincgC5AYDWw1l09GpDxOuCmlMN6zOJ1\n" +
            "OuSwEQKBgHquIcgYJyGGUJXEDLesYZmK8twXAY3/xaqfn8phOMHGuw5ljbLGE4RY\n" +
            "D1YzkFMS2uB31m2LKbFFCzO0nQaljPL87zzak47xzsz1asdae/TNgSwgWNJchf6c\n" +
            "1w/L1Vuetfy3k/XUgyUsns8XhdTpqKoTdBFOFjO0TO9nuM1aU9eY\n" +
            "-----END RSA PRIVATE KEY-----\n";

    private String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3l5T+qOJl7dqVQTnG1YlwGYQOY1WLWnNQHmzfAIbUZ9ceWN++DPSsiFHDgYqXCLkOZ02gzWyVQTWPohXMf7hqB/NIGDJcQng5z/cHJgRysge+Gh0PWtz0J0bTmuvxlWZq4/eRx/CwqLzMVq3td2WjDdT1Un4EMuJuAQS5kEGBZldLjbTVgowHp8pRrQQXO7GDK4oubUdKpKbI+hQ5THmvhEd5yo/PeNSXRkKxIGT6ZVKKqd27nfQgLWdQzJ0Hnmz/g0bB1IqEOKjmr0yDczMRhqpk3BT3X3D9hj5FF9tATnM4tjTbZkj6+LyOvNpk+dMkpvfDFOIfzsWqCtnCY41cQIDAQAB-----END PUBLIC KEY-----";

    @Value("${security.oauth2.resource.id}")
    private String resourceId;

    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomDetailsService();
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(this.authenticationManager)
                .tokenServices(tokenServices())
                .tokenStore(tokenStore())
                .accessTokenConverter(accessTokenConverter());
    }


    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {

        oauthServer
                // we're allowing access to the token only for clients with 'ROLE_TRUSTED_CLIENT' authority
                .tokenKeyAccess("hasAuthority('ROLE_TRUSTED_CLIENT')")
                .checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')");

    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()

                .withClient("trusted-app")
                    .authorizedGrantTypes("client_credentials", "password", "refresh_token")
                    .authorities("TRUSTED_CLIENT")
                    .scopes("read", "write")
                    .resourceIds(resourceId)
                    .accessTokenValiditySeconds(30000)
                    .refreshTokenValiditySeconds(30000)
                    .secret(passwordEncoder.encode("secret"))
                .and()
                .withClient("register-app")
                    .authorizedGrantTypes("client_credentials")
                    .authorities("ROLE_REGISTER")
                    .scopes("registerUser")
                    .accessTokenValiditySeconds(10)
                    .refreshTokenValiditySeconds(10)
                    .resourceIds(resourceId)
                    .secret("secret");
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());

    }

//    @Bean
//    public JwtAccessTokenConverter accessTokenConverter() {
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        KeyStoreKeyFactory keyStoreKeyFactory =
//                new KeyStoreKeyFactory(
//                        new ClassPathResource("mykeys.jks"),
//                        "mypass".toCharArray());
//        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("mykeys"));
//        return converter;
//    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(privateKey);
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



}

