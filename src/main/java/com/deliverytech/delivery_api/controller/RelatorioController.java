package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.projection.RelatorioVendas;
import com.deliverytech.delivery_api.projection.RelatorioVendasClientes;
import com.deliverytech.delivery_api.projection.RelatorioVendasProdutos;
import com.deliverytech.delivery_api.service.RelatorioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
// OpenAPI imports removidos pois não são usados diretamente neste controlador
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
// OffsetDateTime/List não utilizados neste controlador
import java.util.Map;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/relatorios")
@Tag(name = "Relatórios", description = "Relatórios e estatísticas de vendas e pedidos")
public class RelatorioController {
    @Autowired private RelatorioService relatorioService;

    @GetMapping("/vendas-por-restaurante")
    @Operation(summary = "Relatório de vendas por restaurante", 
               description = "Retorna relatório de vendas agrupadas por restaurante no período especificado")
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.deliverytech.delivery_api.dto.response.ApiResultRelatorioVendasPaged.class)))
    })
    @Parameters({
        @Parameter(name = "page", description = "Índice da página (0-based)", example = "0"),
        @Parameter(name = "size", description = "Tamanho da página", example = "20")
    })
    public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<com.deliverytech.delivery_api.dto.response.PagedResponse<RelatorioVendas>>> vendasPorRestaurante(
            @Parameter(description = "Data inicial do período", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @Parameter(description = "Data final do período", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
        var data = relatorioService.relatorioVendasPorRestaurante(inicio, fim);
        var pageable = org.springframework.data.domain.PageRequest.of(Math.max(0, page), Math.max(1, size));
        var total = data == null ? 0 : data.size();
        var from = Math.min(pageable.getPageNumber() * pageable.getPageSize(), total);
        var to = Math.min(from + pageable.getPageSize(), total);
    var pageList = data == null || total == 0 ? java.util.List.<RelatorioVendas>of() : data.subList(from, to);
        var pageImpl = new org.springframework.data.domain.PageImpl<RelatorioVendas>(pageList, pageable, total);
        var paged = new com.deliverytech.delivery_api.dto.response.PagedResponse<>(pageImpl.getContent(), pageImpl.getTotalElements(), pageImpl.getNumber(), pageImpl.getSize(), "Relatório gerado com sucesso", true);
        return ResponseEntity.ok(new com.deliverytech.delivery_api.dto.response.ApiResult<>(paged, "Relatório gerado com sucesso", true));
    }

    @GetMapping("/produtos-mais-vendidos")
    @Operation(summary = "Produtos mais vendidos", 
               description = "Retorna lista dos produtos mais vendidos no período especificado")
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.deliverytech.delivery_api.dto.response.ApiResultRelatorioProdutosPaged.class)))
    })
    @Parameters({
        @Parameter(name = "page", description = "Índice da página (0-based)", example = "0"),
        @Parameter(name = "size", description = "Tamanho da página", example = "20")
    })
    public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<com.deliverytech.delivery_api.dto.response.PagedResponse<RelatorioVendasProdutos>>> produtosMaisVendidos(
            @Parameter(description = "Número máximo de produtos a retornar", required = false)
            @RequestParam(defaultValue = "5") int limite,
            @Parameter(description = "Data inicial do período", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @Parameter(description = "Data final do período", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
        var data = relatorioService.relatorioProdutosMaisVendidos(limite, inicio, fim);
        var pageable = org.springframework.data.domain.PageRequest.of(Math.max(0, page), Math.max(1, size));
        var total = data == null ? 0 : data.size();
        var from = Math.min(pageable.getPageNumber() * pageable.getPageSize(), total);
        var to = Math.min(from + pageable.getPageSize(), total);
    var pageList = data == null || total == 0 ? java.util.List.<RelatorioVendasProdutos>of() : data.subList(from, to);
        var pageImpl = new org.springframework.data.domain.PageImpl<RelatorioVendasProdutos>(pageList, pageable, total);
        var paged = new com.deliverytech.delivery_api.dto.response.PagedResponse<>(pageImpl.getContent(), pageImpl.getTotalElements(), pageImpl.getNumber(), pageImpl.getSize(), "Relatório gerado com sucesso", true);
        return ResponseEntity.ok(new com.deliverytech.delivery_api.dto.response.ApiResult<>(paged, "Relatório gerado com sucesso", true));
    }

    @GetMapping("/clientes-ativos")
    @Operation(summary = "Clientes mais ativos", 
               description = "Retorna lista dos clientes que mais fizeram pedidos no período especificado")
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.deliverytech.delivery_api.dto.response.ApiResultRelatorioClientesPaged.class)))
    })
    @Parameters({
        @Parameter(name = "page", description = "Índice da página (0-based)", example = "0"),
        @Parameter(name = "size", description = "Tamanho da página", example = "20")
    })
    public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<com.deliverytech.delivery_api.dto.response.PagedResponse<RelatorioVendasClientes>>> clientesAtivos(
            @Parameter(description = "Número máximo de clientes a retornar", required = false)
            @RequestParam(defaultValue = "10") int limite,
            @Parameter(description = "Data inicial do período", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @Parameter(description = "Data final do período", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
        var data = relatorioService.relatorioClientesAtivos(limite, inicio, fim);
        var pageable = org.springframework.data.domain.PageRequest.of(Math.max(0, page), Math.max(1, size));
        var total = data == null ? 0 : data.size();
        var from = Math.min(pageable.getPageNumber() * pageable.getPageSize(), total);
        var to = Math.min(from + pageable.getPageSize(), total);
    var pageList = data == null || total == 0 ? java.util.List.<RelatorioVendasClientes>of() : data.subList(from, to);
        var pageImpl = new org.springframework.data.domain.PageImpl<RelatorioVendasClientes>(pageList, pageable, total);
        var paged = new com.deliverytech.delivery_api.dto.response.PagedResponse<>(pageImpl.getContent(), pageImpl.getTotalElements(), pageImpl.getNumber(), pageImpl.getSize(), "Relatório gerado com sucesso", true);
        return ResponseEntity.ok(new com.deliverytech.delivery_api.dto.response.ApiResult<>(paged, "Relatório gerado com sucesso", true));
    }

    @GetMapping("/pedidos-por-periodo")
    @Operation(summary = "Pedidos por período e status", 
               description = "Retorna pedidos filtrados por período e status")
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.deliverytech.delivery_api.dto.response.ApiResultMapPaged.class)))
    })
    @Parameters({
        @Parameter(name = "page", description = "Índice da página (0-based)", example = "0"),
        @Parameter(name = "size", description = "Tamanho da página", example = "20")
    })
    public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<com.deliverytech.delivery_api.dto.response.PagedResponse<java.util.Map<String, Object>>>> pedidosPorPeriodo(
            @Parameter(description = "Data inicial do período", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @Parameter(description = "Data final do período", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @Parameter(description = "Status do pedido", required = true)
            @RequestParam String status,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
        var data = relatorioService.relatorioPedidosPorPeriodo(inicio, fim, status);
        var pageable = org.springframework.data.domain.PageRequest.of(Math.max(0, page), Math.max(1, size));
        var total = data == null ? 0 : data.size();
        var from = Math.min(pageable.getPageNumber() * pageable.getPageSize(), total);
        var to = Math.min(from + pageable.getPageSize(), total);
    var pageList = data == null || total == 0 ? java.util.List.<Map<String, Object>>of() : data.subList(from, to);
        var pageImpl = new org.springframework.data.domain.PageImpl<java.util.Map<String, Object>>(pageList, pageable, total);
        var paged = new com.deliverytech.delivery_api.dto.response.PagedResponse<>(pageImpl.getContent(), pageImpl.getTotalElements(), pageImpl.getNumber(), pageImpl.getSize(), "Relatório gerado com sucesso", true);
        return ResponseEntity.ok(new com.deliverytech.delivery_api.dto.response.ApiResult<>(paged, "Relatório gerado com sucesso", true));
    }

    @GetMapping("/faturamento-por-categoria")
    @Operation(summary = "Faturamento por categoria", 
               description = "Retorna faturamento agrupado por categoria de produto no período especificado")
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.deliverytech.delivery_api.dto.response.ApiResultMapPaged.class)))
    })
    @Parameters({
        @Parameter(name = "page", description = "Índice da página (0-based)", example = "0"),
        @Parameter(name = "size", description = "Tamanho da página", example = "20")
    })
    public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<com.deliverytech.delivery_api.dto.response.PagedResponse<java.util.Map<String, Object>>>> faturamentoPorCategoria(
            @Parameter(description = "Data inicial do período", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @Parameter(description = "Data final do período", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
        var data = relatorioService.faturamentoPorCategoria(inicio, fim);
        var pageable = org.springframework.data.domain.PageRequest.of(Math.max(0, page), Math.max(1, size));
        var total = data == null ? 0 : data.size();
        var from = Math.min(pageable.getPageNumber() * pageable.getPageSize(), total);
        var to = Math.min(from + pageable.getPageSize(), total);
    var pageList = data == null || total == 0 ? java.util.List.<Map<String, Object>>of() : data.subList(from, to);
        var pageImpl = new org.springframework.data.domain.PageImpl<java.util.Map<String, Object>>(pageList, pageable, total);
        var paged = new com.deliverytech.delivery_api.dto.response.PagedResponse<>(pageImpl.getContent(), pageImpl.getTotalElements(), pageImpl.getNumber(), pageImpl.getSize(), "Relatório gerado com sucesso", true);
        return ResponseEntity.ok(new com.deliverytech.delivery_api.dto.response.ApiResult<>(paged, "Relatório gerado com sucesso", true));
    }

    @GetMapping("/resumo-vendas")
    @Operation(summary = "Resumo de vendas", 
               description = "Retorna um resumo geral das vendas no período especificado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resumo gerado com sucesso")
    })
    public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<Map<String, Object>>> resumoVendas(
            @Parameter(description = "Data inicial do período", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @Parameter(description = "Data final do período", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        var data = relatorioService.resumoVendas(inicio, fim);
    return ResponseEntity.ok(new com.deliverytech.delivery_api.dto.response.ApiResult<>(data, "Resumo gerado com sucesso", true));
    }
}
