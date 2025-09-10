package com.deliverytech.delivery_api.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.deliverytech.delivery_api.dto.request.ClienteRequest;
import com.deliverytech.delivery_api.dto.response.ClienteResponse;
import com.deliverytech.delivery_api.mapper.typemaps.ClienteTypeMap;
import com.deliverytech.delivery_api.model.Cliente;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

class DtoMapperIntegrationTest {

  private DtoMapper dtoMapper;
  private ModelMapper modelMapper;

  @BeforeEach
  void setUp() {
    modelMapper = new ModelMapper();
    new ClienteTypeMap(modelMapper).configure();
    dtoMapper = new DtoMapper(modelMapper);
  }

  @Test
  void shouldMapClienteToClienteResponse() {
    Cliente cliente = new Cliente();
    cliente.setId(1L);
    cliente.setNome("João da Silva");
    cliente.setEmail("joao@teste.com");
    cliente.setTelefone("11999999999");
    cliente.setEndereco("Rua Teste, 123");
    cliente.setAtivo(true);

    ClienteResponse response = dtoMapper.toDto(cliente, ClienteResponse.class);

    assertThat(response.getId()).isEqualTo(cliente.getId());
    assertThat(response.getNome()).isEqualTo(cliente.getNome());
    assertThat(response.getEmail()).isEqualTo(cliente.getEmail());
    assertThat(response.getTelefone()).isEqualTo(cliente.getTelefone());
    assertThat(response.getEndereco()).isEqualTo(cliente.getEndereco());
    assertThat(response.isAtivo()).isEqualTo(cliente.isAtivo());
  }

  @Test
  void shouldMapClienteRequestToCliente() {
    ClienteRequest request = new ClienteRequest();
    request.setNome("João da Silva");
    request.setEmail("joao@teste.com");
    request.setTelefone("11999999999");
    request.setEndereco("Rua Teste, 123");

    Cliente cliente = dtoMapper.toEntity(request, Cliente.class);

    assertThat(cliente.getNome()).isEqualTo(request.getNome());
    assertThat(cliente.getEmail()).isEqualTo(request.getEmail());
    assertThat(cliente.getTelefone()).isEqualTo(request.getTelefone());
    assertThat(cliente.getEndereco()).isEqualTo(request.getEndereco());
    assertThat(cliente.isAtivo()).isTrue();
  }

  @Test
  void shouldMapListToDtoList() {
    Cliente cliente1 = new Cliente();
    cliente1.setId(1L);
    cliente1.setNome("João da Silva");
    cliente1.setEmail("joao@teste.com");
    cliente1.setAtivo(true);

    Cliente cliente2 = new Cliente();
    cliente2.setId(2L);
    cliente2.setNome("Maria Oliveira");
    cliente2.setEmail("maria@teste.com");
    cliente2.setAtivo(true);

    List<Cliente> clientes = List.of(cliente1, cliente2);

    List<ClienteResponse> responses = dtoMapper.toDtoList(clientes, ClienteResponse.class);

    assertThat(responses).hasSize(2);
    assertThat(responses.get(0).getNome()).isEqualTo("João da Silva");
    assertThat(responses.get(1).getNome()).isEqualTo("Maria Oliveira");
  }
}
