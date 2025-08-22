package com.deliverytech.delivery_api.mapper;

import com.deliverytech.delivery_api.dto.request.ClienteRequest;
import com.deliverytech.delivery_api.dto.response.ClienteResponse;
import com.deliverytech.delivery_api.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {
    public Cliente toEntity(ClienteRequest dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setAtivo(true);
        cliente.setTelefone(dto.getTelefone());
        cliente.setEndereco(dto.getEndereco());
        return cliente;
    }

    public ClienteResponse toResponse(Cliente entity) {
        ClienteResponse dto = new ClienteResponse();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setEmail(entity.getEmail());
        dto.setTelefone(entity.getTelefone());
        dto.setEndereco(entity.getEndereco());
        dto.setAtivo(entity.isAtivo());
        return dto;
    }
}
