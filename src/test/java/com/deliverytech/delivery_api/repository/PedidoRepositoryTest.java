package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PedidoRepositoryTest {
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void testFindByClienteId() {
        Cliente c = new Cliente();
        c.setNome("Carlos");
        c.setEmail("carlos@email.com");
        c.setAtivo(true);
        clienteRepository.save(c);
        Pedido p = new Pedido();
        p.setCliente(c);
        p.setStatus(com.deliverytech.delivery_api.model.StatusPedido.CRIADO);
        p.setDataPedido(LocalDateTime.now());
        pedidoRepository.save(p);
        List<Pedido> results = pedidoRepository.findByClienteId(c.getId());
        assertThat(results).isNotEmpty();
    }
}
