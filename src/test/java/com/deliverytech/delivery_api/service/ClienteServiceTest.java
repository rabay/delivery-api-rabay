package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

import com.deliverytech.delivery_api.model.Cliente;
import java.util.List;

@SpringBootTest
class ClienteServiceTest {
    @Autowired
    private ClienteService clienteService;

    @Test
    void contextLoads() {
        assertThat(clienteService).isNotNull();
    }

    @Test
    void softDeleteDeveMarcarComoExcluidoENaoRetornarEmBuscarAtivos() {
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente SoftDelete");
        cliente.setEmail("cliente@soft.com");
        cliente.setAtivo(true);
    cliente = clienteService.cadastrar(cliente);
    final Long clienteId = cliente.getId();
    clienteService.inativar(clienteId);
    List<Cliente> ativos = clienteService.buscarAtivos();
    assertThat(ativos.stream().anyMatch(c -> c.getId().equals(clienteId))).isFalse();
    var opt = clienteService.buscarPorId(clienteId);
    assertThat(opt).isPresent();
    assertThat(opt.get().getExcluido()).isTrue();
    }
}
