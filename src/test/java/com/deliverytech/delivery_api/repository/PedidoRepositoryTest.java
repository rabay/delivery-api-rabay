package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.projection.RelatorioVendas;
import java.math.BigDecimal;
import java.util.ArrayList;

@DataJpaTest
class PedidoRepositoryTest {
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Test
    void testCalcularTotalVendasPorRestaurante() {
        Restaurante r = new Restaurante();
        r.setNome("Restaurante Teste");
        r.setCategoria("Pizza");
        r.setAtivo(true);
        r.setAvaliacao(new BigDecimal("4.5"));
        restauranteRepository.save(r);

        Cliente c = new Cliente();
        c.setNome("Cliente Teste");
        c.setEmail("cliente@teste.com");
        c.setAtivo(true);
        clienteRepository.save(c);

        Pedido p = new Pedido();
        p.setCliente(c);
        p.setRestaurante(r);
        p.setStatus(com.deliverytech.delivery_api.model.StatusPedido.CRIADO);
        p.setDataPedido(LocalDateTime.now());
        p.setValorTotal(new BigDecimal("100.00"));
        p.setItens(new ArrayList<>());
        pedidoRepository.save(p);

        List<RelatorioVendas> relatorio = pedidoRepository.calcularTotalVendasPorRestaurante();
        assertThat(relatorio).isNotEmpty();
        assertThat(relatorio.get(0).getNomeRestaurante()).isEqualTo("Restaurante Teste");
        assertThat(relatorio.get(0).getTotalVendas()).isEqualByComparingTo("100.00");
        assertThat(relatorio.get(0).getQuantidadePedidos()).isEqualTo(1L);
    }

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
