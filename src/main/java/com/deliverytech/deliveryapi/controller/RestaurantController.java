package com.deliverytech.deliveryapi.controller;

import com.deliverytech.deliveryapi.dto.CreateRestaurantRequest;
import com.deliverytech.deliveryapi.dto.RestaurantCategoryDTO;
import com.deliverytech.deliveryapi.dto.RestaurantDTO;
import com.deliverytech.deliveryapi.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Restaurants", description = "Operações de gerenciamento de restaurantes")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Operation(
        summary = "Criar novo restaurante",
        description = "Cria um novo restaurante no sistema com todas as informações necessárias"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Restaurante criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RestaurantDTO.class),
                examples = @ExampleObject(
                    name = "Exemplo de resposta",
                    value = """
                    {
                        "data": {
                            "id": 1,
                            "name": "Pizza Palace",
                            "description": "A melhor pizza da cidade",
                            "cnpj": "12.345.678/0001-90",
                            "phone": "(11) 99999-9999",
                            "address": {
                                "street": "Rua das Pizzas",
                                "number": "123",
                                "complement": "Loja 1",
                                "neighborhood": "Centro",
                                "city": "São Paulo",
                                "state": "SP",
                                "postalCode": "01234-567",
                                "reference": "Próximo ao metrô"
                            },
                            "logo": "https://exemplo.com/logo.png",
                            "active": true,
                            "open": true,
                            "createdAt": "2025-08-11T20:00:00",
                            "updatedAt": "2025-08-11T20:00:00"
                        },
                        "meta": {
                            "timestamp": "2025-08-11T20:00:00",
                            "message": "Restaurante criado com sucesso",
                            "version": "v1"
                        }
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos fornecidos",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Exemplo de erro de validação",
                    value = """
                    {
                        "error": "Dados inválidos",
                        "message": "Nome é obrigatório",
                        "meta": {
                            "timestamp": "2025-08-11T20:00:00",
                            "version": "v1"
                        }
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno do servidor"
        )
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> createRestaurant(
            @Parameter(
                description = "Dados do restaurante a ser criado",
                required = true,
                schema = @Schema(implementation = CreateRestaurantRequest.class)
            )
            @RequestBody CreateRestaurantRequest request) {
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

    @Operation(
        summary = "Listar restaurantes",
        description = "Lista restaurantes com opções de filtro por status e busca"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de restaurantes retornada com sucesso",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = RestaurantDTO.class))
            )
        )
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllRestaurants(
            @Parameter(description = "Incluir restaurantes inativos") 
            @RequestParam(value = "includeInactive", defaultValue = "false") boolean includeInactive,
            @Parameter(description = "Apenas restaurantes abertos")
            @RequestParam(value = "onlyOpen", defaultValue = "false") boolean onlyOpen,
            @Parameter(description = "Busca por nome do restaurante")
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

    /**
     * GET /api/v1/restaurants/category/{categoryId}
     * Busca restaurantes por categoria (ID)
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Map<String, Object>> getRestaurantsByCategory(@PathVariable Long categoryId) {
        List<RestaurantDTO> restaurants = restaurantService.getRestaurantsByCategory(categoryId);
        
        Map<String, Object> response = Map.of(
                "data", restaurants,
                "meta", Map.of(
                        "timestamp", LocalDateTime.now(),
                        "categoryId", categoryId,
                        "total", restaurants.size(),
                        "version", "v1"
                )
        );

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/restaurants/category/name/{categoryName}
     * Busca restaurantes por categoria (nome)
     */
    @GetMapping("/category/name/{categoryName}")
    public ResponseEntity<Map<String, Object>> getRestaurantsByCategoryName(@PathVariable String categoryName) {
        List<RestaurantDTO> restaurants = restaurantService.getRestaurantsByCategoryName(categoryName);
        
        Map<String, Object> response = Map.of(
                "data", restaurants,
                "meta", Map.of(
                        "timestamp", LocalDateTime.now(),
                        "categoryName", categoryName,
                        "total", restaurants.size(),
                        "version", "v1"
                )
        );

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/restaurants/categories
     * Lista todas as categorias que possuem restaurantes
     */
    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getCategoriesInUse() {
        List<RestaurantCategoryDTO> categories = restaurantService.getCategoriesInUse();
        
        Map<String, Object> response = Map.of(
                "data", categories,
                "meta", Map.of(
                        "timestamp", LocalDateTime.now(),
                        "total", categories.size(),
                        "version", "v1"
                )
        );

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/restaurants/search
     * Busca restaurantes por nome ou categoria
     * 
     * Query Parameters:
     * - q: string - termo de busca (obrigatório)
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchRestaurants(@RequestParam("q") String query) {
        if (query == null || query.trim().isEmpty()) {
            Map<String, Object> errorResponse = Map.of(
                    "error", "Parâmetro de busca é obrigatório",
                    "message", "O parâmetro 'q' deve ser informado",
                    "meta", Map.of(
                            "timestamp", LocalDateTime.now(),
                            "version", "v1"
                    )
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }

        List<RestaurantDTO> restaurants = restaurantService.searchRestaurantsByNameOrCategory(query.trim());
        
        Map<String, Object> response = Map.of(
                "data", restaurants,
                "meta", Map.of(
                        "timestamp", LocalDateTime.now(),
                        "query", query.trim(),
                        "total", restaurants.size(),
                        "version", "v1"
                )
        );

        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/v1/restaurants/{id}
     * Atualiza um restaurante existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateRestaurant(
            @PathVariable Long id, 
            @RequestBody CreateRestaurantRequest request) {
        try {
            RestaurantDTO updatedRestaurant = restaurantService.updateRestaurant(id, request);
            
            Map<String, Object> response = Map.of(
                    "data", updatedRestaurant,
                    "meta", Map.of(
                            "timestamp", LocalDateTime.now(),
                            "message", "Restaurante atualizado com sucesso",
                            "version", "v1"
                    )
            );
            
            return ResponseEntity.ok(response);
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
                    "message", "Não foi possível atualizar o restaurante",
                    "meta", Map.of(
                            "timestamp", LocalDateTime.now(),
                            "version", "v1"
                    )
            );
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * DELETE /api/v1/restaurants/{id}
     * Remove um restaurante (soft delete - marca como inativo)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteRestaurant(@PathVariable Long id) {
        try {
            restaurantService.deleteRestaurant(id);
            
            Map<String, Object> response = Map.of(
                    "meta", Map.of(
                            "timestamp", LocalDateTime.now(),
                            "message", "Restaurante removido com sucesso",
                            "version", "v1"
                    )
            );
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = Map.of(
                    "error", "Restaurante não encontrado",
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
                    "message", "Não foi possível remover o restaurante",
                    "meta", Map.of(
                            "timestamp", LocalDateTime.now(),
                            "version", "v1"
                    )
            );
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * PATCH /api/v1/restaurants/{id}/status
     * Ativa ou desativa um restaurante
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> toggleActiveStatus(
            @PathVariable Long id, 
            @RequestParam("active") boolean active) {
        try {
            RestaurantDTO updatedRestaurant = restaurantService.updateActiveStatus(id, active);
            
            Map<String, Object> response = Map.of(
                    "data", updatedRestaurant,
                    "meta", Map.of(
                            "timestamp", LocalDateTime.now(),
                            "message", active ? "Restaurante ativado com sucesso" : "Restaurante desativado com sucesso",
                            "version", "v1"
                    )
            );
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = Map.of(
                    "error", "Restaurante não encontrado",
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
                    "message", "Não foi possível alterar o status do restaurante",
                    "meta", Map.of(
                            "timestamp", LocalDateTime.now(),
                            "version", "v1"
                    )
            );
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * PATCH /api/v1/restaurants/{id}/open-status
     * Abre ou fecha um restaurante
     */
    @PatchMapping("/{id}/open-status")
    public ResponseEntity<Map<String, Object>> toggleOpenStatus(
            @PathVariable Long id, 
            @RequestParam("open") boolean open) {
        try {
            RestaurantDTO updatedRestaurant = restaurantService.updateOpenStatus(id, open);
            
            Map<String, Object> response = Map.of(
                    "data", updatedRestaurant,
                    "meta", Map.of(
                            "timestamp", LocalDateTime.now(),
                            "message", open ? "Restaurante aberto com sucesso" : "Restaurante fechado com sucesso",
                            "version", "v1"
                    )
            );
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = Map.of(
                    "error", "Erro de validação",
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
                    "message", "Não foi possível alterar o status de funcionamento do restaurante",
                    "meta", Map.of(
                            "timestamp", LocalDateTime.now(),
                            "version", "v1"
                    )
            );
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    

    

}
