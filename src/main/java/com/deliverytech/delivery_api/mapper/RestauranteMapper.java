package com.deliverytech.delivery_api.mapper;

import com.deliverytech.delivery_api.dto.request.RestauranteRequest;
import com.deliverytech.delivery_api.dto.response.RestauranteResponse;
import com.deliverytech.delivery_api.dto.response.RestauranteResumoResponse;
import com.deliverytech.delivery_api.model.Restaurante;

import org.springframework.stereotype.Component;

@Component
public class RestauranteMapper {

    public Restaurante toEntity(RestauranteRequest dto) {
        return Restaurante.builder()
                .nome(dto.getNome())
                .categoria(dto.getCategoria())
                .endereco(dto.getEndereco())
                .taxaEntrega(dto.getTaxaEntrega())
                .telefone(dto.getTelefone())
                .email(dto.getEmail())
                .tempoEntregaMinutos(dto.getTempoEntregaMinutos())
                .avaliacao(dto.getAvaliacao())
                .ativo(true)
                .excluido(false)
                .build();
    }

    public RestauranteResponse toResponse(Restaurante entity) {
        return RestauranteResponse.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .categoria(entity.getCategoria())
                .endereco(entity.getEndereco())
                .taxaEntrega(entity.getTaxaEntrega())
                .telefone(entity.getTelefone())
                .email(entity.getEmail())
                .tempoEntregaMinutos(entity.getTempoEntregaMinutos())
                .avaliacao(entity.getAvaliacao())
                .ativo(entity.isAtivo())
                .build();
    }

    public RestauranteResumoResponse toResumoResponse(Restaurante entity) {
        return RestauranteResumoResponse.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .categoria(entity.getCategoria())
                .taxaEntrega(entity.getTaxaEntrega())
                .tempoEntregaMinutos(entity.getTempoEntregaMinutos())
                .avaliacao(entity.getAvaliacao())
                .ativo(entity.isAtivo())
                .build();
    }
}
