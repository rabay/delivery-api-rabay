package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.Restaurante;
import java.util.Optional;
import java.util.List;

public interface RestauranteService {
    Restaurante cadastrar(Restaurante restaurante);
    Optional<Restaurante> buscarPorId(Long id);
    List<Restaurante> buscarAtivos();
    List<Restaurante> buscarPorCategoria(String categoria);
    Restaurante ativar(Long id);
    Restaurante inativar(Long id);
}
