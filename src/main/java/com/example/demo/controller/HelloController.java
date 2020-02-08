package com.example.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
public class HelloController {

    @Value("${security.oauth2.client.access-token-uri}")
    private String accessTokenUri;

    @RequestMapping("/hello")
    public String hello(){
        return "hello world";
    }

    @RequestMapping("/redirect") //获取授权码时的回调地址，使用获得的授权码获取access_token
    public Map get(@RequestParam(value = "code") String code){
        OkHttpClient httpClient=new OkHttpClient();

        RequestBody requestBody=new FormBody.Builder()
                .add("grant_type","authorization_code")
                .add("client","user")
                .add("redirect_uri","http://localhost:8082/redirect")
                .add("code",code)
                .build();

        Request request=new Request.Builder()
                .url(accessTokenUri)
                .post(requestBody)
                .addHeader("Authorization","Basic dXNlcjoxMjM0NTY=")
                .build();

        Map result=null;

        try {
            Response response=httpClient.newCall(request).execute();
            System.out.println(response);

            ObjectMapper objectMapper=new ObjectMapper();
            result=objectMapper.readValue(Objects.requireNonNull(response.body()).string(),Map.class);

            System.out.println("access_token："+result.get("access_token"));
            System.out.println("token_type："+result.get("token_type"));
            System.out.println("refresh_token："+result.get("refresh_token"));
            System.out.println("expires_in："+result.get("expires_in"));
            System.out.println("scope："+result.get("scope"));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return result;
    }

    @RequestMapping("/parse")
    public Object parse(Authentication authentication){
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails=(OAuth2AuthenticationDetails) authentication.getDetails();
        String token=oAuth2AuthenticationDetails.getTokenValue();

        Jwt jwt=JwtHelper.decode(token);
        String claims=jwt.getClaims();
        String encoded=jwt.getEncoded();
        System.out.println("claims 原始信息："+claims);
        System.out.println("access token编码信息："+encoded);

        return JwtHelper.decode(token);
    }
}