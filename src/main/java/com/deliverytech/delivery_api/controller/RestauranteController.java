package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.request.RestauranteRequest;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.service.ProdutoService;
import com.deliverytech.delivery_api.service.RestauranteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurantes")
@Tag(
        name = "Restaurantes",
        description =
                "Gerenciamento de restaurantes, incluindo cadastro, consulta, atualização,"
                    + " inativação e busca por categoria.")
public class RestauranteController {
    private final RestauranteService restauranteService;
    private final ProdutoService produtoService;

    public RestauranteController(
            RestauranteService restauranteService, ProdutoService produtoService) {
        this.restauranteService = restauranteService;
        this.produtoService = produtoService;
    }

    @Operation(
            summary = "Cadastrar novo restaurante",
            description = "Cria um novo restaurante ativo no sistema.")
    @PostMapping
    public ResponseEntity<Restaurante> criar(@Valid @RequestBody RestauranteRequest restauranteRequest) {
        Restaurante novo = restauranteService.cadastrar(restauranteRequest);
        return ResponseEntity.status(201).body(novo);
    }

    @Operation(
            summary = "Listar restaurantes ativos",
            description = "Retorna todos os restaurantes ativos cadastrados.")
    @GetMapping
    public List<Restaurante> listar() {
        return restauranteService.listarAtivos();
    }

    @Operation(
            summary = "Buscar restaurante por ID",
            description = "Consulta um restaurante pelo seu identificador único.")
    @GetMapping("/{id}")
    public ResponseEntity<Restaurante> buscarPorId(@PathVariable Long id) {
        return restauranteService
                .buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Atualizar restaurante",
            description = "Atualiza os dados de um restaurante existente.")
    @PutMapping("/{id}")
    public ResponseEntity<Restaurante> atualizar(
            @PathVariable Long id, @Valid @RequestBody RestauranteRequest restauranteRequest) {
        Restaurante atualizado = restauranteService.atualizar(id, restauranteRequest);
        return ResponseEntity.ok(atualizado);
    }

    @Operation(
            summary = "Inativar restaurante",
            description =
                    "Inativa um restaurante pelo seu ID, tornando-o indisponível para operações.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        restauranteService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Buscar restaurantes por categoria",
            description = "Retorna todos os restaurantes ativos de uma determinada categoria.")
    @GetMapping("/categoria/{categoria}")
    public List<Restaurante> buscarPorCategoria(@PathVariable String categoria) {
        // Supondo que exista o método no service/repository
        return restauranteService.buscarPorCategoria(categoria);
    }

    @Operation(
            summary = "Calcular taxa de entrega",
            description = "Calcula a taxa de entrega para um restaurante em um CEP específico.")
    @GetMapping("/{id}/taxa-entrega/{cep}")
    public ResponseEntity<Map<String, Object>> calcularTaxaEntrega(
            @PathVariable Long id, @PathVariable String cep) {
        try {
            java.math.BigDecimal taxa = restauranteService.calcularTaxaEntrega(id, cep);
            Map<String, Object> response = new java.util.HashMap<>();
            response.put("restauranteId", id);
            response.put("cep", cep);
            response.put("taxaEntrega", taxa);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("error", "Restaurante não encontrado ou indisponível");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    @Operation(
            summary = "Listar produtos de um restaurante",
            description = "Retorna todos os produtos disponíveis de um restaurante específico.")
    @GetMapping("/{restauranteId}/produtos")
    public ResponseEntity<List<Produto>> buscarProdutosPorRestaurante(
            @PathVariable Long restauranteId) {
        try {
            Restaurante restaurante =
                    restauranteService
                            .buscarPorId(restauranteId)
                            .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));
            List<Produto> produtos = produtoService.buscarPorRestaurante(restaurante);
            return ResponseEntity.ok(produtos);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
