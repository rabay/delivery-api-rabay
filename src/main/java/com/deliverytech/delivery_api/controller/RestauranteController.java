
package com.deliverytech.delivery_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.dto.request.RestauranteRequest;
import com.deliverytech.delivery_api.service.RestauranteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/restaurantes")
@Tag(name = "Restaurantes", description = "Gerenciamento de restaurantes, incluindo cadastro, consulta, atualização, inativação e busca por categoria.")
public class RestauranteController {
    private final RestauranteService restauranteService;

    public RestauranteController(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
    }


    @Operation(summary = "Cadastrar novo restaurante", description = "Cria um novo restaurante ativo no sistema.")
    @PostMapping
    public ResponseEntity<Restaurante> criar(@RequestBody RestauranteRequest restauranteRequest) {
        Restaurante novo = restauranteService.cadastrar(restauranteRequest);
        return ResponseEntity.status(201).body(novo);
    }


    @Operation(summary = "Listar restaurantes ativos", description = "Retorna todos os restaurantes ativos cadastrados.")
    @GetMapping
    public List<Restaurante> listar() {
        return restauranteService.listarAtivos();
    }

    @Operation(summary = "Buscar restaurante por ID", description = "Consulta um restaurante pelo seu identificador único.")
    @GetMapping("/{id}")
    public ResponseEntity<Restaurante> buscarPorId(@PathVariable Long id) {
        return restauranteService.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }


    @Operation(summary = "Atualizar restaurante", description = "Atualiza os dados de um restaurante existente.")
    @PutMapping("/{id}")
    public ResponseEntity<Restaurante> atualizar(@PathVariable Long id, @RequestBody RestauranteRequest restauranteRequest) {
        Restaurante atualizado = restauranteService.atualizar(id, restauranteRequest);
        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Inativar restaurante", description = "Inativa um restaurante pelo seu ID, tornando-o indisponível para operações.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        restauranteService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar restaurantes por categoria", description = "Retorna todos os restaurantes ativos de uma determinada categoria.")
    @GetMapping("/categoria/{categoria}")
    public List<Restaurante> buscarPorCategoria(@PathVariable String categoria) {
        // Supondo que exista o método no service/repository
        return restauranteService.buscarPorCategoria(categoria);
    }
}
