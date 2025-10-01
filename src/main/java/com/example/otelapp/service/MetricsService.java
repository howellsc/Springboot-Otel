package com.example.otelapp.service;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.DoubleHistogram;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MetricsService {

    private final Meter meter;
    private LongCounter requestCounter;
    private DoubleHistogram responseTimeHistogram;
    private AtomicLong activeUsers;

    public MetricsService(Meter meter) {
        this.meter = meter;
        this.activeUsers = new AtomicLong(0);
    }

    @PostConstruct
    public void init() {
        requestCounter = meter
                .counterBuilder("app.requests.total")
                .setDescription("Total number of requests")
                .setUnit("requests")
                .build();

        responseTimeHistogram = meter
                .histogramBuilder("app.response.time")
                .setDescription("Response time in milliseconds")
                .setUnit("ms")
                .build();

        meter
                .gaugeBuilder("app.users.active")
                .setDescription("Number of active users")
                .setUnit("users")
                .buildWithCallback(measurement ->
                        measurement.record(activeUsers.get())
                );
    }

    public void recordRequest(String endpoint, String method) {
        Attributes attributes = Attributes.of(
                AttributeKey.stringKey("endpoint"), endpoint,
                AttributeKey.stringKey("method"), method
        );
        requestCounter.add(1, attributes);
    }

    public void recordResponseTime(double milliseconds, String endpoint) {
        Attributes attributes = Attributes.of(
                AttributeKey.stringKey("endpoint"), endpoint
        );
        responseTimeHistogram.record(milliseconds, attributes);
    }

    public void incrementActiveUsers() {
        activeUsers.incrementAndGet();
    }

    public void decrementActiveUsers() {
        activeUsers.decrementAndGet();
    }

    public long getActiveUsers() {
        return activeUsers.get();
    }
}
