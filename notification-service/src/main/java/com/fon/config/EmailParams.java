package com.fon.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "email.properties")
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailParams {

    private String SMTP_SERVER;
    private String SMTP_PORT;
    private String USERNAME;
    private String PASSWORD;
    private String EMAIL_FROM;
    private String EMAIL_SUBJECT;
    private String EMAIL_TO_CC;

}