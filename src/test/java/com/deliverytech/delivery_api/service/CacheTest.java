package com.deliverytech.delivery_api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.deliverytech.delivery_api.projection.RelatorioVendas;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.service.impl.RelatorioServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CacheTest {

  @Mock private PedidoRepository pedidoRepository;

  @InjectMocks private RelatorioServiceImpl relatorioService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testRelatorioVendasPorRestauranteMethodCall() {
    // Given
    LocalDate dataInicio = LocalDate.now();
    LocalDate dataFim = LocalDate.now().plusDays(1);

    RelatorioVendas relatorio =
        new RelatorioVendas() {
          public String getNomeRestaurante() {
            return "Restaurante Teste";
          }

          public BigDecimal getTotalVendas() {
            return new BigDecimal("100.00");
          }

          public Long getQuantidadePedidos() {
            return 1L;
          }
        };

    when(pedidoRepository.calcularTotalVendasPorRestaurante(any(), any()))
        .thenReturn(List.of(relatorio));

    // When - Call the method
    List<RelatorioVendas> result =
        relatorioService.relatorioVendasPorRestaurante(dataInicio, dataFim);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getNomeRestaurante()).isEqualTo("Restaurante Teste");

    // Verify repository method was called
    verify(pedidoRepository, times(1)).calcularTotalVendasPorRestaurante(any(), any());
  }
}
