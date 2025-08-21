package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

public interface ProdutoService {
    Produto cadastrar(Produto produto);
    List<Produto> buscarPorRestaurante(Restaurante restaurante);
    List<Produto> buscarDisponiveis();
    Optional<Produto> buscarPorId(Long id);

    // Métodos ausentes do projeto de referência
    List<Produto> listarTodos();
    Produto atualizar(Long id, Produto produtoAtualizado);
    void inativar(Long id);
    void deletar(Long id);
    List<Produto> buscarPorCategoria(String categoria);
    List<Produto> buscarPorNome(String nome);
    void alterarDisponibilidade(Long id, boolean disponivel);
    void validarPreco(BigDecimal preco);
}
