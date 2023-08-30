package com.fon.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fon.model.ResourceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@Service
public class HttpRequestService {

    @Autowired
    private WebClient webClient;

    @Autowired
    ObjectMapper objectMapper;


    public ResponseEntity<?> performRequest(ResourceRequest request) {
        WebClient.RequestHeadersSpec<?> requestSpec = prepareRequest(request);
        WebClient.ResponseSpec fullResponse = requestSpec.retrieve();
        ResponseEntity<String> responseEntity = fullResponse.toEntity(String.class).block();
        return responseEntity;
    }

    private WebClient.RequestHeadersSpec<?> prepareRequest(ResourceRequest request) {
        WebClient.RequestBodySpec requestSpec = webClient.method(request.getHttpMethod()).uri(request.getUri());

        if ((request.getHttpMethod() == HttpMethod.POST || request.getHttpMethod() == HttpMethod.PUT) && request.getBody() != null) {
            requestSpec.bodyValue(request.getBody());
        }

        if (request.getAuthorizedClient() != null) {
            requestSpec.attributes(oauth2AuthorizedClient(request.getAuthorizedClient()));
        }

        return requestSpec;
    }
}
