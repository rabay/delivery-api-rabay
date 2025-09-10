package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.request.PedidoRequest;
import com.deliverytech.delivery_api.dto.request.StatusUpdateRequest;
import com.deliverytech.delivery_api.dto.response.ApiResult;
import com.deliverytech.delivery_api.dto.response.ItemPedidoResponse;
import com.deliverytech.delivery_api.dto.response.PedidoResponse;
import com.deliverytech.delivery_api.dto.response.PedidoResumoResponse;
import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.StatusPedido;
import com.deliverytech.delivery_api.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(
    value = "/api/pedidos",
    produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
@Tag(
    name = "Pedidos",
    description = "Criação, consulta e atualização de pedidos realizados pelos clientes.")
public class PedidoController {

  private static final Logger logger = LoggerFactory.getLogger(PedidoController.class);

  private final PedidoService pedidoService;

  public PedidoController(PedidoService pedidoService) {
    this.pedidoService = pedidoService;
  }

  @Operation(
      summary = "Listar todos os pedidos",
      description = "Retorna todos os pedidos cadastrados no sistema.")
  @Parameters({
    @Parameter(name = "page", description = "Índice da página (0-based)", example = "0"),
    @Parameter(name = "size", description = "Tamanho da página", example = "20")
  })
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Pedidos paginados (ApiResult<PagedResponse<PedidoResponse>>)",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        implementation =
                            com.deliverytech.delivery_api.dto.response.ApiResult.class))),
    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  @GetMapping
  public ResponseEntity<
          ApiResult<com.deliverytech.delivery_api.dto.response.PagedResponse<PedidoResponse>>>
      listarTodos(
          @RequestParam(name = "page", required = false, defaultValue = "0") int page,
          @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
    try {
      var pageable =
          com.deliverytech.delivery_api.util.PageableUtil.buildPageable(
              page,
              size,
              (String) null,
              com.deliverytech.delivery_api.util.SortableProperties.PEDIDO);
      var pageResult = pedidoService.listarTodos(pageable);

      var uriBuilder = ServletUriComponentsBuilder.fromCurrentRequest();
      java.util.Map<String, String> links = new java.util.HashMap<>();
      links.put("first", uriBuilder.replaceQueryParam("page", 0).build().toUriString());
      int lastPage = Math.max(0, pageResult.getTotalPages() - 1);
      links.put("last", uriBuilder.replaceQueryParam("page", lastPage).build().toUriString());
      if (pageResult.hasNext()) {
        links.put(
            "next",
            uriBuilder.replaceQueryParam("page", pageResult.getNumber() + 1).build().toUriString());
      }
      if (pageResult.hasPrevious()) {
        links.put(
            "prev",
            uriBuilder.replaceQueryParam("page", pageResult.getNumber() - 1).build().toUriString());
      }

      var paged =
          new com.deliverytech.delivery_api.dto.response.PagedResponse<>(
              pageResult.getContent(),
              pageResult.getTotalElements(),
              pageResult.getNumber(),
              pageResult.getSize(),
              pageResult.getTotalPages(),
              links,
              "Pedidos obtidos com sucesso",
              true);
      return ResponseEntity.ok(new ApiResult<>(paged, "Pedidos obtidos com sucesso", true));
    } catch (Exception ex) {
      logger.error("Erro ao listar todos os pedidos: {}", ex.getMessage(), ex);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResult<>(null, "Erro interno: " + ex.getMessage(), false));
    }
  }

  @Operation(
      summary = "Listar todos os pedidos (resumo)",
      description = "Retorna um resumo de todos os pedidos cadastrados no sistema.")
  @Parameters({
    @Parameter(name = "page", description = "Índice da página (0-based)", example = "0"),
    @Parameter(name = "size", description = "Tamanho da página", example = "20")
  })
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description =
            "Resumo de pedidos paginados (ApiResult<PagedResponse<PedidoResumoResponse>>)",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        implementation =
                            com.deliverytech.delivery_api.dto.response.ApiResult.class))),
    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  @GetMapping("/resumo")
  public ResponseEntity<
          ApiResult<com.deliverytech.delivery_api.dto.response.PagedResponse<PedidoResumoResponse>>>
      listarTodosResumo(
          @RequestParam(name = "page", required = false, defaultValue = "0") int page,
          @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
    try {
      var pageable =
          com.deliverytech.delivery_api.util.PageableUtil.buildPageable(
              page,
              size,
              (String) null,
              com.deliverytech.delivery_api.util.SortableProperties.PEDIDO);
      var pageResult = pedidoService.listarTodos(pageable);
      // map to resumo
      var resumoPage =
          pageResult.map(p -> mapToResumoResponse(pedidoService.buscarPorId(p.getId())));

      var uriBuilder = ServletUriComponentsBuilder.fromCurrentRequest();
      java.util.Map<String, String> links = new java.util.HashMap<>();
      links.put("first", uriBuilder.replaceQueryParam("page", 0).build().toUriString());
      int lastPage = Math.max(0, resumoPage.getTotalPages() - 1);
      links.put("last", uriBuilder.replaceQueryParam("page", lastPage).build().toUriString());
      if (resumoPage.hasNext()) {
        links.put(
            "next",
            uriBuilder.replaceQueryParam("page", resumoPage.getNumber() + 1).build().toUriString());
      }
      if (resumoPage.hasPrevious()) {
        links.put(
            "prev",
            uriBuilder.replaceQueryParam("page", resumoPage.getNumber() - 1).build().toUriString());
      }

      var paged =
          new com.deliverytech.delivery_api.dto.response.PagedResponse<>(
              resumoPage.getContent(),
              resumoPage.getTotalElements(),
              resumoPage.getNumber(),
              resumoPage.getSize(),
              resumoPage.getTotalPages(),
              links,
              "Resumo de pedidos obtido",
              true);
      return ResponseEntity.ok(new ApiResult<>(paged, "Resumo de pedidos obtido", true));
    } catch (Exception ex) {
      logger.error("Erro ao listar todos os pedidos (resumo): {}", ex.getMessage(), ex);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResult<>(null, "Erro interno: " + ex.getMessage(), false));
    }
  }

  @Operation(
      summary = "Buscar pedido por ID",
      description = "Retorna um pedido pelo seu identificador.")
  @GetMapping("/{id}")
  public ResponseEntity<ApiResult<PedidoResponse>> buscarPorId(@PathVariable Long id) {
    try {
      Pedido pedido = pedidoService.buscarPorId(id);
      if (pedido == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResult<>(null, "Pedido não encontrado", false));
      }
      return ResponseEntity.ok(
          new ApiResult<>(mapToResponse(pedido), "Pedido obtido com sucesso", true));
    } catch (RuntimeException ex) {
      if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("não encontrado")) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiResult<>(null, "Pedido não encontrado", false));
      }
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ApiResult<>(null, "Requisição inválida: " + ex.getMessage(), false));
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResult<>(null, "Erro interno: " + ex.getMessage(), false));
    }
  }

  @Operation(
      summary = "Criar novo pedido",
      description = "Cria um novo pedido para um cliente em um restaurante.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "201",
        description = "Pedido criado com sucesso",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResult.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Request inválido ou erro de negócio",
        content = @Content(schema = @Schema(implementation = Void.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Erro interno do servidor",
        content = @Content(schema = @Schema(implementation = Void.class)))
  })
  @PostMapping
  public ResponseEntity<ApiResult<PedidoResponse>> criar(
      @Valid @RequestBody PedidoRequest pedidoRequest) {
    try {
      logger.debug("Recebido PedidoRequest: {}", pedidoRequest);
      Pedido pedido = mapToEntity(pedidoRequest);
      logger.debug("Pedido mapeado: {}", pedido);
      Pedido novo = pedidoService.criar(pedido);
      PedidoResponse response = mapToResponse(novo);
      logger.info(
          "Pedido criado com sucesso: id={} status= {}", response.getId(), response.getStatus());
      URI location =
          ServletUriComponentsBuilder.fromCurrentRequest()
              .path("/{id}")
              .buildAndExpand(response.getId())
              .toUri();
      return ResponseEntity.created(location)
          .body(new ApiResult<>(response, "Pedido criado com sucesso", true));
    } catch (RuntimeException ex) {
      logger.error("Erro de negócio ao criar pedido: {}", ex.getMessage(), ex);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ApiResult<>(null, "Erro de negócio: " + ex.getMessage(), false));
    } catch (Exception ex) {
      logger.error("Erro inesperado ao criar pedido: {}", ex.getMessage(), ex);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResult<>(null, "Erro interno: " + ex.getMessage(), false));
    }
  }

  @Operation(
      summary = "Criar novo pedido (DTO)",
      description = "Cria um novo pedido para um cliente em um restaurante usando DTO.")
  @PostMapping("/dto")
  public ResponseEntity<ApiResult<PedidoResponse>> criarPedido(
      @Valid @RequestBody PedidoRequest pedidoRequest) {
    try {
      logger.debug("Recebido PedidoRequest: {}", pedidoRequest);
      PedidoResponse response = pedidoService.criarPedido(pedidoRequest);
      logger.info(
          "Pedido criado com sucesso: id={} status= {}", response.getId(), response.getStatus());
      URI location =
          ServletUriComponentsBuilder.fromCurrentRequest()
              .path("/{id}")
              .buildAndExpand(response.getId())
              .toUri();
      return ResponseEntity.created(location)
          .body(new ApiResult<>(response, "Pedido criado com sucesso", true));
    } catch (RuntimeException ex) {
      logger.error("Erro de negócio ao criar pedido: {}", ex.getMessage(), ex);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ApiResult<>(null, "Erro de negócio: " + ex.getMessage(), false));
    } catch (Exception ex) {
      logger.error("Erro inesperado ao criar pedido: {}", ex.getMessage(), ex);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResult<>(null, "Erro interno: " + ex.getMessage(), false));
    }
  }

  @Operation(
      summary = "Atualizar status do pedido",
      description = "Atualiza o status de um pedido existente (ex: CRIADO, ENTREGUE, CANCELADO).")
  @PutMapping("/{id}/status")
  public ResponseEntity<ApiResult<PedidoResponse>> atualizarStatus(
      @PathVariable Long id, @Valid @RequestBody StatusUpdateRequest statusUpdateRequest) {
    StatusPedido status = StatusPedido.valueOf(statusUpdateRequest.getStatus());
    Pedido atualizado = pedidoService.atualizarStatus(id, status);
    // Buscar novamente o pedido com itens para evitar LazyInitializationException
    var opt = pedidoService.buscarPorIdComItens(atualizado.getId());
    if (opt.isPresent()) {
      var pedidoComItens = opt.get();
      return ResponseEntity.ok(
          new ApiResult<>(mapToResponse(pedidoComItens), "Status atualizado", true));
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ApiResult<>(null, "Pedido não encontrado", false));
  }

  @Operation(
      summary = "Atualizar status do pedido (parcial)",
      description = "Atualiza parcialmente o status de um pedido.")
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
                            "{\"data\": {\"id\": 1, \"status\": \"ENTREGUE\"}, \"message\":"
                                + " \"Status atualizado\", \"success\": true}"))),
    @ApiResponse(responseCode = "400", description = "Status inválido ou requisição inválida"),
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  @PatchMapping("/{id}/status")
  public ResponseEntity<ApiResult<PedidoResponse>> atualizarStatusPatch(
      @PathVariable Long id, @RequestBody StatusUpdateRequest statusUpdateRequest) {
    try {
      StatusPedido novoStatus = StatusPedido.valueOf(statusUpdateRequest.getStatus().toUpperCase());
      Pedido pedidoAtualizado = pedidoService.atualizarStatus(id, novoStatus);
      return ResponseEntity.ok(
          new ApiResult<>(mapToResponse(pedidoAtualizado), "Status atualizado", true));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(new ApiResult<>(null, "Status inválido", false));
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResult<>(null, "Pedido não encontrado", false));
    }
  }

  @Operation(summary = "Cancelar pedido", description = "Cancela um pedido existente.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Pedido cancelado com sucesso",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        value =
                            "{\"data\": {\"id\": 1, \"status\": \"CANCELADO\"}, \"message\":"
                                + " \"Pedido cancelado\", \"success\": true}"))),
    @ApiResponse(responseCode = "400", description = "Erro de negócio ao cancelar pedido"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResult<PedidoResponse>> cancelarPedido(@PathVariable Long id) {
    try {
      Pedido cancelado = pedidoService.cancelar(id);
      PedidoResponse response = mapToResponse(cancelado);
      return ResponseEntity.ok(new ApiResult<>(response, "Pedido cancelado", true));
    } catch (RuntimeException ex) {
      logger.error("Erro ao cancelar pedido {}: {}", id, ex.getMessage(), ex);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ApiResult<>(null, "Erro de negócio: " + ex.getMessage(), false));
    } catch (Exception ex) {
      logger.error("Erro inesperado ao cancelar pedido {}: {}", id, ex.getMessage(), ex);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResult<>(null, "Erro interno: " + ex.getMessage(), false));
    }
  }

  @Operation(
      summary = "Calcular total do pedido",
      description = "Calcula o total de um pedido sem persisti-lo no banco de dados.")
  @PostMapping("/calcular")
  public ResponseEntity<ApiResult<java.util.Map<String, Object>>> calcularTotal(
      @Valid @RequestBody PedidoRequest pedidoRequest) {
    try {
      java.math.BigDecimal total = pedidoService.calcularTotalPedido(pedidoRequest.getItens());
      Map<String, Object> response = new java.util.HashMap<>();
      response.put("total", total);
      response.put("quantidade_itens", pedidoRequest.getItens().size());
      response.put("clienteId", pedidoRequest.getClienteId());
      response.put("restauranteId", pedidoRequest.getRestauranteId());
      return ResponseEntity.ok(new ApiResult<>(response, "Total calculado", true));
    } catch (RuntimeException ex) {
      logger.error("Erro ao calcular total do pedido: {}", ex.getMessage(), ex);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ApiResult<>(null, "Erro de negócio: " + ex.getMessage(), false));
    } catch (Exception ex) {
      logger.error("Erro inesperado ao calcular total do pedido: {}", ex.getMessage(), ex);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApiResult<>(null, "Erro interno: " + ex.getMessage(), false));
    }
  }

  // Métodos utilitários de mapeamento
  private Pedido mapToEntity(PedidoRequest dto) {
    Pedido pedido = new Pedido();
    pedido.setCliente(new com.deliverytech.delivery_api.model.Cliente());
    pedido.getCliente().setId(dto.getClienteId());
    pedido.setRestaurante(new com.deliverytech.delivery_api.model.Restaurante());
    pedido.getRestaurante().setId(dto.getRestauranteId());

    // Convert EnderecoRequest to Endereco
    if (dto.getEnderecoEntrega() != null) {
      com.deliverytech.delivery_api.model.Endereco endereco =
          new com.deliverytech.delivery_api.model.Endereco();
      endereco.setRua(dto.getEnderecoEntrega().getRua());
      endereco.setNumero(dto.getEnderecoEntrega().getNumero());
      endereco.setBairro(dto.getEnderecoEntrega().getBairro());
      endereco.setCidade(dto.getEnderecoEntrega().getCidade());
      endereco.setEstado(dto.getEnderecoEntrega().getEstado());
      endereco.setCep(dto.getEnderecoEntrega().getCep());
      endereco.setComplemento(dto.getEnderecoEntrega().getComplemento());
      pedido.setEnderecoEntrega(endereco);
    }

    pedido.setItens(
        dto.getItens().stream()
            .map(
                itemDto -> {
                  com.deliverytech.delivery_api.model.ItemPedido item =
                      new com.deliverytech.delivery_api.model.ItemPedido();
                  item.setProduto(new com.deliverytech.delivery_api.model.Produto());
                  item.getProduto().setId(itemDto.getProdutoId());
                  item.setQuantidade(itemDto.getQuantidade());
                  // Use precoUnitario from DTO if available, otherwise set default
                  if (itemDto.getPrecoUnitario() != null) {
                    item.setPrecoUnitario(itemDto.getPrecoUnitario());
                  } else {
                    // Set a default precoUnitario to avoid validation error
                    // The service will update this with the actual product price
                    item.setPrecoUnitario(java.math.BigDecimal.ONE);
                  }
                  return item;
                })
            .toList());
    return pedido;
  }

  private PedidoResponse mapToResponse(Pedido pedido) {
    return new PedidoResponse(
        pedido.getId(),
        (pedido.getCliente() != null)
            ? new com.deliverytech.delivery_api.dto.response.ClienteResumoResponse(
                pedido.getCliente().getId(), pedido.getCliente().getNome())
            : null,
        pedido.getRestaurante() != null ? pedido.getRestaurante().getId() : null,
        pedido.getEnderecoEntrega(),
        pedido.getValorTotal(),
        pedido.getStatus(),
        pedido.getDataPedido(),
        pedido.getItens() != null
            ? pedido.getItens().stream()
                .map(
                    item ->
                        new ItemPedidoResponse(
                            item.getProduto() != null ? item.getProduto().getId() : null,
                            item.getProduto() != null ? item.getProduto().getNome() : null,
                            item.getQuantidade(),
                            item.getPrecoUnitario()))
                .toList()
            : List.of());
  }

  private PedidoResumoResponse mapToResumoResponse(Pedido pedido) {
    return new PedidoResumoResponse(
        pedido.getId(),
        (pedido.getCliente() != null) ? pedido.getCliente().getNome() : null,
        (pedido.getRestaurante() != null) ? pedido.getRestaurante().getNome() : null,
        pedido.getValorTotal(),
        (pedido.getStatus() != null) ? pedido.getStatus().name() : null,
        pedido.getDataPedido());
  }
}
