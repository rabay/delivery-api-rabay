package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.request.ClienteRequest;
import com.deliverytech.delivery_api.dto.response.ClienteResponse;
import com.deliverytech.delivery_api.service.ClienteService;
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
import org.springframework.http.ResponseEntity;
import java.net.URI;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.*;

// java.util.List não é mais necessário neste arquivo

@RestController
@RequestMapping(value = "/api/clientes", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
@Tag(
    name = "Clientes",
    description = "Operações de cadastro, consulta, atualização e inativação de clientes.")
public class ClienteController {
  private final ClienteService clienteService;
  private final PedidoService pedidoService;

  public ClienteController(ClienteService clienteService, PedidoService pedidoService) {
    this.clienteService = clienteService;
    this.pedidoService = pedidoService;
  }

  @Operation(
      summary = "Cadastrar novo cliente",
      description = "Cria um novo cliente ativo no sistema.")
  @PostMapping
  public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<ClienteResponse>>
      criar(@Valid @RequestBody ClienteRequest clienteRequest) {
    ClienteResponse novo = clienteService.cadastrar(clienteRequest);
    var body =
        new com.deliverytech.delivery_api.dto.response.ApiResult<>(
            novo, "Cliente criado com sucesso", true);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(novo.getId()).toUri();
    return ResponseEntity.created(location).body(body);
  }

  @Operation(
      summary = "Listar clientes ativos",
      description = "Retorna todos os clientes ativos cadastrados.")
  @Parameters({
    @Parameter(name = "page", description = "Índice da página (0-based)", example = "0"),
    @Parameter(name = "size", description = "Tamanho da página", example = "20")
  })
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Clientes paginados (ApiResult<PagedResponse<ClienteResponse>>)",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        implementation =
                            com.deliverytech.delivery_api.dto.response.ApiResultClientePaged
                                .class))),
    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  @GetMapping
    public ResponseEntity<
                    com.deliverytech.delivery_api.dto.response.ApiResult<
                            com.deliverytech.delivery_api.dto.response.PagedResponse<
                                    com.deliverytech.delivery_api.dto.response.ClienteResponse>>>
            listar(
                    @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                    @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
        // Usar PageableUtil para padronizar comportamento de paginação
        var pageable = com.deliverytech.delivery_api.util.PageableUtil.buildPageable(page, size, (String) null, com.deliverytech.delivery_api.util.SortableProperties.CLIENTE);
        var pageResult = clienteService.listarAtivos(pageable);

        // Construir links de navegação
        var uriBuilder = org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest();
        java.util.Map<String, String> links = new java.util.HashMap<>();
        links.put("first", uriBuilder.replaceQueryParam("page", 0).build().toUriString());
        int lastPage = Math.max(0, pageResult.getTotalPages() - 1);
        links.put("last", uriBuilder.replaceQueryParam("page", lastPage).build().toUriString());
        if (pageResult.hasNext()) {
            links.put("next", uriBuilder.replaceQueryParam("page", pageResult.getNumber() + 1).build().toUriString());
        }
        if (pageResult.hasPrevious()) {
            links.put("prev", uriBuilder.replaceQueryParam("page", pageResult.getNumber() - 1).build().toUriString());
        }

        var paged =
                new com.deliverytech.delivery_api.dto.response.PagedResponse<>(
                        pageResult.getContent(),
                        pageResult.getTotalElements(),
                        pageResult.getNumber(),
                        pageResult.getSize(),
                        pageResult.getTotalPages(),
                        links,
                        "Clientes obtidos com sucesso",
                        true);
        return ResponseEntity.ok(
                new com.deliverytech.delivery_api.dto.response.ApiResult<>(paged, "Clientes obtidos com sucesso", true));
    }

  @Operation(
      summary = "Buscar cliente por e-mail",
      description = "Consulta um cliente pelo e-mail.")
  @GetMapping("/email/{email}")
  public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<ClienteResponse>>
      buscarPorEmail(@PathVariable String email) {
    var opt = clienteService.buscarPorEmail(email);
    if (opt.isPresent()) {
      var c = opt.get();
      return ResponseEntity.ok(
          new com.deliverytech.delivery_api.dto.response.ApiResult<>(
              c, "Cliente encontrado", true));
    }
    return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND)
        .body(
            new com.deliverytech.delivery_api.dto.response.ApiResult<>(
                null, "Cliente não encontrado", false));
  }

  @Operation(
      summary = "Buscar cliente por ID",
      description = "Consulta um cliente pelo seu identificador único.")
  @GetMapping("/{id:[0-9]+}")
  public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<ClienteResponse>>
      buscarPorId(@PathVariable Long id) {
    try {
      ClienteResponse cliente = clienteService.buscarPorId(id);
      return ResponseEntity.ok(
          new com.deliverytech.delivery_api.dto.response.ApiResult<>(
              cliente, "Cliente obtido com sucesso", true));
    } catch (RuntimeException e) {
      return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND)
          .body(
              new com.deliverytech.delivery_api.dto.response.ApiResult<>(
                  null, "Cliente não encontrado", false));
    }
  }

  @Operation(
      summary = "Atualizar cliente",
      description = "Atualiza os dados de um cliente existente.")
  @PutMapping("/{id:[0-9]+}")
  public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<ClienteResponse>>
      atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest clienteRequest) {
    ClienteResponse atualizado = clienteService.atualizar(id, clienteRequest);
    return ResponseEntity.ok(
        new com.deliverytech.delivery_api.dto.response.ApiResult<>(
            atualizado, "Cliente atualizado com sucesso", true));
  }

  @Operation(
      summary = "Inativar cliente",
      description = "Inativa um cliente pelo seu ID, tornando-o indisponível para operações.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Cliente inativado com sucesso",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        value =
                            "{\"data\":null, \"message\":\"Cliente inativado com sucesso\","
                                + " \"success\":true}"))),
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  @DeleteMapping("/{id:[0-9]+}")
  public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<Void>> inativar(
      @PathVariable Long id) {
    clienteService.inativar(id);
    return ResponseEntity.ok(
        new com.deliverytech.delivery_api.dto.response.ApiResult<>(
            null, "Cliente inativado com sucesso", true));
  }

  @Operation(
      summary = "Ativar/Desativar cliente",
      description = "Alterna o status ativo/inativo de um cliente.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Status do cliente alterado com sucesso",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        value =
                            "{\"data\": {\"id\": 1, \"nome\": \"João\"}, \"message\": \"Status do"
                                + " cliente alterado\", \"success\": true}"))),
    @ApiResponse(responseCode = "400", description = "Requisição inválida"),
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  @PatchMapping("/{id:[0-9]+}/status")
  public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<ClienteResponse>>
      alterarStatus(@PathVariable Long id) {
    ClienteResponse atualizado = clienteService.ativarDesativarCliente(id);
    return ResponseEntity.ok(
        new com.deliverytech.delivery_api.dto.response.ApiResult<>(
            atualizado, "Status do cliente alterado", true));
  }

  @Operation(
      summary = "Listar pedidos do cliente",
      description = "Retorna todos os pedidos realizados por um cliente específico.")
  @Parameters({
    @Parameter(name = "page", description = "Índice da página (0-based)", example = "0"),
    @Parameter(name = "size", description = "Tamanho da página", example = "20")
  })
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Pedidos do cliente paginados (ApiResult<PagedResponse<PedidoResponse>>)",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        implementation =
                            com.deliverytech.delivery_api.dto.response.ApiResultPedidoPaged
                                .class))),
    @ApiResponse(responseCode = "400", description = "clienteId inválido"),
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  @GetMapping("/{clienteId:[0-9]+}/pedidos")
  public ResponseEntity<
          com.deliverytech.delivery_api.dto.response.ApiResult<
              com.deliverytech.delivery_api.dto.response.PagedResponse<
                  com.deliverytech.delivery_api.dto.response.PedidoResponse>>>
      buscarPedidosDoCliente(
          @PathVariable Long clienteId,
          @RequestParam(name = "page", required = false, defaultValue = "0") int page,
          @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
    try {
      if (clienteId == null || clienteId <= 0) {
        return ResponseEntity.badRequest()
            .body(
                new com.deliverytech.delivery_api.dto.response.ApiResult<>(
                    null, "clienteId inválido", false));
      }

                var pageable = com.deliverytech.delivery_api.util.PageableUtil.buildPageable(page, size, (String) null, com.deliverytech.delivery_api.util.SortableProperties.PEDIDO);
                var pageResult = pedidoService.buscarPedidosPorCliente(clienteId, pageable);

                var uriBuilder = ServletUriComponentsBuilder.fromCurrentRequest();
                java.util.Map<String, String> links = new java.util.HashMap<>();
                links.put("first", uriBuilder.replaceQueryParam("page", 0).build().toUriString());
                int lastPage = Math.max(0, pageResult.getTotalPages() - 1);
                links.put("last", uriBuilder.replaceQueryParam("page", lastPage).build().toUriString());
                if (pageResult.hasNext()) {
                    links.put("next", uriBuilder.replaceQueryParam("page", pageResult.getNumber() + 1).build().toUriString());
                }
                if (pageResult.hasPrevious()) {
                    links.put("prev", uriBuilder.replaceQueryParam("page", pageResult.getNumber() - 1).build().toUriString());
                }

                var paged =
                        new com.deliverytech.delivery_api.dto.response.PagedResponse<>(
                                pageResult.getContent(),
                                pageResult.getTotalElements(),
                                pageResult.getNumber(),
                                pageResult.getSize(),
                                pageResult.getTotalPages(),
                                links,
                                "Pedidos do cliente obtidos",
                                true);
                return ResponseEntity.ok(
                        new com.deliverytech.delivery_api.dto.response.ApiResult<>(
                                paged, "Pedidos do cliente obtidos", true));
    } catch (RuntimeException ex) {
      // Em caso de erro (ex.: cliente não existe)
      return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND)
          .body(
              new com.deliverytech.delivery_api.dto.response.ApiResult<>(
                  null, "Cliente não encontrado", false));
    } catch (Exception ex) {
      return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new com.deliverytech.delivery_api.dto.response.ApiResult<>(
                  null, "Erro interno: " + ex.getMessage(), false));
    }
  }

  // Removido método mapToResponse pois não é utilizado neste controller.
}
