package com.deliverytech.delivery_api.mapper;

import com.deliverytech.delivery_api.dto.request.ProdutoRequest;
import com.deliverytech.delivery_api.dto.response.ProdutoResponse;
import com.deliverytech.delivery_api.dto.response.RestauranteResumoResponse;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProdutoMapper {
    
    private final RestauranteRepository restauranteRepository;
    private final RestauranteMapper restauranteMapper;
    
    public Produto toEntity(ProdutoRequest dto) {
        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));
        
        return Produto.builder()
                .nome(dto.getNome())
                .categoria(dto.getCategoria())
                .descricao(dto.getDescricao())
                .preco(dto.getPreco())
                .disponivel(dto.getDisponivel() != null ? dto.getDisponivel() : true)
                .excluido(false)
                .restaurante(restaurante)
                .build();
    }
    
    public ProdutoResponse toResponse(Produto entity) {
        RestauranteResumoResponse restauranteResumo = null;
        if (entity.getRestaurante() != null) {
            restauranteResumo = restauranteMapper.toResumoResponse(entity.getRestaurante());
        }
        
        return ProdutoResponse.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .categoria(entity.getCategoria())
                .descricao(entity.getDescricao())
                .preco(entity.getPreco())
                .disponivel(entity.getDisponivel())
                .restaurante(restauranteResumo)
                .build();
    }
}