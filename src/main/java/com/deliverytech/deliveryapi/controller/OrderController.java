package com.deliverytech.deliveryapi.controller;

import com.deliverytech.deliveryapi.dto.CreateOrderRequest;
import com.deliverytech.deliveryapi.dto.OrderDTO;
import com.deliverytech.deliveryapi.service.OrderService;
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
 * Controller para gerenciamento de pedidos
 */
@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Orders", description = "Operações de gerenciamento de pedidos")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Cria um novo pedido
     */
    @PostMapping
    @Operation(summary = "Criar novo pedido", description = "Cria um novo pedido com validações de negócio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Map<String, Object>> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        try {
            OrderDTO createdOrder = orderService.createOrder(request);
            
            Map<String, Object> response = Map.of(
                "data", createdOrder,
                "meta", Map.of(
                    "timestamp", LocalDateTime.now(),
                    "message", "Pedido criado com sucesso",
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
                "message", "Não foi possível criar o pedido",
                "meta", Map.of(
                    "timestamp", LocalDateTime.now(),
                    "version", "v1"
                )
            );
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Busca um pedido pelo ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Busca um pedido específico por ID")
    public ResponseEntity<Map<String, Object>> getOrderById(@PathVariable Long id) {
        try {
            OrderDTO order = orderService.getOrderById(id);
            
            Map<String, Object> response = Map.of(
                "data", order,
                "meta", Map.of(
                    "timestamp", LocalDateTime.now(),
                    "version", "v1"
                )
            );
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = Map.of(
                "error", "Pedido não encontrado",
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
     * Lista pedidos por cliente
     */
    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Buscar pedidos por cliente", description = "Lista pedidos de um cliente específico")
    public ResponseEntity<Map<String, Object>> getOrdersByCustomer(@PathVariable Long customerId) {
        try {
            List<OrderDTO> orders = orderService.getOrdersByCustomer(customerId);
            
            Map<String, Object> response = Map.of(
                "data", orders,
                "meta", Map.of(
                    "timestamp", LocalDateTime.now(),
                    "customerId", customerId,
                    "total", orders.size(),
                    "version", "v1"
                )
            );
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = Map.of(
                "error", "Cliente não encontrado",
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
     * Lista pedidos por restaurante
     */
    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Buscar pedidos por restaurante", description = "Lista pedidos de um restaurante específico")
    public ResponseEntity<Map<String, Object>> getOrdersByRestaurant(@PathVariable Long restaurantId) {
        try {
            List<OrderDTO> orders = orderService.getOrdersByRestaurant(restaurantId);
            
            Map<String, Object> response = Map.of(
                "data", orders,
                "meta", Map.of(
                    "timestamp", LocalDateTime.now(),
                    "restaurantId", restaurantId,
                    "total", orders.size(),
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
     * Atualiza status do pedido
     */
    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do pedido", description = "Atualiza o status de um pedido")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(@PathVariable Long id, @RequestParam("status") String status) {
        try {
            OrderDTO updatedOrder = orderService.updateOrderStatus(id, status);
            
            Map<String, Object> response = Map.of(
                "data", updatedOrder,
                "meta", Map.of(
                    "timestamp", LocalDateTime.now(),
                    "message", "Status do pedido atualizado com sucesso",
                    "version", "v1"
                )
            );
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = Map.of(
                "error", "Status inválido ou pedido não encontrado",
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
                "message", "Não foi possível atualizar o status do pedido",
                "meta", Map.of(
                    "timestamp", LocalDateTime.now(),
                    "version", "v1"
                )
            );
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Cancela um pedido
     */
    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancelar pedido", description = "Cancela um pedido se possível")
    public ResponseEntity<Map<String, Object>> cancelOrder(@PathVariable Long id) {
        try {
            OrderDTO cancelledOrder = orderService.cancelOrder(id);
            
            Map<String, Object> response = Map.of(
                "data", cancelledOrder,
                "meta", Map.of(
                    "timestamp", LocalDateTime.now(),
                    "message", "Pedido cancelado com sucesso",
                    "version", "v1"
                )
            );
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = Map.of(
                "error", "Pedido não pode ser cancelado",
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
                "message", "Não foi possível cancelar o pedido",
                "meta", Map.of(
                    "timestamp", LocalDateTime.now(),
                    "version", "v1"
                )
            );
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Lista todos os pedidos com filtros opcionais
     */
    @GetMapping
    @Operation(summary = "Listar pedidos", description = "Lista pedidos com opções de filtro por status")
    public ResponseEntity<Map<String, Object>> getAllOrders(
            @Parameter(description = "Filtrar por status")
            @RequestParam(value = "status", required = false) String status) {
        
        List<OrderDTO> orders;
        String filterApplied = "none";

        if (status != null && !status.trim().isEmpty()) {
            orders = orderService.getOrdersByStatus(status.trim());
            filterApplied = "status: " + status.trim();
        } else {
            orders = orderService.getAllOrders();
        }

        Map<String, Object> response = Map.of(
            "data", orders,
            "meta", Map.of(
                "total", orders.size(),
                "timestamp", LocalDateTime.now(),
                "filter", filterApplied,
                "version", "v1"
            )
        );

        return ResponseEntity.ok(response);
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
