package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

import com.deliverytech.delivery_api.projection.RelatorioVendasClientes;
import java.util.List;

@DataJpaTest
class ClienteRepositoryTest {
    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void testFindByEmail() {
        Cliente cliente = new Cliente();
        cliente.setNome("João");
        cliente.setEmail("joao@email.com");
        cliente.setAtivo(true);
        clienteRepository.save(cliente);
    Optional<Cliente> found = clienteRepository.findByEmailAndExcluidoFalse("joao@email.com");
        assertThat(found).isPresent();
        assertThat(found.get().getNome()).isEqualTo("João");
    }

    @Test
    void testFindByAtivoTrue() {
        Cliente cliente = new Cliente();
        cliente.setNome("Maria");
        cliente.setEmail("maria@email.com");
        cliente.setAtivo(true);
        clienteRepository.save(cliente);
    assertThat(clienteRepository.findByAtivoTrueAndExcluidoFalse()).extracting(Cliente::getNome).contains("Maria");
    }

    @Test
    void testRankingClientesPorPedidos() {
        Cliente cliente = new Cliente();
        cliente.setNome("Ranking Teste");
        cliente.setEmail("ranking@teste.com");
        cliente.setAtivo(true);
        clienteRepository.save(cliente);

        List<RelatorioVendasClientes> ranking = clienteRepository.rankingClientesPorPedidos();
        assertThat(ranking).isNotEmpty();
        assertThat(ranking.get(0).getNomeCliente()).isEqualTo("Ranking Teste");
    }
}
