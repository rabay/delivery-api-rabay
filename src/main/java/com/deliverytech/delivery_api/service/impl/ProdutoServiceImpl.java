package com.deliverytech.delivery_api.service.impl;

import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.service.ProdutoService;
import com.deliverytech.delivery_api.dto.request.ProdutoRequest;
import com.deliverytech.delivery_api.dto.response.ProdutoResponse;
import com.deliverytech.delivery_api.mapper.ProdutoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProdutoServiceImpl implements ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;

    public ProdutoServiceImpl(ProdutoRepository produtoRepository, ProdutoMapper produtoMapper) {
        this.produtoRepository = produtoRepository;
        this.produtoMapper = produtoMapper;
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
    @Deprecated
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

    // ===== NOVOS MÉTODOS COM DTOs =====
    
    @Override
    public ProdutoResponse cadastrar(ProdutoRequest produtoRequest) {
        Produto produto = produtoMapper.toEntity(produtoRequest);
        Produto salvo = produtoRepository.save(produto);
        return produtoMapper.toResponse(salvo);
    }

    @Override
    @Transactional(readOnly = true)
    public ProdutoResponse buscarProdutoPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        
        // Validate not soft deleted
        if (Boolean.TRUE.equals(produto.getExcluido())) {
            throw new RuntimeException("Produto foi excluído do sistema");
        }
        
        return produtoMapper.toResponse(produto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponse> buscarProdutosPorRestaurante(Long restauranteId) {
        List<Produto> produtos = produtoRepository.findByRestauranteIdAndExcluidoFalse(restauranteId);
        return produtos.stream()
                .map(produtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProdutoResponse atualizar(Long id, ProdutoRequest produtoRequest) {
        Produto existente = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        
        // Validate not soft deleted
        if (Boolean.TRUE.equals(existente.getExcluido())) {
            throw new RuntimeException("Não é possível atualizar produto excluído");
        }
        
        // Update fields
        existente.setNome(produtoRequest.getNome());
        existente.setCategoria(produtoRequest.getCategoria());
        existente.setDescricao(produtoRequest.getDescricao());
        existente.setPreco(produtoRequest.getPreco());
        existente.setDisponivel(produtoRequest.getDisponivel());
        
        // Update restaurant if changed
        if (!existente.getRestaurante().getId().equals(produtoRequest.getRestauranteId())) {
            Produto temp = produtoMapper.toEntity(produtoRequest);
            existente.setRestaurante(temp.getRestaurante());
        }
        
        Produto atualizado = produtoRepository.save(existente);
        return produtoMapper.toResponse(atualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponse> buscarProdutosPorCategoria(String categoria) {
        List<Produto> produtos = produtoRepository.findByCategoriaAndExcluidoFalse(categoria);
        return produtos.stream()
                .map(produtoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
