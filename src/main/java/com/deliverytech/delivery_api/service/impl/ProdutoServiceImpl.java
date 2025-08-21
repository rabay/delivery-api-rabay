package com.deliverytech.delivery_api.service.impl;

import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.service.ProdutoService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoServiceImpl implements ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoServiceImpl(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
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

    @Override
    public List<Produto> buscarPorRestaurante(Restaurante restaurante) {
        return produtoRepository.findByRestaurante(restaurante);
    }

    @Override
    public List<Produto> buscarDisponiveis() {
        return produtoRepository.findByDisponivelTrue();
    }

    @Override
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }
}
