package com.fon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class OAuth2AuthorizationServerApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(OAuth2AuthorizationServerApplication.class, args);
    }

}