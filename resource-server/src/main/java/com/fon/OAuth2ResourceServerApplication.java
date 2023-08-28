package com.fon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OAuth2ResourceServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(OAuth2ResourceServerApplication.class, args);
    }
}
