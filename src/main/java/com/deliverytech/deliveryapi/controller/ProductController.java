package com.deliverytech.deliveryapi.controller;

import com.deliverytech.deliveryapi.dto.CreateProductRequest;
import com.deliverytech.deliveryapi.dto.ProductDTO;
import com.deliverytech.deliveryapi.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controller para gerenciamento de produtos
 */
@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "Operações de gerenciamento de produtos")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Cria um novo produto
     */
    @PostMapping
    @Operation(summary = "Criar novo produto", description = "Cria um novo produto para um restaurante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Map<String, Object>> createProduct(@Valid @RequestBody CreateProductRequest request) {
        try {
            ProductDTO createdProduct = productService.createProduct(request);
            
            Map<String, Object> response = Map.of(
                "data", createdProduct,
                "meta", Map.of(
                    "timestamp", LocalDateTime.now(),
                    "message", "Produto criado com sucesso",
                    "version", "v1"
                )
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
                "message", "Não foi possível criar o produto",
                "meta", Map.of(
                    "timestamp", LocalDateTime.now(),
                    "version", "v1"
                )
            );
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Lista todos os produtos
     */
    @GetMapping
    @Operation(summary = "Listar produtos", description = "Lista produtos com opções de filtro")
    public ResponseEntity<Map<String, Object>> getAllProducts(
            @Parameter(description = "Incluir produtos inativos")
            @RequestParam(value = "includeInactive", defaultValue = "false") boolean includeInactive,
            @Parameter(description = "Apenas produtos disponíveis")
            @RequestParam(value = "availableOnly", defaultValue = "false") boolean availableOnly) {
        
        List<ProductDTO> products;
        String filterApplied;

        if (availableOnly) {
            products = productService.findAvailableProducts();
            filterApplied = "availableOnly: true";
        } else if (includeInactive) {
            products = productService.findAllProducts();
            filterApplied = "includeInactive: true";
        } else {
            products = productService.findActiveProducts();
            filterApplied = "activeOnly: true (default)";
        }

        Map<String, Object> response = Map.of(
            "data", products,
            "meta", Map.of(
                "total", products.size(),
                "timestamp", LocalDateTime.now(),
                "filter", filterApplied,
                "version", "v1"
            )
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Busca um produto pelo ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Busca um produto específico por ID")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable Long id) {
        try {
            ProductDTO product = productService.getProductById(id);
            
            Map<String, Object> response = Map.of(
                "data", product,
                "meta", Map.of(
                    "timestamp", LocalDateTime.now(),
                    "version", "v1"
                )
            );
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = Map.of(
                "error", "Produto não encontrado",
                "message", e.getMessage(),
                "meta", Map.of(
                    "timestamp", LocalDateTime.now(),
                    "version", "v1"
                )
            );
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Busca produtos por restaurante
     */
    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Buscar produtos por restaurante", description = "Lista produtos de um restaurante específico")
    public ResponseEntity<Map<String, Object>> getProductsByRestaurant(@PathVariable Long restaurantId) {
        try {
            List<ProductDTO> products = productService.getProductsByRestaurant(restaurantId);
            
            Map<String, Object> response = Map.of(
                "data", products,
                "meta", Map.of(
                    "timestamp", LocalDateTime.now(),
                    "restaurantId", restaurantId,
                    "total", products.size(),
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
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Busca produtos disponíveis por restaurante
     */
    @GetMapping("/restaurant/{restaurantId}/available")
    @Operation(summary = "Buscar produtos disponíveis por restaurante", description = "Lista apenas produtos disponíveis de um restaurante")
    public ResponseEntity<Map<String, Object>> getAvailableProductsByRestaurant(@PathVariable Long restaurantId) {
        try {
            List<ProductDTO> products = productService.getAvailableProductsByRestaurant(restaurantId);
            
            Map<String, Object> response = Map.of(
                "data", products,
                "meta", Map.of(
                    "timestamp", LocalDateTime.now(),
                    "restaurantId", restaurantId,
                    "total", products.size(),
                    "filter", "availableOnly: true",
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
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Atualiza um produto
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto", description = "Atualiza um produto existente")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable Long id, @Valid @RequestBody CreateProductRequest request) {
        try {
            ProductDTO updatedProduct = productService.updateProduct(id, request);
            
            Map<String, Object> response = Map.of(
                "data", updatedProduct,
                "meta", Map.of(
                    "timestamp", LocalDateTime.now(),
                    "message", "Produto atualizado com sucesso",
                    "version", "v1"
                )
            );
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = Map.of(
                "error", "Produto não encontrado ou dados inválidos",
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
                "message", "Não foi possível atualizar o produto",
                "meta", Map.of(
                    "timestamp", LocalDateTime.now(),
                    "version", "v1"
                )
            );
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Remove um produto (soft delete)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover produto", description = "Remove um produto do sistema (soft delete)")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            
            Map<String, Object> response = Map.of(
                "meta", Map.of(
                    "timestamp", LocalDateTime.now(),
                    "message", "Produto removido com sucesso",
                    "version", "v1"
                )
            );
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = Map.of(
                "error", "Produto não encontrado",
                "message", e.getMessage(),
                "meta", Map.of(
                    "timestamp", LocalDateTime.now(),
                    "version", "v1"
                )
            );
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = Map.of(
                "error", "Erro interno do servidor",
                "message", "Não foi possível remover o produto",
                "meta", Map.of(
                    "timestamp", LocalDateTime.now(),
                    "version", "v1"
                )
            );
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Alterna disponibilidade do produto
     */
    @PatchMapping("/{id}/availability")
    @Operation(summary = "Alterar disponibilidade", description = "Alterna a disponibilidade de um produto")
    public ResponseEntity<Map<String, Object>> toggleAvailability(@PathVariable Long id, @RequestParam("available") boolean available) {
        try {
            ProductDTO updatedProduct = productService.updateAvailability(id, available);
            
            Map<String, Object> response = Map.of(
                "data", updatedProduct,
                "meta", Map.of(
                    "timestamp", LocalDateTime.now(),
                    "message", available ? "Produto disponibilizado com sucesso" : "Produto indisponibilizado com sucesso",
                    "version", "v1"
                )
            );
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = Map.of(
                "error", "Produto não encontrado",
                "message", e.getMessage(),
                "meta", Map.of(
                    "timestamp", LocalDateTime.now(),
                    "version", "v1"
                )
            );
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = Map.of(
                "error", "Erro interno do servidor",
                "message", "Não foi possível alterar a disponibilidade do produto",
                "meta", Map.of(
                    "timestamp", LocalDateTime.now(),
                    "version", "v1"
                )
            );
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    


    /**
     * Trata exceções de argumento ilegal
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> error = Map.of(
            "error", "Erro de validação",
            "message", ex.getMessage(),
            "meta", Map.of(
                "timestamp", LocalDateTime.now(),
                "version", "v1"
            )
        );
        return ResponseEntity.badRequest().body(error);
    }
}
