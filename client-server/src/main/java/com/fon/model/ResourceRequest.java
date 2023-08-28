package com.fon.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

import java.util.Map;

@Data
@Builder
public class ResourceRequest {
    private String uri;
    private HttpMethod httpMethod;
    private Object body;
    private OAuth2AuthorizedClient authorizedClient;
}
