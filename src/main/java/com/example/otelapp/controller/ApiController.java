package com.example.otelapp.controller;

import com.example.otelapp.service.MetricsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final MetricsService metricsService;
    private final Random random = new Random();

    public ApiController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping("/hello")
    public Map<String, String> hello() {
        long startTime = System.currentTimeMillis();

        metricsService.recordRequest("/api/hello", "GET");

        simulateWork();

        long duration = System.currentTimeMillis() - startTime;
        metricsService.recordResponseTime(duration, "/api/hello");

        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello from Spring Boot with OpenTelemetry!");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return response;
    }

    @PostMapping("/process")
    public Map<String, Object> process(@RequestBody(required = false) Map<String, Object> data) {
        long startTime = System.currentTimeMillis();

        metricsService.recordRequest("/api/process", "POST");

        simulateWork();

        long duration = System.currentTimeMillis() - startTime;
        metricsService.recordResponseTime(duration, "/api/process");

        Map<String, Object> response = new HashMap<>();
        response.put("status", "processed");
        response.put("processingTime", duration + "ms");
        response.put("received", data != null ? data : Map.of());
        return response;
    }

    @GetMapping("/users/login")
    public Map<String, Object> login() {
        long startTime = System.currentTimeMillis();

        metricsService.recordRequest("/api/users/login", "GET");
        metricsService.incrementActiveUsers();

        simulateWork();

        long duration = System.currentTimeMillis() - startTime;
        metricsService.recordResponseTime(duration, "/api/users/login");

        Map<String, Object> response = new HashMap<>();
        response.put("status", "logged in");
        response.put("activeUsers", metricsService.getActiveUsers());
        return response;
    }

    @GetMapping("/users/logout")
    public Map<String, Object> logout() {
        long startTime = System.currentTimeMillis();

        metricsService.recordRequest("/api/users/logout", "GET");
        metricsService.decrementActiveUsers();

        simulateWork();

        long duration = System.currentTimeMillis() - startTime;
        metricsService.recordResponseTime(duration, "/api/users/logout");

        Map<String, Object> response = new HashMap<>();
        response.put("status", "logged out");
        response.put("activeUsers", metricsService.getActiveUsers());
        return response;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "otel-springboot-app");
        response.put("activeUsers", metricsService.getActiveUsers());
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
