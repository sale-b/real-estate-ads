package com.fon.controller;

import com.fon.model.ResourceRequest;
import com.fon.service.HttpRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/filter")
//@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class FilterController {


    @Autowired
    HttpRequestService requestService;

    @Value("${resource-server.url}")
    private String resourceServerUrl;

    @PostMapping()
    public ResponseEntity<?> save(@RequestBody Map requestMap, HttpServletRequest request) {
        return requestService.performRequest(
                ResourceRequest.builder()
                        .httpMethod(HttpMethod.POST)
                        .body(requestMap)
                        .uri(String.format("%s%s", resourceServerUrl, request.getServletPath()))
                        .build());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> findByUserId(HttpServletRequest request) {
        return requestService.performRequest(
                ResourceRequest.builder()
                        .httpMethod(HttpMethod.GET)
                        .uri(String.format("%s%s", resourceServerUrl, request.getServletPath()))
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(HttpServletRequest request) {
        return requestService.performRequest(
                ResourceRequest.builder()
                        .httpMethod(HttpMethod.DELETE)
                        .uri(String.format("%s%s", resourceServerUrl, request.getServletPath()))
                        .build());
    }


}
