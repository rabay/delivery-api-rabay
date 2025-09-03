package com.deliverytech.delivery_api.mapper;

import com.deliverytech.delivery_api.dto.request.RestauranteRequest;
import com.deliverytech.delivery_api.dto.response.RestauranteResponse;
import com.deliverytech.delivery_api.dto.response.RestauranteResumoResponse;
import com.deliverytech.delivery_api.model.Restaurante;

import org.springframework.stereotype.Component;

@Component
public class RestauranteMapper {
    
    private final DtoMapper dtoMapper;
    
    public RestauranteMapper(DtoMapper dtoMapper) {
        this.dtoMapper = dtoMapper;
    }

    public Restaurante toEntity(RestauranteRequest dto) {
        Restaurante restaurante = dtoMapper.toEntity(dto, Restaurante.class);
        restaurante.setAtivo(true);
        restaurante.setExcluido(false);
        return restaurante;
    }

    public RestauranteResponse toResponse(Restaurante entity) {
        return dtoMapper.toDto(entity, RestauranteResponse.class);
    }

    public RestauranteResumoResponse toResumoResponse(Restaurante entity) {
        return dtoMapper.toDto(entity, RestauranteResumoResponse.class);
    }
}