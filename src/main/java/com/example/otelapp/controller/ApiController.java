package com.example.otelapp.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final Random random = new Random();

    @GetMapping("/hello")
    public Map<String, String> hello() {
        simulateWork();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello from Spring Boot with OpenTelemetry!");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return response;
    }

    @PostMapping("/process")
    public Map<String, Object> process(@RequestBody(required = false) Map<String, Object> data) {
        simulateWork();
        Map<String, Object> response = new HashMap<>();
        response.put("status", "processed");
        response.put("received", data != null ? data : Map.of());
        return response;
    }

    @GetMapping("/users/login")
    public Map<String, Object> login() {
        simulateWork();
        Map<String, Object> response = new HashMap<>();
        response.put("status", "logged in");
        return response;
    }

    @GetMapping("/users/logout")
    public Map<String, Object> logout() {
        simulateWork();
        Map<String, Object> response = new HashMap<>();
        response.put("status", "logged out");
        return response;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "otel-springboot-app");
        return response;
    }

    private void simulateWork() {
        try {
            Thread.sleep(random.nextInt(50) + 10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
