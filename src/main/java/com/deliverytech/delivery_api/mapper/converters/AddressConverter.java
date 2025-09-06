package com.deliverytech.delivery_api.mapper.converters;

import com.deliverytech.delivery_api.dto.request.EnderecoRequest;
import com.deliverytech.delivery_api.model.Endereco;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class AddressConverter implements Converter<Endereco, EnderecoRequest> {

  @Override
  public EnderecoRequest convert(MappingContext<Endereco, EnderecoRequest> context) {
    Endereco source = context.getSource();
    if (source == null) return null;

    EnderecoRequest dto = new EnderecoRequest();
    dto.setRua(source.getRua());
    dto.setNumero(source.getNumero());
    dto.setBairro(source.getBairro());
    dto.setCidade(source.getCidade());
    dto.setEstado(source.getEstado());
    dto.setCep(source.getCep());
    dto.setComplemento(source.getComplemento());

    return dto;
  }
}
