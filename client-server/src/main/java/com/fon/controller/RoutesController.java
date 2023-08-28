package com.fon.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Controller
public class RoutesController implements ErrorController {
    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String error() {
        return "forward:/";
    }

    @RequestMapping(value = "/auth/login", method = RequestMethod.GET)
    public void login(@RegisteredOAuth2AuthorizedClient("realestates-client-authorization-code") OAuth2AuthorizedClient authorizedClient, HttpServletResponse httpServletResponse) throws JsonProcessingException {
        httpServletResponse.setHeader("Location", "http://client-server:8081/login");
        httpServletResponse.setStatus(302);
    }

    @RequestMapping(value = "/auth/logout", method = RequestMethod.GET)
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        if (request.getCookies() != null) {
            Arrays.stream(request.getCookies())
                    .filter(cookie -> "JSESSIONID".equals(cookie.getName())).forEach(cookie -> {
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            });
        }
        response.setHeader("Location", "http://auth-server:9000/logout");
        response.setStatus(302);
    }

}