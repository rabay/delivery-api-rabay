package com.deliverytech.delivery_api.config;

import com.deliverytech.delivery_api.dto.request.ClienteRequest;
import com.deliverytech.delivery_api.dto.response.ClienteResponse;
import com.deliverytech.delivery_api.mapper.typemaps.ClienteTypeMap;
import com.deliverytech.delivery_api.model.Cliente;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;

class ModelMapperConfigTest {

    @Test
    void shouldConfigureModelMapperWithStrictStrategy() {
        ModelMapperConfig config = new ModelMapperConfig();
        ModelMapper mapper = config.modelMapper();
        
        // Verify that the configuration is set to STRICT
        assertThat(mapper.getConfiguration().getMatchingStrategy()).isEqualTo(org.modelmapper.convention.MatchingStrategies.STRICT);
    }
    
    @Test
    void shouldMapClienteRequestToClienteWithAtivoTrue() {
        ModelMapper mapper = new ModelMapper();
        new ClienteTypeMap(mapper).configure();
        
        ClienteRequest request = new ClienteRequest();
        request.setNome("João da Silva");
        request.setEmail("joao@teste.com");
        request.setTelefone("11999999999");
        request.setEndereco("Rua Teste, 123");
        
        Cliente cliente = mapper.map(request, Cliente.class);
        
        assertThat(cliente.getNome()).isEqualTo(request.getNome());
        assertThat(cliente.getEmail()).isEqualTo(request.getEmail());
        assertThat(cliente.getTelefone()).isEqualTo(request.getTelefone());
        assertThat(cliente.getEndereco()).isEqualTo(request.getEndereco());
        assertThat(cliente.isAtivo()).isTrue();
    }
    
    @Test
    void shouldMapClienteToClienteResponse() {
        ModelMapper mapper = new ModelMapper();
        new ClienteTypeMap(mapper).configure();
        
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João da Silva");
        cliente.setEmail("joao@teste.com");
        cliente.setTelefone("11999999999");
        cliente.setEndereco("Rua Teste, 123");
        cliente.setAtivo(true);
        
        ClienteResponse response = mapper.map(cliente, ClienteResponse.class);
        
        assertThat(response.getId()).isEqualTo(cliente.getId());
        assertThat(response.getNome()).isEqualTo(cliente.getNome());
        assertThat(response.getEmail()).isEqualTo(cliente.getEmail());
        assertThat(response.getTelefone()).isEqualTo(cliente.getTelefone());
        assertThat(response.getEndereco()).isEqualTo(cliente.getEndereco());
        assertThat(response.isAtivo()).isEqualTo(cliente.isAtivo());
    }
}