package com.deliverytech.delivery_api.mapper;

import com.deliverytech.delivery_api.dto.request.ClienteRequest;
import com.deliverytech.delivery_api.dto.response.ClienteResponse;
import com.deliverytech.delivery_api.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

  private final DtoMapper dtoMapper;

  public ClienteMapper(DtoMapper dtoMapper) {
    this.dtoMapper = dtoMapper;
  }

  public Cliente toEntity(ClienteRequest dto) {
    return dtoMapper.toEntity(dto, Cliente.class);
  }

  public ClienteResponse toResponse(Cliente entity) {
    return dtoMapper.toDto(entity, ClienteResponse.class);
  }
}
