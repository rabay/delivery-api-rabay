package com.deliverytech.delivery_api.mapper;

import com.deliverytech.delivery_api.dto.request.ProdutoRequest;
import com.deliverytech.delivery_api.dto.response.ProdutoResponse;
import com.deliverytech.delivery_api.dto.response.RestauranteResumoResponse;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProdutoMapper {

  private final RestauranteRepository restauranteRepository;
  private final RestauranteMapper restauranteMapper;
  private final DtoMapper dtoMapper;

  public Produto toEntity(ProdutoRequest dto) {
    Restaurante restaurante =
        restauranteRepository
            .findById(dto.getRestauranteId())
            .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));

    Produto produto = dtoMapper.toEntity(dto, Produto.class);
    produto.setRestaurante(restaurante);
    produto.setExcluido(false);
    if (produto.getDisponivel() == null) {
      produto.setDisponivel(true);
    }
    return produto;
  }

  public ProdutoResponse toResponse(Produto entity) {
    RestauranteResumoResponse restauranteResumo = null;
    if (entity.getRestaurante() != null) {
      restauranteResumo = restauranteMapper.toResumoResponse(entity.getRestaurante());
    }

    ProdutoResponse response = dtoMapper.toDto(entity, ProdutoResponse.class);
    response.setRestaurante(restauranteResumo);
    return response;
  }
}
