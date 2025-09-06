package com.deliverytech.delivery_api.mapper.converters;

import com.deliverytech.delivery_api.model.StatusPedido;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class StatusPedidoConverter implements Converter<String, StatusPedido> {

  @Override
  public StatusPedido convert(MappingContext<String, StatusPedido> context) {
    String source = context.getSource();
    if (source == null) return null;

    try {
      return StatusPedido.valueOf(source);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}
