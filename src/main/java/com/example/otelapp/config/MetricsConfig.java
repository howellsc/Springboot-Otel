package com.example.otelapp.config;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.exporter.otlp.http.metrics.OtlpHttpMetricExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static io.opentelemetry.semconv.ResourceAttributes.SERVICE_NAME;

@Configuration
public class MetricsConfig {

    @Value("${otel.exporter.otlp.endpoint:http://localhost:4318/v1/metrics}")
    private String otlpEndpoint;

    @Bean
    public OpenTelemetry openTelemetry() {
        Resource resource = Resource.getDefault()
                .merge(Resource.create(Attributes.of(SERVICE_NAME, "otel-springboot-app")));

        OtlpHttpMetricExporter otlpExporter = OtlpHttpMetricExporter.builder()
                .setEndpoint(otlpEndpoint)
                .build();

        SdkMeterProvider meterProvider = SdkMeterProvider.builder()
                .registerMetricReader(
                        PeriodicMetricReader.builder(otlpExporter)
                                .setInterval(Duration.ofSeconds(30))
                                .build()
                )
                .setResource(resource)
                .build();

        return OpenTelemetrySdk.builder()
                .setMeterProvider(meterProvider)
                .buildAndRegisterGlobal();
    }

    @Bean
    public Meter meter(OpenTelemetry openTelemetry) {
        return openTelemetry.getMeter("com.example.otelapp");
    }
}
