package com.fon.controller;

import com.fon.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class BffController {

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private WebClient webClient;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/authenticate")
    public void authenticate(@RegisteredOAuth2AuthorizedClient("realestates-client-authorization-code") OAuth2AuthorizedClient authorizedClient,
                             HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
        if (authorizedClient != null) {
            // Generate a token
//            String token = UUID.randomUUID().toString();
//            sessionMap.put(token, authorizedClient);

            // Redirect the user to the SPA with the token in the Authorization header
//            HttpHeaders headers = new HttpHeaders();
            String accessToken = (authorizedClient.getAccessToken().getTokenValue());
            URI redirectUri = URI.create("http://localhost/");

            response.setStatus(HttpStatus.FOUND.value());
            response.setHeader(HttpHeaders.LOCATION, redirectUri.toString());
            response.setHeader("Access-Token", accessToken);
            tokenService.saveTokens(accessToken, authorizedClient.getRefreshToken().getTokenValue());
//            response.setHeader("validation", validateAccessToken(token) ? "valid" : "invalid");
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
        } catch (OAuth2AuthorizationException ex) {
            if (ex.getError().getErrorCode().equals("invalid_grant")) {
                // Invalid grant, redirect to login page
                response.sendRedirect("/oauth2/authorization/realestates-client-oidc");
                return;
            }
            // Handle other OAuth2 authorization exceptions if needed
        }
    }

}