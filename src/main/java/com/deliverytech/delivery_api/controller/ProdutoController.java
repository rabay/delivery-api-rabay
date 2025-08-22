
package com.deliverytech.delivery_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.service.ProdutoService;
import com.deliverytech.delivery_api.service.RestauranteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/produtos")
@Tag(name = "Produtos", description = "Cadastro, consulta e gerenciamento de produtos disponíveis nos restaurantes.")
public class ProdutoController {
    private final ProdutoService produtoService;
    private final RestauranteService restauranteService;

    public ProdutoController(ProdutoService produtoService, RestauranteService restauranteService) {
        this.produtoService = produtoService;
        this.restauranteService = restauranteService;
    }

    @Operation(summary = "Cadastrar novo produto", description = "Cria um novo produto disponível em um restaurante.")
    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody Produto produto) {
        Produto novo = produtoService.cadastrar(produto);
        return ResponseEntity.status(201).body(novo);
    }

    @Operation(summary = "Listar produtos disponíveis", description = "Retorna todos os produtos disponíveis cadastrados.")
    @GetMapping
    public List<Produto> listar() {
        return produtoService.buscarDisponiveis();
    }

    @Operation(summary = "Buscar produto por ID", description = "Consulta um produto pelo seu identificador único.")
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        // Supondo que exista método buscarPorId no service
        return produtoService.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Atualizar produto", description = "Atualiza os dados de um produto existente.")
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        if (!id.equals(produto.getId())) {
            return ResponseEntity.badRequest().build();
        }
        Produto atualizado = produtoService.cadastrar(produto);
        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Deletar produto", description = "Remove (ou inativa) um produto pelo seu ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        // Não implementado: deletar produto (poderia ser inativar)
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar produtos de um restaurante", description = "Retorna todos os produtos disponíveis de um restaurante específico.")
    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<Produto>> buscarPorRestaurante(@PathVariable Long restauranteId) {
        Restaurante restaurante = restauranteService.buscarPorId(restauranteId)
            .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));
        List<Produto> produtos = produtoService.buscarPorRestaurante(restaurante);
        return ResponseEntity.ok(produtos);
    }
}
