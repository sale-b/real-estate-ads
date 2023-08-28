package com.fon.controller;

import com.fon.model.ResourceRequest;
import com.fon.service.HttpRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
@RequestMapping("/api/v1/real-estate")
public class RealEstateController {

    @Autowired
    HttpRequestService requestService;

    @Value("${resource-server.url}")
    private String resourceServerUrl;

    @Autowired
    private WebClient webClient;

    @GetMapping("/autocomplete-fields")
    public ResponseEntity<?> getAutocompleteValues(HttpServletRequest request) {
        return requestService.performRequest(
                ResourceRequest.builder()
                        .httpMethod(HttpMethod.GET)
                        .uri(resourceServerUrl + request.getServletPath())
                        .build());
    }

    @PutMapping()
    public ResponseEntity saveRealEstate(@ModelAttribute("model") String request,
                                         @RequestParam(value = "images", required = false) List<MultipartFile> fileList, @RegisteredOAuth2AuthorizedClient("realestates-client-authorization-code") OAuth2AuthorizedClient authorizedClient) throws IOException {

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("model", request);
        if (fileList != null && !fileList.isEmpty()) {
            for (MultipartFile file : fileList) {
                builder.part("images", file.getResource());
            }
        }

        WebClient.ResponseSpec fullResponse = webClient.put()
                .uri(resourceServerUrl + "/api/v1/real-estate")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve();
        String jsonResponse = Objects.requireNonNull(fullResponse.bodyToMono(String.class).block());
        HttpStatus statusCode = fullResponse.toBodilessEntity().block().getStatusCode();

        return ResponseEntity.status(statusCode).body(jsonResponse);
    }

    @PostMapping("/page")
    public ResponseEntity<?> getRealEstates(@RequestBody Map requestMap, HttpServletRequest request) {
        return requestService.performRequest(
                ResourceRequest.builder()
                        .httpMethod(HttpMethod.POST)
                        .uri(resourceServerUrl + request.getServletPath())
                        .body(requestMap)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findAll(HttpServletRequest request) {
        return requestService.performRequest(
                ResourceRequest.builder()
                        .httpMethod(HttpMethod.GET)
                        .uri(resourceServerUrl + request.getServletPath())
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@RegisteredOAuth2AuthorizedClient("realestates-client-authorization-code") OAuth2AuthorizedClient authorizedClient, HttpServletRequest request) {
        return requestService.performRequest(
                ResourceRequest.builder()
                        .httpMethod(HttpMethod.DELETE)
                        .uri(resourceServerUrl + request.getServletPath())
                        .authorizedClient(authorizedClient)
                        .build());
    }

}