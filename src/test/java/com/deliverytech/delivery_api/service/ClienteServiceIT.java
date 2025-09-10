package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.AbstractIntegrationTest;
import com.deliverytech.delivery_api.dto.request.ClienteRequest;
import com.deliverytech.delivery_api.dto.response.ClienteResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

class ClienteServiceIT extends AbstractIntegrationTest {
    @Autowired
    private ClienteService clienteService;

    @Test
    void contextLoads() {
        assertThat(clienteService).isNotNull();
    }

    @Test
    void softDeleteDeveMarcarComoExcluidoENaoRetornarEmListarAtivos() {
        ClienteRequest req = new ClienteRequest();
        req.setNome("Cliente SoftDelete");
        req.setEmail("cliente@soft.com");
        req.setTelefone("11999999999");
        req.setEndereco("Rua Teste, 123");
        ClienteResponse resp = clienteService.cadastrar(req);
        final Long clienteId = resp.getId();
        clienteService.inativar(clienteId);
        List<ClienteResponse> ativos = clienteService.listarAtivos();
        assertThat(ativos.stream().anyMatch(c -> c.getId().equals(clienteId))).isFalse();
        // Não há método buscarPorId com DTO, então validamos apenas a ausência em ativos
    }
}
