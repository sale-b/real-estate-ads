package com.fon.controller;

import com.fon.model.ResourceRequest;
import com.fon.service.HttpRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/city-subregion")
public class CitySubregionController {

    @Autowired
    HttpRequestService requestService;

    @Value("${resource-server.url}")
    private String resourceServerUrl;

    @GetMapping("/{id}")
    public ResponseEntity<?> findAll(HttpServletRequest request) {
        return requestService.performRequest(ResourceRequest.builder()
                .httpMethod(HttpMethod.GET)
                .uri(resourceServerUrl + request.getServletPath())
                .build());
    }


    @PostMapping()
    public ResponseEntity<?> findByCityIdIn(@RequestBody List<Map<String, Object>> requestData, HttpServletRequest request) {
        return requestService.performRequest(
                ResourceRequest.builder()
                        .httpMethod(HttpMethod.POST)
                        .body(requestData)
                        .uri(resourceServerUrl + request.getServletPath())
                        .build());
    }
}
