package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.projection.RelatorioVendas;
import com.deliverytech.delivery_api.projection.RelatorioVendasClientes;
import com.deliverytech.delivery_api.projection.RelatorioVendasProdutos;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface RelatorioService {
  List<RelatorioVendas> relatorioVendasPorRestaurante(LocalDate dataInicio, LocalDate dataFim);

  List<RelatorioVendasProdutos> relatorioProdutosMaisVendidos(
      int limite, LocalDate dataInicio, LocalDate dataFim);

  List<RelatorioVendasClientes> relatorioClientesAtivos(
      int limite, LocalDate dataInicio, LocalDate dataFim);

  List<Map<String, Object>> relatorioPedidosPorPeriodo(
      LocalDate dataInicio, LocalDate dataFim, String status);

  List<Map<String, Object>> faturamentoPorCategoria(LocalDate dataInicio, LocalDate dataFim);

  Map<String, Object> resumoVendas(LocalDate dataInicio, LocalDate dataFim);
}
