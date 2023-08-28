package com.fon.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {

    private Map<String, String> tokenStorage = new HashMap<>();

    public void saveTokens(String accessToken, String refreshToken) {
        tokenStorage.put(accessToken, refreshToken);
    }

    public String getRefreshToken(String accessToken) {
        return tokenStorage.get(accessToken);
    }

    public void removeTokens(String accessToken) {
        tokenStorage.remove(accessToken);
    }

    public String getValidAccessToken(String accessToken, String refreshToken) {
            WebClient webClient = WebClient.create();

            // Encode client credentials for the authorization header
            String credentials = "realestates-client" + ":" + "secret";
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
            String authorizationHeader = "Basic " + encodedCredentials;

            // Prepare the request body with required parameters
            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("grant_type", "refresh_token");
            requestBody.add("refresh_token", refreshToken);

            // Send the POST request to retrieve the access token
            String response = webClient.method(HttpMethod.POST)
                    .uri("http://auth-server:9000/oauth2/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                    .body(BodyInserters.fromFormData(requestBody))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Extract the access token from the response and print it
            String extractedAccessToken = extractAccessToken(response);
            System.out.println("NEW Access Token: " + extractedAccessToken);

        if(tokenStorage.containsKey(accessToken)){
           removeTokens(accessToken);
           saveTokens(extractedAccessToken, refreshToken);
        }
        return extractedAccessToken;
    }

    private String extractAccessToken(String response) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            return jsonNode.get("access_token").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Access token not found or error occurred during parsing
        }
    }

}