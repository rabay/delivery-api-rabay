package com.deliverytech.deliveryapi.controller;

import com.deliverytech.deliveryapi.dto.CreateRestaurantRequest;
import com.deliverytech.deliveryapi.dto.RestaurantDTO;
import com.deliverytech.deliveryapi.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * REST Controller para gerenciamento de restaurantes
 * Implementa endpoints RESTful seguindo padrões de API moderna
 */
@RestController
@RequestMapping("/api/v1/restaurants")
@CrossOrigin(origins = "*") // Para desenvolvimento - em produção, especificar domínios específicos
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    /**
     * POST /api/v1/restaurants
     * Cria um novo restaurante
     * 
     * Exemplo de JSON esperado:
     * {
     *   "name": "Restaurante Exemplo",
     *   "description": "Descrição do restaurante",
     *   "cnpj": "00.000.000/0000-00",
     *   "phone": "(11) 99999-9999",
     *   "address": {
     *     "street": "Rua Exemplo",
     *     "number": "123",
     *     "complement": "Sala 101",
     *     "neighborhood": "Bairro Exemplo",
     *     "city": "Cidade Exemplo",
     *     "state": "SP",
     *     "postalCode": "00000-000",
     *     "reference": "Próximo ao ponto de ônibus"
     *   },
     *   "logo": "https://exemplo.com/logo.png",
     *   "deliveryFee": 5.00,
     *   "minimumOrderValue": 15.00,
     *   "averageDeliveryTimeInMinutes": 30
     * }
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createRestaurant(@RequestBody CreateRestaurantRequest request) {
        try {
            RestaurantDTO createdRestaurant = restaurantService.createRestaurant(request);
            
            Map<String, Object> response = Map.of(
                    "data", createdRestaurant,
                    "meta", Map.of(
                            "timestamp", LocalDateTime.now(),
                            "message", "Restaurante criado com sucesso",
                            "version", "v1"
                    )
            );
            
            return ResponseEntity.status(201).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = Map.of(
                    "error", "Dados inválidos",
                    "message", e.getMessage(),
                    "meta", Map.of(
                            "timestamp", LocalDateTime.now(),
                            "version", "v1"
                    )
            );
            
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = Map.of(
                    "error", "Erro interno do servidor",
                    "message", "Não foi possível criar o restaurante",
                    "meta", Map.of(
                            "timestamp", LocalDateTime.now(),
                            "version", "v1"
                    )
            );
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * GET /api/v1/restaurants
     * Lista todos os restaurantes ativos
     * 
     * Query Parameters opcionais:
     * - includeInactive: boolean - inclui restaurantes inativos
     * - onlyOpen: boolean - apenas restaurantes abertos
     * - search: string - busca por nome do restaurante
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllRestaurants(
            @RequestParam(value = "includeInactive", defaultValue = "false") boolean includeInactive,
            @RequestParam(value = "onlyOpen", defaultValue = "false") boolean onlyOpen,
            @RequestParam(value = "search", required = false) String search) {
        
        List<RestaurantDTO> restaurants;
        String filterApplied;

        // Lógica de filtragem baseada nos parâmetros
        if (search != null && !search.trim().isEmpty()) {
            restaurants = restaurantService.searchRestaurantsByName(search.trim());
            filterApplied = "search: " + search;
        } else if (onlyOpen) {
            restaurants = restaurantService.findOpenRestaurants();
            filterApplied = "onlyOpen: true";
        } else if (includeInactive) {
            restaurants = restaurantService.findAllRestaurants();
            filterApplied = "includeInactive: true";
        } else {
            restaurants = restaurantService.findAllActiveRestaurants();
            filterApplied = "activeOnly: true (default)";
        }

        // Resposta estruturada seguindo padrões de API REST
        Map<String, Object> response = Map.of(
                "data", restaurants,
                "meta", Map.of(
                        "total", restaurants.size(),
                        "timestamp", LocalDateTime.now(),
                        "filter", filterApplied,
                        "version", "v1"
                )
        );

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/restaurants/{id}
     * Busca um restaurante específico por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getRestaurantById(@PathVariable Long id) {
        return restaurantService.findRestaurantById(id)
                .map(restaurant -> {
                    Map<String, Object> response = Map.of(
                            "data", restaurant,
                            "meta", Map.of(
                                    "timestamp", LocalDateTime.now(),
                                    "version", "v1"
                            )
                    );
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
