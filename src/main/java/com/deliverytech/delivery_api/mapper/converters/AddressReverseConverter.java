package com.deliverytech.delivery_api.mapper.converters;

import com.deliverytech.delivery_api.dto.request.EnderecoRequest;
import com.deliverytech.delivery_api.model.Endereco;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class AddressReverseConverter implements Converter<EnderecoRequest, Endereco> {
    
    @Override
    public Endereco convert(MappingContext<EnderecoRequest, Endereco> context) {
        EnderecoRequest source = context.getSource();
        if (source == null) return null;
        
        Endereco entity = new Endereco();
        entity.setRua(source.getRua());
        entity.setNumero(source.getNumero());
        entity.setBairro(source.getBairro());
        entity.setCidade(source.getCidade());
        entity.setEstado(source.getEstado());
        entity.setCep(source.getCep());
        entity.setComplemento(source.getComplemento());
        
        return entity;
    }
}