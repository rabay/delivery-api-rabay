package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.projection.RelatorioVendas;
import com.deliverytech.delivery_api.projection.RelatorioVendasClientes;
import com.deliverytech.delivery_api.projection.RelatorioVendasProdutos;
import com.deliverytech.delivery_api.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
// OpenAPI imports removidos pois não são usados diretamente neste controlador
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
// OffsetDateTime/List não utilizados neste controlador
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/relatorios", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Relatórios", description = "Relatórios e estatísticas de vendas e pedidos")
public class RelatorioController {
  @Autowired private RelatorioService relatorioService;

  @GetMapping("/vendas-por-restaurante")
  @Operation(
      summary = "Relatório de vendas por restaurante",
      description = "Retorna relatório de vendas agrupadas por restaurante no período especificado")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Relatório gerado com sucesso",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        implementation =
                            com.deliverytech.delivery_api.dto.response.ApiResultRelatorioVendasPaged
                                .class)))
  })
  @Parameters({
    @Parameter(name = "page", description = "Índice da página (0-based)", example = "0"),
    @Parameter(name = "size", description = "Tamanho da página", example = "20")
  })
  public ResponseEntity<
          com.deliverytech.delivery_api.dto.response.ApiResult<
              com.deliverytech.delivery_api.dto.response.PagedResponse<RelatorioVendas>>>
      vendasPorRestaurante(
          @Parameter(description = "Data inicial do período", required = true)
              @RequestParam
              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
              LocalDate inicio,
          @Parameter(description = "Data final do período", required = true)
              @RequestParam
              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
              LocalDate fim,
          @RequestParam(name = "page", required = false, defaultValue = "0") int page,
          @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
    var data = relatorioService.relatorioVendasPorRestaurante(inicio, fim);
    var pageable =
        org.springframework.data.domain.PageRequest.of(Math.max(0, page), Math.max(1, size));
    var total = data == null ? 0 : data.size();
    var from = Math.min(pageable.getPageNumber() * pageable.getPageSize(), total);
    var to = Math.min(from + pageable.getPageSize(), total);
    var pageList =
        data == null || total == 0 ? java.util.List.<RelatorioVendas>of() : data.subList(from, to);
        var pageImpl =
                new org.springframework.data.domain.PageImpl<RelatorioVendas>(pageList, pageable, total);
        var uriBuilder = org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest();
        java.util.Map<String, String> links = new java.util.HashMap<>();
        links.put("first", uriBuilder.replaceQueryParam("page", 0).build().toUriString());
        int lastPage = Math.max(0, pageImpl.getTotalPages() - 1);
        links.put("last", uriBuilder.replaceQueryParam("page", lastPage).build().toUriString());
        if (pageImpl.hasNext()) {
            links.put("next", uriBuilder.replaceQueryParam("page", pageImpl.getNumber() + 1).build().toUriString());
        }
        if (pageImpl.hasPrevious()) {
            links.put("prev", uriBuilder.replaceQueryParam("page", pageImpl.getNumber() - 1).build().toUriString());
        }
        var paged =
                new com.deliverytech.delivery_api.dto.response.PagedResponse<>(
                        pageImpl.getContent(),
                        pageImpl.getTotalElements(),
                        pageImpl.getNumber(),
                        pageImpl.getSize(),
                        pageImpl.getTotalPages(),
                        links,
                        "Relatório gerado com sucesso",
                        true);
    return ResponseEntity.ok(
        new com.deliverytech.delivery_api.dto.response.ApiResult<>(
            paged, "Relatório gerado com sucesso", true));
  }

  @GetMapping("/produtos-mais-vendidos")
  @Operation(
      summary = "Produtos mais vendidos",
      description = "Retorna lista dos produtos mais vendidos no período especificado")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Relatório gerado com sucesso",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        implementation =
                            com.deliverytech.delivery_api.dto.response
                                .ApiResultRelatorioProdutosPaged.class)))
  })
  @Parameters({
    @Parameter(name = "page", description = "Índice da página (0-based)", example = "0"),
    @Parameter(name = "size", description = "Tamanho da página", example = "20")
  })
  public ResponseEntity<
          com.deliverytech.delivery_api.dto.response.ApiResult<
              com.deliverytech.delivery_api.dto.response.PagedResponse<RelatorioVendasProdutos>>>
      produtosMaisVendidos(
          @Parameter(description = "Número máximo de produtos a retornar", required = false)
              @RequestParam(defaultValue = "5")
              int limite,
          @Parameter(description = "Data inicial do período", required = true)
              @RequestParam
              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
              LocalDate inicio,
          @Parameter(description = "Data final do período", required = true)
              @RequestParam
              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
              LocalDate fim,
          @RequestParam(name = "page", required = false, defaultValue = "0") int page,
          @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
    var data = relatorioService.relatorioProdutosMaisVendidos(limite, inicio, fim);
    var pageable =
        org.springframework.data.domain.PageRequest.of(Math.max(0, page), Math.max(1, size));
    var total = data == null ? 0 : data.size();
    var from = Math.min(pageable.getPageNumber() * pageable.getPageSize(), total);
    var to = Math.min(from + pageable.getPageSize(), total);
    var pageList =
        data == null || total == 0
            ? java.util.List.<RelatorioVendasProdutos>of()
            : data.subList(from, to);
        var pageImpl =
                new org.springframework.data.domain.PageImpl<RelatorioVendasProdutos>(
                        pageList, pageable, total);
        var uriBuilder2 = org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest();
        java.util.Map<String, String> links2 = new java.util.HashMap<>();
        links2.put("first", uriBuilder2.replaceQueryParam("page", 0).build().toUriString());
        int lastPage2 = Math.max(0, pageImpl.getTotalPages() - 1);
        links2.put("last", uriBuilder2.replaceQueryParam("page", lastPage2).build().toUriString());
        if (pageImpl.hasNext()) {
            links2.put("next", uriBuilder2.replaceQueryParam("page", pageImpl.getNumber() + 1).build().toUriString());
        }
        if (pageImpl.hasPrevious()) {
            links2.put("prev", uriBuilder2.replaceQueryParam("page", pageImpl.getNumber() - 1).build().toUriString());
        }
        var paged =
                new com.deliverytech.delivery_api.dto.response.PagedResponse<>(
                        pageImpl.getContent(),
                        pageImpl.getTotalElements(),
                        pageImpl.getNumber(),
                        pageImpl.getSize(),
                        pageImpl.getTotalPages(),
                        links2,
                        "Relatório gerado com sucesso",
                        true);
    return ResponseEntity.ok(
        new com.deliverytech.delivery_api.dto.response.ApiResult<>(
            paged, "Relatório gerado com sucesso", true));
  }

  @GetMapping("/clientes-ativos")
  @Operation(
      summary = "Clientes mais ativos",
      description = "Retorna lista dos clientes que mais fizeram pedidos no período especificado")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Relatório gerado com sucesso",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        implementation =
                            com.deliverytech.delivery_api.dto.response
                                .ApiResultRelatorioClientesPaged.class)))
  })
  @Parameters({
    @Parameter(name = "page", description = "Índice da página (0-based)", example = "0"),
    @Parameter(name = "size", description = "Tamanho da página", example = "20")
  })
  public ResponseEntity<
          com.deliverytech.delivery_api.dto.response.ApiResult<
              com.deliverytech.delivery_api.dto.response.PagedResponse<RelatorioVendasClientes>>>
      clientesAtivos(
          @Parameter(description = "Número máximo de clientes a retornar", required = false)
              @RequestParam(defaultValue = "10")
              int limite,
          @Parameter(description = "Data inicial do período", required = true)
              @RequestParam
              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
              LocalDate inicio,
          @Parameter(description = "Data final do período", required = true)
              @RequestParam
              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
              LocalDate fim,
          @RequestParam(name = "page", required = false, defaultValue = "0") int page,
          @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
    var data = relatorioService.relatorioClientesAtivos(limite, inicio, fim);
    var pageable =
        org.springframework.data.domain.PageRequest.of(Math.max(0, page), Math.max(1, size));
    var total = data == null ? 0 : data.size();
    var from = Math.min(pageable.getPageNumber() * pageable.getPageSize(), total);
    var to = Math.min(from + pageable.getPageSize(), total);
    var pageList =
        data == null || total == 0
            ? java.util.List.<RelatorioVendasClientes>of()
            : data.subList(from, to);
        var pageImpl =
                new org.springframework.data.domain.PageImpl<RelatorioVendasClientes>(
                        pageList, pageable, total);
        var uriBuilder3 = org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest();
        java.util.Map<String, String> links3 = new java.util.HashMap<>();
        links3.put("first", uriBuilder3.replaceQueryParam("page", 0).build().toUriString());
        int lastPage3 = Math.max(0, pageImpl.getTotalPages() - 1);
        links3.put("last", uriBuilder3.replaceQueryParam("page", lastPage3).build().toUriString());
        if (pageImpl.hasNext()) {
            links3.put("next", uriBuilder3.replaceQueryParam("page", pageImpl.getNumber() + 1).build().toUriString());
        }
        if (pageImpl.hasPrevious()) {
            links3.put("prev", uriBuilder3.replaceQueryParam("page", pageImpl.getNumber() - 1).build().toUriString());
        }
        var paged =
                new com.deliverytech.delivery_api.dto.response.PagedResponse<>(
                        pageImpl.getContent(),
                        pageImpl.getTotalElements(),
                        pageImpl.getNumber(),
                        pageImpl.getSize(),
                        pageImpl.getTotalPages(),
                        links3,
                        "Relatório gerado com sucesso",
                        true);
    return ResponseEntity.ok(
        new com.deliverytech.delivery_api.dto.response.ApiResult<>(
            paged, "Relatório gerado com sucesso", true));
  }

  @GetMapping("/pedidos-por-periodo")
  @Operation(
      summary = "Pedidos por período e status",
      description = "Retorna pedidos filtrados por período e status")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Relatório gerado com sucesso",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        implementation =
                            com.deliverytech.delivery_api.dto.response.ApiResultMapPaged.class)))
  })
  @Parameters({
    @Parameter(name = "page", description = "Índice da página (0-based)", example = "0"),
    @Parameter(name = "size", description = "Tamanho da página", example = "20")
  })
  public ResponseEntity<
          com.deliverytech.delivery_api.dto.response.ApiResult<
              com.deliverytech.delivery_api.dto.response.PagedResponse<
                  java.util.Map<String, Object>>>>
      pedidosPorPeriodo(
          @Parameter(description = "Data inicial do período", required = true)
              @RequestParam
              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
              LocalDate inicio,
          @Parameter(description = "Data final do período", required = true)
              @RequestParam
              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
              LocalDate fim,
          @Parameter(description = "Status do pedido", required = true) @RequestParam String status,
          @RequestParam(name = "page", required = false, defaultValue = "0") int page,
          @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
    var data = relatorioService.relatorioPedidosPorPeriodo(inicio, fim, status);
    var pageable =
        org.springframework.data.domain.PageRequest.of(Math.max(0, page), Math.max(1, size));
    var total = data == null ? 0 : data.size();
    var from = Math.min(pageable.getPageNumber() * pageable.getPageSize(), total);
    var to = Math.min(from + pageable.getPageSize(), total);
    var pageList =
        data == null || total == 0
            ? java.util.List.<Map<String, Object>>of()
            : data.subList(from, to);
        var pageImpl =
                new org.springframework.data.domain.PageImpl<java.util.Map<String, Object>>(
                        pageList, pageable, total);
        var uriBuilder4 = org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest();
        java.util.Map<String, String> links4 = new java.util.HashMap<>();
        links4.put("first", uriBuilder4.replaceQueryParam("page", 0).build().toUriString());
        int lastPage4 = Math.max(0, pageImpl.getTotalPages() - 1);
        links4.put("last", uriBuilder4.replaceQueryParam("page", lastPage4).build().toUriString());
        if (pageImpl.hasNext()) {
            links4.put("next", uriBuilder4.replaceQueryParam("page", pageImpl.getNumber() + 1).build().toUriString());
        }
        if (pageImpl.hasPrevious()) {
            links4.put("prev", uriBuilder4.replaceQueryParam("page", pageImpl.getNumber() - 1).build().toUriString());
        }
        var paged =
                new com.deliverytech.delivery_api.dto.response.PagedResponse<>(
                        pageImpl.getContent(),
                        pageImpl.getTotalElements(),
                        pageImpl.getNumber(),
                        pageImpl.getSize(),
                        pageImpl.getTotalPages(),
                        links4,
                        "Relatório gerado com sucesso",
                        true);
    return ResponseEntity.ok(
        new com.deliverytech.delivery_api.dto.response.ApiResult<>(
            paged, "Relatório gerado com sucesso", true));
  }

  @GetMapping("/faturamento-por-categoria")
  @Operation(
      summary = "Faturamento por categoria",
      description = "Retorna faturamento agrupado por categoria de produto no período especificado")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Relatório gerado com sucesso",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        implementation =
                            com.deliverytech.delivery_api.dto.response.ApiResultMapPaged.class)))
  })
  @Parameters({
    @Parameter(name = "page", description = "Índice da página (0-based)", example = "0"),
    @Parameter(name = "size", description = "Tamanho da página", example = "20")
  })
  public ResponseEntity<
          com.deliverytech.delivery_api.dto.response.ApiResult<
              com.deliverytech.delivery_api.dto.response.PagedResponse<
                  java.util.Map<String, Object>>>>
      faturamentoPorCategoria(
          @Parameter(description = "Data inicial do período", required = true)
              @RequestParam
              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
              LocalDate inicio,
          @Parameter(description = "Data final do período", required = true)
              @RequestParam
              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
              LocalDate fim,
          @RequestParam(name = "page", required = false, defaultValue = "0") int page,
          @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
    var data = relatorioService.faturamentoPorCategoria(inicio, fim);
    var pageable =
        org.springframework.data.domain.PageRequest.of(Math.max(0, page), Math.max(1, size));
    var total = data == null ? 0 : data.size();
    var from = Math.min(pageable.getPageNumber() * pageable.getPageSize(), total);
    var to = Math.min(from + pageable.getPageSize(), total);
    var pageList =
        data == null || total == 0
            ? java.util.List.<Map<String, Object>>of()
            : data.subList(from, to);
        var pageImpl =
                new org.springframework.data.domain.PageImpl<java.util.Map<String, Object>>(
                        pageList, pageable, total);
        var uriBuilder5 = org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest();
        java.util.Map<String, String> links5 = new java.util.HashMap<>();
        links5.put("first", uriBuilder5.replaceQueryParam("page", 0).build().toUriString());
        int lastPage5 = Math.max(0, pageImpl.getTotalPages() - 1);
        links5.put("last", uriBuilder5.replaceQueryParam("page", lastPage5).build().toUriString());
        if (pageImpl.hasNext()) {
            links5.put("next", uriBuilder5.replaceQueryParam("page", pageImpl.getNumber() + 1).build().toUriString());
        }
        if (pageImpl.hasPrevious()) {
            links5.put("prev", uriBuilder5.replaceQueryParam("page", pageImpl.getNumber() - 1).build().toUriString());
        }
        var paged =
                new com.deliverytech.delivery_api.dto.response.PagedResponse<>(
                        pageImpl.getContent(),
                        pageImpl.getTotalElements(),
                        pageImpl.getNumber(),
                        pageImpl.getSize(),
                        pageImpl.getTotalPages(),
                        links5,
                        "Relatório gerado com sucesso",
                        true);
    return ResponseEntity.ok(
        new com.deliverytech.delivery_api.dto.response.ApiResult<>(
            paged, "Relatório gerado com sucesso", true));
  }

  @GetMapping("/resumo-vendas")
  @Operation(
      summary = "Resumo de vendas",
      description = "Retorna um resumo geral das vendas no período especificado")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "Resumo gerado com sucesso")})
  public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<Map<String, Object>>>
      resumoVendas(
          @Parameter(description = "Data inicial do período", required = true)
              @RequestParam
              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
              LocalDate inicio,
          @Parameter(description = "Data final do período", required = true)
              @RequestParam
              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
              LocalDate fim) {
    var data = relatorioService.resumoVendas(inicio, fim);
    return ResponseEntity.ok(
        new com.deliverytech.delivery_api.dto.response.ApiResult<>(
            data, "Resumo gerado com sucesso", true));
  }
}
