package com.deliverytech.deliveryapi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Controller customizado para endpoint de health check
 * Disponibiliza um endpoint /health simplificado além do Actuator
 */
@RestController
public class HealthController {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${info.app.version:unknown}")
    private String version;

    @GetMapping("/")
    public Map<String, Object> root() {
        return Map.of(
                "message", "Bem-vindo à Delivery Tech API",
                "status", "online",
                "application", applicationName,
                "version", version,
                "timestamp", LocalDateTime.now(),
                "endpoints", Map.of(
                        "health", "/health",
                        "actuator", "/actuator",
                        "restaurants", "/api/v1/restaurants"
                )
        );
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
                "status", "UP",
                "application", applicationName,
                "version", version,
                "timestamp", LocalDateTime.now(),
                "javaVersion", System.getProperty("java.version"),
                "details", Map.of(
                        "message", "Delivery Tech API está funcionando corretamente",
                        "actuator", "Para detalhes completos acesse /actuator/health"
                )
        );
    }
}
