package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class JwtTokenStoreConfig {

    @Bean
    public JwtAccessTokenConverter initJwtAccessTokenConverter(){
        JwtAccessTokenConverter jwtAccessTokenConverter=new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey("sign123456");

        return jwtAccessTokenConverter;
    }

    @Bean
    public JwtTokenStore initJwtTokenStore(){
        return new JwtTokenStore(initJwtAccessTokenConverter());
    }
}
