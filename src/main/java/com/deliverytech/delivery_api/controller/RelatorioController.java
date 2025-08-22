package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.service.RelatorioService;
import com.deliverytech.delivery_api.projection.RelatorioVendas;
import com.deliverytech.delivery_api.projection.RelatorioVendasClientes;
import com.deliverytech.delivery_api.projection.RelatorioVendasProdutos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {
    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/vendas-por-restaurante")
    public List<RelatorioVendas> vendasPorRestaurante(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return relatorioService.relatorioVendasPorRestaurante(inicio, fim);
    }

    @GetMapping("/produtos-mais-vendidos")
    public List<RelatorioVendasProdutos> produtosMaisVendidos(
            @RequestParam(defaultValue = "5") int limite,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return relatorioService.relatorioProdutosMaisVendidos(limite, inicio, fim);
    }

    @GetMapping("/clientes-ativos")
    public List<RelatorioVendasClientes> clientesAtivos(
            @RequestParam(defaultValue = "10") int limite,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return relatorioService.relatorioClientesAtivos(limite, inicio, fim);
    }

    @GetMapping("/pedidos-por-periodo")
    public List<Map<String, Object>> pedidosPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam String status) {
        return relatorioService.relatorioPedidosPorPeriodo(inicio, fim, status);
    }

    @GetMapping("/faturamento-por-categoria")
    public List<Map<String, Object>> faturamentoPorCategoria(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return relatorioService.faturamentoPorCategoria(inicio, fim);
    }

    @GetMapping("/resumo-vendas")
    public Map<String, Object> resumoVendas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return relatorioService.resumoVendas(inicio, fim);
    }
}
