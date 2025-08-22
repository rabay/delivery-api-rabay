package com.deliverytech.delivery_api.service.impl;

import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.service.ProdutoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
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
    @Transactional(readOnly = true)
    public List<Produto> buscarPorRestaurante(Restaurante restaurante) {
        return produtoRepository.findByRestauranteAndExcluidoFalse(restaurante);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produto> buscarDisponiveis() {
        return produtoRepository.findByDisponivelTrueAndExcluidoFalse();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    @Override
    public Produto atualizar(Long id, Produto produtoAtualizado) {
        Produto existente = produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        existente.setNome(produtoAtualizado.getNome());
    existente.setCategoria(produtoAtualizado.getCategoria());
    existente.setDisponivel(produtoAtualizado.getDisponivel());
    existente.setRestaurante(produtoAtualizado.getRestaurante());
        // Adicione outros campos conforme necessário
        return produtoRepository.save(existente);
    }

    @Override
    public void inativar(Long id) {
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        produto.setDisponivel(false);
        produtoRepository.save(produto);
    }

    @Override
    public void deletar(Long id) {
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        produto.setDisponivel(false);
        produto.setExcluido(true);
        produtoRepository.save(produto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produto> buscarPorCategoria(String categoria) {
        return produtoRepository.findByCategoriaAndExcluidoFalse(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produto> buscarPorNome(String nome) {
        // Se existir método customizado, usar:
        // return produtoRepository.findByNomeContainingIgnoreCaseAndExcluidoFalse(nome);
        List<Produto> todos = produtoRepository.findByDisponivelTrueAndExcluidoFalse();
        return todos.stream()
            .filter(p -> p.getNome() != null && p.getNome().toLowerCase().contains(nome.toLowerCase()))
            .toList();
    }

    @Override
    public void alterarDisponibilidade(Long id, boolean disponivel) {
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        produto.setDisponivel(disponivel);
        produtoRepository.save(produto);
    }

    @Override
    public void validarPreco(java.math.BigDecimal preco) {
        if (preco == null || preco.compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Preço inválido");
        }
    }
}
