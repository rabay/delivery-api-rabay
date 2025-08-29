
package com.deliverytech.delivery_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.service.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/produtos")
@Tag(name = "Produtos", description = "Cadastro, consulta e gerenciamento de produtos disponíveis nos restaurantes.")
public class ProdutoController {
    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
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

    @Operation(summary = "Deletar produto", description = "Remove (soft delete) um produto pelo seu ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Alterar disponibilidade do produto", description = "Alterna a disponibilidade de um produto (ativar/desativar).")
    @PatchMapping("/{id}/disponibilidade")
    public ResponseEntity<Map<String, Object>> alterarDisponibilidade(@PathVariable Long id, @RequestBody Map<String, Boolean> request) {
        try {
            Boolean disponivel = request.get("disponivel");
            if (disponivel == null) {
                return ResponseEntity.badRequest().build();
            }
            produtoService.alterarDisponibilidade(id, disponivel);
            Map<String, Object> response = new java.util.HashMap<>();
            response.put("produtoId", id);
            response.put("disponivel", disponivel);
            response.put("message", "Disponibilidade atualizada com sucesso");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Buscar produtos por categoria", description = "Retorna todos os produtos disponíveis de uma categoria específica.")
    @GetMapping("/categoria/{categoria}")
    public List<Produto> buscarPorCategoria(@PathVariable String categoria) {
        return produtoService.buscarPorCategoria(categoria);
    }
}
