package com.deliverytech.delivery_api.mapper.converters;

import com.deliverytech.delivery_api.model.StatusPedido;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class StatusPedidoReverseConverter implements Converter<StatusPedido, String> {
    
    @Override
    public String convert(MappingContext<StatusPedido, String> context) {
        StatusPedido source = context.getSource();
        return source != null ? source.name() : null;
    }
}