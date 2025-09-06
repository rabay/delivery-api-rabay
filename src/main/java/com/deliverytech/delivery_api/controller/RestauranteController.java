package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.request.RestauranteRequest;
import com.deliverytech.delivery_api.dto.request.StatusRequest;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.service.ProdutoService;
import com.deliverytech.delivery_api.service.RestauranteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import java.net.URI;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/restaurantes", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
@Tag(
    name = "Restaurantes",
    description =
        "Gerenciamento de restaurantes, incluindo cadastro, consulta, atualização,"
            + " inativação e busca por categoria.")
public class RestauranteController {
  private final RestauranteService restauranteService;
  private final ProdutoService produtoService;

  public RestauranteController(
      RestauranteService restauranteService, ProdutoService produtoService) {
    this.restauranteService = restauranteService;
    this.produtoService = produtoService;
  }

  @Operation(
      summary = "Cadastrar novo restaurante",
      description = "Cria um novo restaurante ativo no sistema.")
  @PostMapping
  public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<Restaurante>> criar(
      @Valid @RequestBody RestauranteRequest restauranteRequest) {
    Restaurante novo = restauranteService.cadastrar(restauranteRequest);

    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(novo.getId()).toUri();

    return ResponseEntity.created(location)
        .body(
            new com.deliverytech.delivery_api.dto.response.ApiResult<>(
                novo, "Restaurante criado com sucesso", true));
  }

  @Operation(
      summary = "Listar restaurantes ativos",
      description = "Retorna todos os restaurantes ativos cadastrados.")
  @Parameters({
    @io.swagger.v3.oas.annotations.Parameter(
        name = "page",
        description = "Índice da página (0-based)",
        example = "0"),
    @io.swagger.v3.oas.annotations.Parameter(
        name = "size",
        description = "Tamanho da página",
        example = "20")
  })
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Restaurantes paginados (ApiResult<PagedResponse<RestauranteResponse>>)",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        implementation =
                            com.deliverytech.delivery_api.dto.response.ApiResultRestaurantePaged
                                .class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "Parâmetros inválidos"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "Erro interno do servidor")
  })
  @GetMapping
  public ResponseEntity<
          com.deliverytech.delivery_api.dto.response.ApiResult<
              com.deliverytech.delivery_api.dto.response.PagedResponse<
                  com.deliverytech.delivery_api.dto.response.RestauranteResponse>>>
      listar(
          @RequestParam(name = "page", required = false, defaultValue = "0") int page,
          @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
    var pageable =
        org.springframework.data.domain.PageRequest.of(Math.max(0, page), Math.max(1, size));
    var pageResult = restauranteService.buscarRestaurantesDisponiveis(pageable);
    var paged =
        new com.deliverytech.delivery_api.dto.response.PagedResponse<>(
            pageResult.getContent(),
            pageResult.getTotalElements(),
            pageResult.getNumber(),
            pageResult.getSize(),
            "Restaurantes obtidos com sucesso",
            true);
    return ResponseEntity.ok(
        new com.deliverytech.delivery_api.dto.response.ApiResult<>(
            paged, "Restaurantes obtidos com sucesso", true));
  }

  @Operation(
      summary = "Buscar restaurante por ID",
      description = "Consulta um restaurante pelo seu identificador único.")
  @GetMapping("/{id}")
  public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<Restaurante>>
      buscarPorId(@PathVariable Long id) {
    var opt = restauranteService.buscarPorId(id);
    if (opt.isPresent()) {
      return ResponseEntity.ok(
          new com.deliverytech.delivery_api.dto.response.ApiResult<>(
              opt.get(), "Restaurante obtido com sucesso", true));
    }
    return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND)
        .body(
            new com.deliverytech.delivery_api.dto.response.ApiResult<>(
                null, "Restaurante não encontrado", false));
  }

  @Operation(
      summary = "Atualizar restaurante",
      description = "Atualiza os dados de um restaurante existente.")
  @PutMapping("/{id}")
  public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<Restaurante>>
      atualizar(@PathVariable Long id, @Valid @RequestBody RestauranteRequest restauranteRequest) {
    Restaurante atualizado = restauranteService.atualizar(id, restauranteRequest);
    return ResponseEntity.ok(
        new com.deliverytech.delivery_api.dto.response.ApiResult<>(
            atualizado, "Restaurante atualizado com sucesso", true));
  }

  @Operation(
      summary = "Inativar restaurante",
      description = "Inativa um restaurante pelo seu ID, tornando-o indisponível para operações.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Restaurante inativado com sucesso",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        value =
                            "{\"data\":null, \"message\":\"Restaurante inativado com sucesso\","
                                + " \"success\":true}"))),
    @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<Void>> inativar(
      @PathVariable Long id) {
    restauranteService.inativar(id);
    return ResponseEntity.ok(
        new com.deliverytech.delivery_api.dto.response.ApiResult<>(
            null, "Restaurante inativado com sucesso", true));
  }

  @Operation(
      summary = "Buscar restaurantes por categoria",
      description = "Retorna todos os restaurantes ativos de uma determinada categoria.")
  @Parameters({
    @io.swagger.v3.oas.annotations.Parameter(
        name = "page",
        description = "Índice da página (0-based)",
        example = "0"),
    @io.swagger.v3.oas.annotations.Parameter(
        name = "size",
        description = "Tamanho da página",
        example = "20")
  })
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description =
            "Restaurantes paginados por categoria (ApiResult<PagedResponse<RestauranteResponse>>)",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        implementation =
                            com.deliverytech.delivery_api.dto.response.ApiResultRestaurantePaged
                                .class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "Parâmetros inválidos"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "Erro interno do servidor")
  })
  @GetMapping("/categoria/{categoria}")
  public ResponseEntity<
          com.deliverytech.delivery_api.dto.response.ApiResult<
              com.deliverytech.delivery_api.dto.response.PagedResponse<
                  com.deliverytech.delivery_api.dto.response.RestauranteResponse>>>
      buscarPorCategoria(
          @PathVariable String categoria,
          @RequestParam(name = "page", required = false, defaultValue = "0") int page,
          @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
    var pageable =
        org.springframework.data.domain.PageRequest.of(Math.max(0, page), Math.max(1, size));
    var pageResult = restauranteService.buscarRestaurantesPorCategoria(categoria, pageable);
    var paged =
        new com.deliverytech.delivery_api.dto.response.PagedResponse<>(
            pageResult.getContent(),
            pageResult.getTotalElements(),
            pageResult.getNumber(),
            pageResult.getSize(),
            "Restaurantes obtidos por categoria",
            true);
    return ResponseEntity.ok(
        new com.deliverytech.delivery_api.dto.response.ApiResult<>(
            paged, "Restaurantes obtidos por categoria", true));
  }

  @Operation(
      summary = "Ativar/Desativar restaurante",
      description = "Altera o status de ativo do restaurante (ativar/desativar).")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Status atualizado com sucesso",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        value =
                            "{\"data\": {\"id\": 1, \"ativo\": true}, \"message\": \"Status"
                                + " atualizado\", \"success\": true}"))),
    @ApiResponse(responseCode = "400", description = "Requisição inválida"),
    @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  @PatchMapping("/{id}/status")
  public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<Restaurante>>
      alterarStatus(@PathVariable Long id, @RequestBody StatusRequest statusRequest) {
    Restaurante atualizado = restauranteService.alterarStatus(id, statusRequest.getAtivo());
    return ResponseEntity.ok(
        new com.deliverytech.delivery_api.dto.response.ApiResult<>(
            atualizado, "Status atualizado", true));
  }

  @Operation(
      summary = "Calcular taxa de entrega",
      description = "Calcula a taxa de entrega para um restaurante em um CEP específico.")
  @GetMapping("/{id}/taxa-entrega/{cep}")
  public ResponseEntity<
          com.deliverytech.delivery_api.dto.response.ApiResult<java.util.Map<String, Object>>>
      calcularTaxaEntrega(@PathVariable Long id, @PathVariable String cep) {
    try {
      java.math.BigDecimal taxa = restauranteService.calcularTaxaEntrega(id, cep);
      Map<String, Object> data = new java.util.HashMap<>();
      data.put("restauranteId", id);
      data.put("cep", cep);
      data.put("taxaEntrega", taxa);
      var body =
          new com.deliverytech.delivery_api.dto.response.ApiResult<>(
              data, "Taxa de entrega calculada com sucesso", true);
      return ResponseEntity.ok(body);
    } catch (RuntimeException e) {
      return ResponseEntity.status(404)
          .body(
              new com.deliverytech.delivery_api.dto.response.ApiResult<>(
                  null, "Restaurante não encontrado ou indisponível: " + e.getMessage(), false));
    }
  }

  @Operation(
      summary = "Listar restaurantes próximos ao CEP",
      description = "Retorna restaurantes ativos próximos ao CEP informado.")
  @Parameters({
    @io.swagger.v3.oas.annotations.Parameter(
        name = "page",
        description = "Índice da página (0-based)",
        example = "0"),
    @io.swagger.v3.oas.annotations.Parameter(
        name = "size",
        description = "Tamanho da página",
        example = "20")
  })
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description =
            "Restaurantes próximos paginados (ApiResult<PagedResponse<RestauranteResponse>>)",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        implementation =
                            com.deliverytech.delivery_api.dto.response.ApiResultRestaurantePaged
                                .class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "Parâmetros inválidos"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "Erro interno do servidor")
  })
  @GetMapping("/proximos/{cep}")
  public ResponseEntity<
          com.deliverytech.delivery_api.dto.response.ApiResult<
              com.deliverytech.delivery_api.dto.response.PagedResponse<
                  com.deliverytech.delivery_api.dto.response.RestauranteResponse>>>
      buscarProximos(
          @PathVariable String cep,
          @RequestParam(name = "page", required = false, defaultValue = "0") int page,
          @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
    var pageable =
        org.springframework.data.domain.PageRequest.of(Math.max(0, page), Math.max(1, size));
    var pageResult = restauranteService.buscarProximos(cep, pageable);
    var paged =
        new com.deliverytech.delivery_api.dto.response.PagedResponse<>(
            pageResult.getContent(),
            pageResult.getTotalElements(),
            pageResult.getNumber(),
            pageResult.getSize(),
            "Restaurantes próximos obtidos",
            true);
    return ResponseEntity.ok(
        new com.deliverytech.delivery_api.dto.response.ApiResult<>(
            paged, "Restaurantes próximos obtidos", true));
  }

  @Operation(
      summary = "Listar produtos de um restaurante",
      description = "Retorna todos os produtos disponíveis de um restaurante específico.")
  @Parameters({
    @io.swagger.v3.oas.annotations.Parameter(
        name = "page",
        description = "Índice da página (0-based)",
        example = "0"),
    @io.swagger.v3.oas.annotations.Parameter(
        name = "size",
        description = "Tamanho da página",
        example = "20")
  })
  @ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description =
            "Produtos do restaurante paginados (ApiResult<PagedResponse<ProdutoResponse>>)",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        implementation =
                            com.deliverytech.delivery_api.dto.response.ApiResultProdutoPaged
                                .class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "Parâmetros inválidos"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "404",
        description = "Restaurante não encontrado"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "Erro interno do servidor")
  })
  @GetMapping("/{restauranteId}/produtos")
  public ResponseEntity<
          com.deliverytech.delivery_api.dto.response.ApiResult<
              com.deliverytech.delivery_api.dto.response.PagedResponse<
                  com.deliverytech.delivery_api.dto.response.ProdutoResponse>>>
      buscarProdutosPorRestaurante(
          @PathVariable Long restauranteId,
          @RequestParam(name = "page", required = false, defaultValue = "0") int page,
          @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
    try {
      restauranteService
          .buscarPorId(restauranteId)
          .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));
      var pageable =
          org.springframework.data.domain.PageRequest.of(Math.max(0, page), Math.max(1, size));
      var pageResult = produtoService.buscarProdutosPorRestaurante(restauranteId, pageable);
      var paged =
          new com.deliverytech.delivery_api.dto.response.PagedResponse<>(
              pageResult.getContent(),
              pageResult.getTotalElements(),
              pageResult.getNumber(),
              pageResult.getSize(),
              "Produtos do restaurante obtidos",
              true);
      return ResponseEntity.ok(
          new com.deliverytech.delivery_api.dto.response.ApiResult<>(
              paged, "Produtos do restaurante obtidos", true));
    } catch (RuntimeException e) {
      return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND)
          .body(
              new com.deliverytech.delivery_api.dto.response.ApiResult<>(
                  null, "Restaurante não encontrado", false));
    }
  }
}
