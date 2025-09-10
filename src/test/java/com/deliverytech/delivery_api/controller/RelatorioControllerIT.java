package com.deliverytech.delivery_api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.deliverytech.delivery_api.BaseIntegrationTest;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.model.StatusPedido;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class RelatorioControllerIT extends BaseIntegrationTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private RestauranteRepository restauranteRepository;
  @Autowired private ProdutoRepository produtoRepository;
  @Autowired private ClienteRepository clienteRepository;
  @Autowired private PedidoRepository pedidoRepository;

  @BeforeEach
  void setup() {
    pedidoRepository.deleteAll();
    // remover produtos antes de restaurantes para evitar violação de FK
    produtoRepository.deleteAll();
    restauranteRepository.deleteAll();
    clienteRepository.deleteAll();
  }

  @Test
  void testVendasPorRestauranteEndpoint() throws Exception {
    Restaurante r = new Restaurante();
    r.setNome("Restaurante IT");
    r.setCategoria("Teste");
    r.setAtivo(true);
    r.setAvaliacao(new BigDecimal("4.5"));
    restauranteRepository.save(r);
    Cliente c = new Cliente();
    c.setNome("Cliente IT");
    c.setEmail("clienteit@email.com");
    c.setAtivo(true);
    clienteRepository.save(c);
    Pedido p = new Pedido();
    p.setCliente(c);
    p.setRestaurante(r);
    p.setStatus(StatusPedido.CRIADO);
    p.setDataPedido(LocalDateTime.now());
    p.setValorTotal(new BigDecimal("200.00"));
    pedidoRepository.save(p);
    String inicio = LocalDate.now().minusDays(1).toString();
    String fim = LocalDate.now().plusDays(1).toString();
    mockMvc
        .perform(
            get("/api/relatorios/vendas-por-restaurante")
                .param("inicio", inicio)
                .param("fim", fim)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].nomeRestaurante").value("Restaurante IT"))
        .andExpect(jsonPath("$[0].totalVendas").value(200.00))
        .andExpect(jsonPath("$[0].quantidadePedidos").value(1));
  }
}
