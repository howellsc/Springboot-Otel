# Spring Boot OpenTelemetry Metrics Application

## Overview

A Spring Boot application that demonstrates OpenTelemetry (OTEL) integration for metrics collection and export. The application provides REST API endpoints that automatically track custom metrics including request counts, response times, and active users.

## User Preferences

Preferred communication style: Simple, everyday language.

## System Architecture

### Backend Architecture
- **Framework**: Spring Boot 3.2.0 with embedded Tomcat
- **Java Version**: Java 17 (GraalVM)
- **Build Tool**: Maven
- **Port**: 5000

### API Endpoints
- `GET /api/hello` - Simple greeting endpoint
- `POST /api/process` - Data processing endpoint
- `GET /api/users/login` - Simulates user login (increments active users)
- `GET /api/users/logout` - Simulates user logout (decrements active users)
- `GET /api/health` - Health check endpoint
- `GET /actuator/health` - Spring Actuator health endpoint
- `GET /actuator/metrics` - Spring Actuator metrics endpoint

### OpenTelemetry Integration

**Configuration** (`MetricsConfig.java`):
- Global OpenTelemetry SDK with SdkMeterProvider
- LoggingMetricExporter exports metrics every 30 seconds
- Service name: "otel-springboot-app"

**Custom Metrics** (`MetricsService.java`):
1. **Counter**: `app.requests.total`
   - Tracks total number of requests
   - Attributes: endpoint, method
   - Unit: requests

2. **Histogram**: `app.response.time`
   - Tracks response time in milliseconds
   - Attributes: endpoint
   - Unit: ms
   - Buckets: 0-10000ms

3. **Gauge**: `app.users.active`
   - Tracks number of active users
   - Updated via login/logout endpoints
   - Unit: users

### Data Storage
No database required - all metrics are in-memory and exported to logs.

## External Dependencies

### Core Libraries & Frameworks
- `spring-boot-starter-web` - REST API framework
- `spring-boot-starter-actuator` - Health and metrics endpoints
- `opentelemetry-api` (1.32.0) - OTEL API
- `opentelemetry-sdk` (1.32.0) - OTEL SDK implementation
- `opentelemetry-exporter-logging` (1.32.0) - Logs metrics to console
- `opentelemetry-semconv` (1.23.1-alpha) - Semantic conventions
- `micrometer-registry-otlp` - Micrometer OTLP support (disabled in production)

### Development & Deployment Tools
- **Build**: Maven 
- **Workflow**: Spring Boot Server (runs packaged JAR on port 5000)
- **Log Export**: Metrics exported every 30 seconds to application logs

## Project Structure
```
├── pom.xml                                    # Maven configuration
├── src/main/
│   ├── java/com/example/otelapp/
│   │   ├── OtelSpringBootApplication.java    # Main application entry point
│   │   ├── config/
│   │   │   └── MetricsConfig.java            # OTEL configuration
│   │   ├── controller/
│   │   │   └── ApiController.java            # REST API endpoints
│   │   └── service/
│   │       └── MetricsService.java           # Custom metrics service
│   └── resources/
│       └── application.properties             # Application configuration
└── target/                                    # Build output (JAR)
```

## Recent Changes
- 2025-10-01: Initial project creation with OpenTelemetry metrics integration
- Disabled OTLP exporter to reduce log noise (using LoggingMetricExporter only)
- Configured periodic metric export every 30 seconds
