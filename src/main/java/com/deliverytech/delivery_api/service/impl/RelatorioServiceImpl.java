package com.deliverytech.delivery_api.service.impl;

import com.deliverytech.delivery_api.projection.RelatorioVendas;
import com.deliverytech.delivery_api.projection.RelatorioVendasClientes;
import com.deliverytech.delivery_api.projection.RelatorioVendasProdutos;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.service.RelatorioService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@Transactional(readOnly = true)
public class RelatorioServiceImpl implements RelatorioService {
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public List<RelatorioVendas> relatorioVendasPorRestaurante(LocalDate dataInicio, LocalDate dataFim) {
        return pedidoRepository.calcularTotalVendasPorRestaurante();
    }

    @Override
    public List<RelatorioVendasProdutos> relatorioProdutosMaisVendidos(int limite, LocalDate dataInicio, LocalDate dataFim) {
        return produtoRepository.produtosMaisVendidos();
    }

    @Override
    public List<RelatorioVendasClientes> relatorioClientesAtivos(int limite, LocalDate dataInicio, LocalDate dataFim) {
        return clienteRepository.rankingClientesPorPedidos();
    }

    @Override
    public List<Map<String, Object>> relatorioPedidosPorPeriodo(LocalDate dataInicio, LocalDate dataFim, String status) {
        // Exemplo simplificado: pode ser adaptado para usar consulta customizada
        List<Map<String, Object>> result = new java.util.ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("periodo", dataInicio + " a " + dataFim);
        map.put("status", status);
        map.put("totalPedidos", pedidoRepository.findByStatusAndDataPedidoBetween(
            com.deliverytech.delivery_api.model.StatusPedido.valueOf(status),
            dataInicio.atStartOfDay(), dataFim.atTime(23,59,59)).size());
        result.add(map);
        return result;
    }

    @Override
    public List<Map<String, Object>> faturamentoPorCategoria(LocalDate dataInicio, LocalDate dataFim) {
        List<Object[]> dados = produtoRepository.faturamentoPorCategoria();
        List<Map<String, Object>> result = new java.util.ArrayList<>();
        for (Object[] row : dados) {
            Map<String, Object> map = new HashMap<>();
            map.put("categoria", row[0]);
            map.put("totalFaturado", row[1]);
            result.add(map);
        }
        return result;
    }

    @Override
    public Map<String, Object> resumoVendas(LocalDate dataInicio, LocalDate dataFim) {
        Map<String, Object> resumo = new HashMap<>();
        resumo.put("totalPedidos", pedidoRepository.findByDataPedidoBetween(dataInicio.atStartOfDay(), dataFim.atTime(23,59,59)).size());
        resumo.put("valorTotalVendas", pedidoRepository.findByDataPedidoBetween(dataInicio.atStartOfDay(), dataFim.atTime(23,59,59)).stream().mapToDouble(p -> p.getValorTotal() != null ? p.getValorTotal().doubleValue() : 0.0).sum());
        return resumo;
    }
}
