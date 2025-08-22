package com.deliverytech.delivery_api.mapper;

import com.deliverytech.delivery_api.dto.request.ClienteRequest;
import com.deliverytech.delivery_api.dto.response.ClienteResponse;
import com.deliverytech.delivery_api.model.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ClienteMapperTest {
    private final ClienteMapper mapper = new ClienteMapper();

    @Test
    @DisplayName("Deve mapear ClienteRequest para Cliente corretamente")
    void deveMapearClienteRequestParaCliente() {
        ClienteRequest request = new ClienteRequest("João da Silva", "11999999999", "joao@email.com", "Rua A, 123");
        Cliente cliente = mapper.toEntity(request);
        assertThat(cliente.getNome()).isEqualTo(request.getNome());
        assertThat(cliente.getEmail()).isEqualTo(request.getEmail());
        assertThat(cliente.getTelefone()).isEqualTo(request.getTelefone());
        assertThat(cliente.getEndereco()).isEqualTo(request.getEndereco());
        assertThat(cliente.isAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deve mapear Cliente para ClienteResponse corretamente")
    void deveMapearClienteParaClienteResponse() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Maria Oliveira");
        cliente.setEmail("maria@email.com");
        cliente.setTelefone("11988888888");
        cliente.setEndereco("Rua B, 456");
        cliente.setAtivo(true);
        ClienteResponse response = mapper.toResponse(cliente);
        assertThat(response.getId()).isEqualTo(cliente.getId());
        assertThat(response.getNome()).isEqualTo(cliente.getNome());
        assertThat(response.getEmail()).isEqualTo(cliente.getEmail());
        assertThat(response.getTelefone()).isEqualTo(cliente.getTelefone());
        assertThat(response.getEndereco()).isEqualTo(cliente.getEndereco());
        assertThat(response.isAtivo()).isTrue();
    }
}
