package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.Pedido;
// ...existing code...
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.service.impl.RelatorioServiceImpl;
import com.deliverytech.delivery_api.projection.RelatorioVendas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.time.LocalDate;
// ...existing code...
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RelatorioServiceImplTest {
    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @InjectMocks
    private RelatorioServiceImpl relatorioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRelatorioVendasPorRestaurante() {
        RelatorioVendas relatorio = new RelatorioVendas() {
            public String getNomeRestaurante() { return "Restaurante Teste"; }
            public BigDecimal getTotalVendas() { return new BigDecimal("100.00"); }
            public Long getQuantidadePedidos() { return 1L; }
        };
        when(pedidoRepository.calcularTotalVendasPorRestaurante()).thenReturn(List.of(relatorio));
        List<RelatorioVendas> result = relatorioService.relatorioVendasPorRestaurante(LocalDate.now(), LocalDate.now());
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getNomeRestaurante()).isEqualTo("Restaurante Teste");
    }

    @Test
    void testResumoVendas() {
        Pedido p = new Pedido();
        p.setValorTotal(new BigDecimal("50.00"));
        when(pedidoRepository.findByDataPedidoBetween(any(), any())).thenReturn(List.of(p));
        Map<String, Object> resumo = relatorioService.resumoVendas(LocalDate.now(), LocalDate.now());
        assertThat(resumo.get("totalPedidos")).isEqualTo(1);
        assertThat((Double)resumo.get("valorTotalVendas")).isEqualTo(50.00);
    }
}
