package com.deliverytech.deliveryapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Health", description = "Endpoints de monitoramento e saúde da aplicação")
public class HealthController {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${info.app.version:unknown}")
    private String version;

    @GetMapping("/")
    @Operation(summary = "Página inicial da API", description = "Retorna informações básicas sobre a API e endpoints disponíveis")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Informações da API retornadas com sucesso")
    })
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
    @Operation(summary = "Status de saúde da aplicação", description = "Retorna o status atual da aplicação e informações do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status de saúde retornado com sucesso")
    })
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
