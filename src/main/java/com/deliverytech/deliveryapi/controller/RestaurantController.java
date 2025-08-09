package com.deliverytech.deliveryapi.controller;

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
