package com.fon.controller;

import com.fon.model.ResourceRequest;
import com.fon.service.HttpRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    HttpRequestService requestService;

    @Value("${resource-server.url}")
    private String resourceServerUrl;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserByEmail(@RegisteredOAuth2AuthorizedClient("realestates-client-authorization-code") OAuth2AuthorizedClient authorizedClient, HttpServletRequest request) {

        return requestService.performRequest(
                ResourceRequest.builder()
                        .httpMethod(HttpMethod.GET)
                        .uri(String.format("%s%s%s/email", resourceServerUrl, request.getServletPath(), authorizedClient.getPrincipalName()))
                        .authorizedClient(authorizedClient)
                        .build());

    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserById(@RegisteredOAuth2AuthorizedClient("realestates-client-authorization-code") OAuth2AuthorizedClient authorizedClient, HttpServletRequest request) {

        return requestService.performRequest(
                ResourceRequest.builder()
                        .httpMethod(HttpMethod.GET)
                        .uri(String.format("%s%s", resourceServerUrl, request.getServletPath()))
                        .authorizedClient(authorizedClient)
                        .build());
    }
}