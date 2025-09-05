package com.deliverytech.delivery_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
// imports do OpenAPI removidos pois não são usados neste controlador
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@Tag(name = "Health", description = "Verificações de saúde e informações do sistema")
public class HealthController {

    @GetMapping("/health")
    @Operation(summary = "Verificar saúde da aplicação", 
               description = "Retorna o status de saúde da aplicação e informações básicas")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Aplicação está saudável")
    })
    public org.springframework.http.ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<java.util.Map<String,String>>> health() {
        Map<String, String> data = Map.of(
                "status",
                "UP",
                "timestamp",
                LocalDateTime.now().toString(),
                "service",
                "Delivery API",
                "javaVersion",
                System.getProperty("java.version"));
    return org.springframework.http.ResponseEntity.ok(new com.deliverytech.delivery_api.dto.response.ApiResult<>(data, "Aplicação saudável", true));
    }

    @GetMapping("/info")
    @Operation(summary = "Obter informações da aplicação", 
               description = "Retorna informações detalhadas sobre a aplicação e ambiente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Informações obtidas com sucesso")
    })
    public org.springframework.http.ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<HealthController.AppInfo>> info() {
        AppInfo info = new AppInfo(
                "Delivery Tech API",
                "1.0.0",
                "Victor Alexandre Rabay",
                "JDK 21",
                "Spring Boot 3.2.x");
    return org.springframework.http.ResponseEntity.ok(new com.deliverytech.delivery_api.dto.response.ApiResult<>(info, "Informações da aplicação", true));
    }

    // Record para demonstrar recurso do Java 14+ (disponível no JDK 21)
    public record AppInfo(
            String application,
            String version,
            String developer,
            String javaVersion,
            String framework) {}
}
