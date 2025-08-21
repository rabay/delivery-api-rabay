package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto cadastrar(Produto produto) {
        if (produto.getCategoria() == null || produto.getCategoria().isEmpty()) {
            throw new RuntimeException("Categoria obrigatória");
        }
        if (produto.getRestaurante() == null) {
            throw new RuntimeException("Restaurante obrigatório");
        }
        if (produto.getNome() == null || produto.getNome().isEmpty()) {
            throw new RuntimeException("Nome obrigatório");
        }
        // Exemplo de validação de preço (supondo campo futuro)
        // if (produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
        //     throw new RuntimeException("Preço inválido");
        // }
        return produtoRepository.save(produto);
    }

    public List<Produto> buscarPorRestaurante(Restaurante restaurante) {
        return produtoRepository.findByRestaurante(restaurante);
    }

    public List<Produto> buscarDisponiveis() {
        return produtoRepository.findByDisponivelTrue();
    }

    public java.util.Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }
}
