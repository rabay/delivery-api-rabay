package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.Restaurante;
import java.util.Optional;
import java.util.List;

import com.deliverytech.delivery_api.dto.request.RestauranteRequest;
import java.math.BigDecimal;

public interface RestauranteService {
    Restaurante cadastrar(RestauranteRequest restauranteRequest);
    Optional<Restaurante> buscarPorId(Long id);
    List<Restaurante> listarTodos();
    List<Restaurante> listarAtivos();
    List<Restaurante> buscarPorCategoria(String categoria);
    List<Restaurante> buscarPorAvaliacao(BigDecimal minAvaliacao);
    List<Restaurante> buscarPorTaxaEntrega(BigDecimal maxTaxa);
    Restaurante atualizar(Long id, RestauranteRequest restauranteRequest);
    void inativar(Long id);
    BigDecimal calcularTaxaEntrega(Long restauranteId, String cep);
    Restaurante alterarStatus(Long id, Boolean ativo);
    List<Restaurante> buscarProximos(String cep);
    List<Restaurante> listarComFiltros(String categoria, Boolean ativo);
}
