package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.request.RestauranteRequest;
import com.deliverytech.delivery_api.dto.response.RestauranteResponse;
import com.deliverytech.delivery_api.model.Restaurante;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface RestauranteService {
    // Métodos existentes com Entity
    Restaurante cadastrar(RestauranteRequest restauranteRequest);

    Optional<Restaurante> buscarPorId(Long id);

    List<Restaurante> listarTodos();

    List<Restaurante> listarAtivos();
    org.springframework.data.domain.Page<Restaurante> listarAtivos(org.springframework.data.domain.Pageable pageable);

    List<Restaurante> buscarPorCategoria(String categoria);

    List<Restaurante> buscarPorAvaliacao(BigDecimal minAvaliacao);

    List<Restaurante> buscarPorTaxaEntrega(BigDecimal maxTaxa);

    Restaurante atualizar(Long id, RestauranteRequest restauranteRequest);

    void inativar(Long id);

    BigDecimal calcularTaxaEntrega(Long restauranteId, String cep);

    Restaurante alterarStatus(Long id, Boolean ativo);

    List<Restaurante> buscarProximos(String cep);
    org.springframework.data.domain.Page<com.deliverytech.delivery_api.dto.response.RestauranteResponse> buscarProximos(String cep, org.springframework.data.domain.Pageable pageable);

    List<Restaurante> listarComFiltros(String categoria, Boolean ativo);

    // Novos métodos com DTOs
    RestauranteResponse cadastrarRestaurante(RestauranteRequest restauranteRequest);

    RestauranteResponse buscarRestaurantePorId(Long id);

    List<RestauranteResponse> buscarRestaurantesPorCategoria(String categoria);

    List<RestauranteResponse> buscarRestaurantesDisponiveis();
    org.springframework.data.domain.Page<RestauranteResponse> buscarRestaurantesDisponiveis(org.springframework.data.domain.Pageable pageable);

    org.springframework.data.domain.Page<RestauranteResponse> buscarRestaurantesPorCategoria(String categoria, org.springframework.data.domain.Pageable pageable);

    RestauranteResponse atualizarRestaurante(Long id, RestauranteRequest restauranteRequest);
}
