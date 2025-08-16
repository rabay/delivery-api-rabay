package com.deliverytech.deliveryapi.controller;

import com.deliverytech.deliveryapi.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Controller para operações de teste e limpeza de dados
 * APENAS PARA DESENVOLVIMENTO E TESTES
 */
@RestController
@RequestMapping("/api/v1/test")
@Tag(name = "Test", description = "Operações de teste - APENAS DESENVOLVIMENTO")
public class TestController {

    @Autowired
    private TestService testService;

    @Operation(
        summary = "Limpar banco de dados",
        description = "Remove todos os dados do banco para permitir testes limpos. APENAS PARA DESENVOLVIMENTO!"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Banco limpo com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro ao limpar banco")
    })
    @DeleteMapping("/cleanup")
    public ResponseEntity<Map<String, Object>> cleanupDatabase() {
        try {
            testService.cleanupDatabase();
            
            Map<String, Object> response = Map.of(
                    "message", "Banco de dados limpo com sucesso",
                    "timestamp", LocalDateTime.now(),
                    "status", "success"
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = Map.of(
                    "error", "Erro ao limpar banco de dados",
                    "message", e.getMessage(),
                    "timestamp", LocalDateTime.now(),
                    "status", "error"
            );
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @Operation(
        summary = "Inicializar dados de teste",
        description = "Recria dados iniciais (categorias) após limpeza do banco"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dados inicializados com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro ao inicializar dados")
    })
    @PostMapping("/init")
    public ResponseEntity<Map<String, Object>> initializeTestData() {
        try {
            testService.initializeTestData();
            
            Map<String, Object> response = Map.of(
                    "message", "Dados de teste inicializados com sucesso",
                    "timestamp", LocalDateTime.now(),
                    "status", "success"
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = Map.of(
                    "error", "Erro ao inicializar dados de teste",
                    "message", e.getMessage(),
                    "timestamp", LocalDateTime.now(),
                    "status", "error"
            );
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @Operation(
        summary = "Reset completo",
        description = "Limpa o banco e reinicializa dados básicos em uma operação"
    )
    @PostMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetDatabase() {
        try {
            testService.resetDatabase();
            
            Map<String, Object> response = Map.of(
                    "message", "Reset do banco realizado com sucesso",
                    "timestamp", LocalDateTime.now(),
                    "status", "success",
                    "details", "Banco limpo e dados básicos reinicializados"
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = Map.of(
                    "error", "Erro ao realizar reset do banco",
                    "message", e.getMessage(),
                    "timestamp", LocalDateTime.now(),
                    "status", "error"
            );
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
