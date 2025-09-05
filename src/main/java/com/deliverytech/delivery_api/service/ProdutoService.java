package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.request.ProdutoRequest;
import com.deliverytech.delivery_api.dto.response.ProdutoResponse;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;

import java.math.BigDecimal;
import java.util.List;

public interface ProdutoService {
    // Métodos existentes com Entity
    Produto cadastrar(Produto produto);

    List<Produto> buscarPorRestaurante(Restaurante restaurante);

    List<Produto> buscarDisponiveisEntities();

    // Métodos ausentes do projeto de referência
    List<Produto> listarTodos();

    Produto atualizar(Long id, Produto produtoAtualizado);

    void inativar(Long id);

    void deletar(Long id);

    List<Produto> buscarPorCategoria(String categoria);

    List<Produto> buscarPorNome(String nome);

    void alterarDisponibilidade(Long id, boolean disponivel);

    void validarPreco(BigDecimal preco);

    // Novos métodos com DTOs
    ProdutoResponse cadastrar(ProdutoRequest produtoRequest);

    ProdutoResponse buscarProdutoPorId(Long id);

    List<ProdutoResponse> buscarProdutosPorRestaurante(Long restauranteId);

    org.springframework.data.domain.Page<com.deliverytech.delivery_api.dto.response.ProdutoResponse> buscarProdutosPorRestaurante(Long restauranteId, org.springframework.data.domain.Pageable pageable);

    ProdutoResponse atualizar(Long id, ProdutoRequest produtoRequest);

    List<ProdutoResponse> buscarProdutosPorCategoria(String categoria);

    List<ProdutoResponse> buscarDisponiveis();

    org.springframework.data.domain.Page<com.deliverytech.delivery_api.dto.response.ProdutoResponse> buscarProdutosPorCategoria(String categoria, org.springframework.data.domain.Pageable pageable);

    org.springframework.data.domain.Page<com.deliverytech.delivery_api.dto.response.ProdutoResponse> buscarProdutosPorNome(String nome, org.springframework.data.domain.Pageable pageable);

    // Versão paginada
    org.springframework.data.domain.Page<ProdutoResponse> buscarDisponiveis(org.springframework.data.domain.Pageable pageable);

    List<ProdutoResponse> buscarProdutosPorNome(String nome);

    // Métodos para controle de estoque
    void validarEstoque(Produto produto, Integer quantidadeSolicitada);
    
    void atualizarEstoque(Long produtoId, Integer novaQuantidade);
    
    void ajustarEstoque(Long produtoId, Integer quantidade);
    
    void reservarEstoque(com.deliverytech.delivery_api.model.Pedido pedido);
    
    void confirmarEstoque(com.deliverytech.delivery_api.model.Pedido pedido);
    
    void cancelarReservaEstoque(com.deliverytech.delivery_api.model.Pedido pedido);
}