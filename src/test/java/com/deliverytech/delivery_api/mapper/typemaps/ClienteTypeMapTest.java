package com.deliverytech.delivery_api.mapper.typemaps;

import com.deliverytech.delivery_api.dto.request.ClienteRequest;
import com.deliverytech.delivery_api.dto.response.ClienteResponse;
import com.deliverytech.delivery_api.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;

class ClienteTypeMapTest {

    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        new ClienteTypeMap(modelMapper).configure();
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

        ClienteResponse response = modelMapper.map(cliente, ClienteResponse.class);

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

        Cliente cliente = modelMapper.map(request, Cliente.class);

        assertThat(cliente.getNome()).isEqualTo(request.getNome());
        assertThat(cliente.getEmail()).isEqualTo(request.getEmail());
        assertThat(cliente.getTelefone()).isEqualTo(request.getTelefone());
        assertThat(cliente.getEndereco()).isEqualTo(request.getEndereco());
        assertThat(cliente.isAtivo()).isTrue(); // Should be set to true by default
    }
}