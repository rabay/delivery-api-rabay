package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.service.ProdutoService;
import com.deliverytech.delivery_api.service.RestauranteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    private final ProdutoService produtoService;
    private final RestauranteService restauranteService;

    public ProdutoController(ProdutoService produtoService, RestauranteService restauranteService) {
        this.produtoService = produtoService;
        this.restauranteService = restauranteService;
    }

    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody Produto produto) {
        Produto novo = produtoService.cadastrar(produto);
        return ResponseEntity.status(201).body(novo);
    }

    @GetMapping
    public List<Produto> listar() {
        return produtoService.buscarDisponiveis();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        // Supondo que exista método buscarPorId no service
        return produtoService.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        if (!id.equals(produto.getId())) {
            return ResponseEntity.badRequest().build();
        }
        Produto atualizado = produtoService.cadastrar(produto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        // Não implementado: deletar produto (poderia ser inativar)
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<Produto>> buscarPorRestaurante(@PathVariable Long restauranteId) {
        Restaurante restaurante = restauranteService.buscarPorId(restauranteId)
            .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));
        List<Produto> produtos = produtoService.buscarPorRestaurante(restaurante);
        return ResponseEntity.ok(produtos);
    }
}
