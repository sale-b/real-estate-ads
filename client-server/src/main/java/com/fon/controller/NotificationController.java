package com.fon.controller;

import com.fon.model.ResourceRequest;
import com.fon.service.HttpRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notification")
//@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class NotificationController {

    @Autowired
    HttpRequestService requestService;

    @Value("${resource-server.url}")
    private String resourceServerUrl;

    @PostMapping("/page")
    public ResponseEntity<?> getNotifications(@RegisteredOAuth2AuthorizedClient("realestates-client-authorization-code") OAuth2AuthorizedClient authorizedClient, @RequestBody Map requestMap, HttpServletRequest request) {
        return requestService.performRequest(
                ResourceRequest.builder()
                        .httpMethod(HttpMethod.GET)
                        .uri(String.format("%s%s", resourceServerUrl, request.getServletPath()))
                        .body(requestMap)
                        .authorizedClient(authorizedClient)
                        .build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> countUnseenNotifications(@RegisteredOAuth2AuthorizedClient("realestates-client-authorization-code") OAuth2AuthorizedClient authorizedClient, HttpServletRequest request) {
        return requestService.performRequest(
                ResourceRequest.builder()
                        .httpMethod(HttpMethod.GET)
                        .authorizedClient(authorizedClient)
                        .build());
    }

    @PutMapping("/seen/{notificationId}")
    public ResponseEntity<?> markNotificationAsSeen(@RegisteredOAuth2AuthorizedClient("realestates-client-authorization-code") OAuth2AuthorizedClient authorizedClient, @RequestBody Map requestMap, HttpServletRequest request) {
        return requestService.performRequest(
                ResourceRequest.builder()
                        .httpMethod(HttpMethod.GET)
                        .uri(String.format("%s%s", resourceServerUrl, request.getServletPath()))
                        .body(requestMap)
                        .authorizedClient(authorizedClient)
                        .build());
    }

    @PutMapping("/seen/filter/{filterId}")
    public ResponseEntity<?> markAllNotificationsAsSeenForFilter(@RegisteredOAuth2AuthorizedClient("realestates-client-authorization-code") OAuth2AuthorizedClient authorizedClient, @RequestBody Map requestMap, HttpServletRequest request) {
        return requestService.performRequest(
                ResourceRequest.builder()
                        .httpMethod(HttpMethod.GET)
                        .body(requestMap)
                        .uri(String.format("%s%s", resourceServerUrl, request.getServletPath()))
                        .authorizedClient(authorizedClient)
                        .build());
    }

}
