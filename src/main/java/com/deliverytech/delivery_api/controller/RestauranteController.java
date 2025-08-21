package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.dto.request.RestauranteRequest;
import com.deliverytech.delivery_api.service.RestauranteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
    private final RestauranteService restauranteService;

    public RestauranteController(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
    }


    @PostMapping
    public ResponseEntity<Restaurante> criar(@RequestBody RestauranteRequest restauranteRequest) {
        Restaurante novo = restauranteService.cadastrar(restauranteRequest);
        return ResponseEntity.status(201).body(novo);
    }


    @GetMapping
    public List<Restaurante> listar() {
        return restauranteService.listarAtivos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurante> buscarPorId(@PathVariable Long id) {
        return restauranteService.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<Restaurante> atualizar(@PathVariable Long id, @RequestBody RestauranteRequest restauranteRequest) {
        Restaurante atualizado = restauranteService.atualizar(id, restauranteRequest);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        restauranteService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categoria/{categoria}")
    public List<Restaurante> buscarPorCategoria(@PathVariable String categoria) {
        // Supondo que exista o método no service/repository
        return restauranteService.buscarPorCategoria(categoria);
    }
}
