package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import java.util.List;
import java.util.Optional;

public interface ProdutoService {
    Produto cadastrar(Produto produto);
    List<Produto> buscarPorRestaurante(Restaurante restaurante);
    List<Produto> buscarDisponiveis();
    Optional<Produto> buscarPorId(Long id);
}
