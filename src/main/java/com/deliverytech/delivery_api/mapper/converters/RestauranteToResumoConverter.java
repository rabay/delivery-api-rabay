package com.deliverytech.delivery_api.mapper.converters;

import com.deliverytech.delivery_api.dto.response.RestauranteResumoResponse;
import com.deliverytech.delivery_api.model.Restaurante;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class RestauranteToResumoConverter implements Converter<Restaurante, RestauranteResumoResponse> {
    
    @Override
    public RestauranteResumoResponse convert(MappingContext<Restaurante, RestauranteResumoResponse> context) {
        Restaurante source = context.getSource();
        if (source == null) return null;
        
        return RestauranteResumoResponse.builder()
            .id(source.getId())
            .nome(source.getNome())
            .categoria(source.getCategoria())
            .taxaEntrega(source.getTaxaEntrega())
            .tempoEntregaMinutos(source.getTempoEntregaMinutos())
            .avaliacao(source.getAvaliacao())
            .ativo(source.isAtivo())
            .build();
    }
}