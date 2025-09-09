package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.request.ProdutoRequest;
import com.deliverytech.delivery_api.dto.response.ProdutoResponse;
import com.deliverytech.delivery_api.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/produtos", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
@Tag(
    name = "Produtos",
    description = "Cadastro, consulta e gerenciamento de produtos disponíveis nos restaurantes.")
public class ProdutoController {
  private final ProdutoService produtoService;

  public ProdutoController(ProdutoService produtoService) {
    this.produtoService = produtoService;
  }

  @Operation(
      summary = "Cadastrar novo produto",
      description = "Cria um novo produto disponível em um restaurante.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "201",
        description = "Produto criado com sucesso",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.deliverytech.delivery_api.dto.response.ApiResult.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Request inválido",
        content = @Content(schema = @Schema(implementation = Void.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Erro interno do servidor",
        content = @Content(schema = @Schema(implementation = Void.class)))
  })
  @PostMapping
  public ResponseEntity<
          com.deliverytech.delivery_api.dto.response.ApiResult<
              com.deliverytech.delivery_api.dto.response.ProdutoResponse>>
      criar(@Valid @RequestBody ProdutoRequest produto) {
    ProdutoResponse novo = produtoService.cadastrar(produto);
    var body =
        new com.deliverytech.delivery_api.dto.response.ApiResult<>(
            novo, "Produto criado com sucesso", true);
    java.net.URI location =
        ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(novo.getId()).toUri();
    return ResponseEntity.created(location).body(body);
  }

  @Operation(
      summary = "Listar produtos disponíveis",
      description = "Retorna todos os produtos disponíveis cadastrados.")
  @Parameters({
    @Parameter(name = "page", description = "Índice da página (0-based)", example = "0"),
    @Parameter(name = "size", description = "Tamanho da página", example = "20")
  })
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Produtos paginados",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        implementation =
                            com.deliverytech.delivery_api.dto.response.ApiResultProdutoPaged
                                .class))),
    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  @GetMapping
    public ResponseEntity<
                    com.deliverytech.delivery_api.dto.response.ApiResult<
                            com.deliverytech.delivery_api.dto.response.PagedResponse<
                                    com.deliverytech.delivery_api.dto.response.ProdutoResponse>>>
            listar(
                    @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                    @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
        // Allow-list de propriedades permitidas para sort em Produto.
        // TODO: extrair para constante central ou configuração por entidade.
    var pageable = com.deliverytech.delivery_api.util.PageableUtil.buildPageable(page, size, (String) null, com.deliverytech.delivery_api.util.SortableProperties.PRODUTO);
        var pageResult = produtoService.buscarDisponiveis(pageable);

        // Construir links de navegação
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
                        "Produtos obtidos com sucesso",
                        true);
        return ResponseEntity.ok(
                new com.deliverytech.delivery_api.dto.response.ApiResult<>(paged, "Produtos obtidos com sucesso", true));
    }

  @Operation(
      summary = "Buscar produto por ID",
      description = "Consulta um produto pelo seu identificador único.")
  @GetMapping("/{id}")
  public ResponseEntity<
          com.deliverytech.delivery_api.dto.response.ApiResult<
              com.deliverytech.delivery_api.dto.response.ProdutoResponse>>
      buscarPorId(@PathVariable Long id) {
    try {
      ProdutoResponse produto = produtoService.buscarProdutoPorId(id);
      return ResponseEntity.ok(
          new com.deliverytech.delivery_api.dto.response.ApiResult<>(
              produto, "Produto obtido com sucesso", true));
    } catch (RuntimeException e) {
      return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND)
          .body(
              new com.deliverytech.delivery_api.dto.response.ApiResult<>(
                  null, "Produto não encontrado", false));
    }
  }

  @Operation(
      summary = "Atualizar produto",
      description = "Atualiza os dados de um produto existente.")
  @PutMapping("/{id}")
  public ResponseEntity<
          com.deliverytech.delivery_api.dto.response.ApiResult<
              com.deliverytech.delivery_api.dto.response.ProdutoResponse>>
      atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequest produto) {
    try {
      ProdutoResponse atualizado = produtoService.atualizar(id, produto);
      return ResponseEntity.ok(
          new com.deliverytech.delivery_api.dto.response.ApiResult<>(
              atualizado, "Produto atualizado com sucesso", true));
    } catch (RuntimeException e) {
      return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND)
          .body(
              new com.deliverytech.delivery_api.dto.response.ApiResult<>(
                  null, "Produto não encontrado", false));
    }
  }

  @Operation(
      summary = "Deletar produto",
      description = "Remove (soft delete) um produto pelo seu ID.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Produto removido com sucesso",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        value =
                            "{\"data\":null, \"message\":\"Produto removido com sucesso\","
                                + " \"success\":true}"))),
    @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<com.deliverytech.delivery_api.dto.response.ApiResult<Void>> deletar(
      @PathVariable Long id) {
    try {
      produtoService.deletar(id);
      return ResponseEntity.ok(
          new com.deliverytech.delivery_api.dto.response.ApiResult<>(
              null, "Produto removido com sucesso", true));
    } catch (RuntimeException e) {
      return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND)
          .body(
              new com.deliverytech.delivery_api.dto.response.ApiResult<>(
                  null, "Produto não encontrado", false));
    }
  }

  @Operation(
      summary = "Alterar disponibilidade do produto",
      description = "Alterna a disponibilidade de um produto (ativar/desativar).")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Disponibilidade atualizada com sucesso",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                        value =
                            "{\"data\": {\"produtoId\": 1, \"disponivel\": true}, \"message\":"
                                + " \"Disponibilidade atualizada\", \"success\": true}"))),
    @ApiResponse(responseCode = "400", description = "Campo 'disponivel' ausente ou inválido"),
    @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  @PatchMapping("/{id}/disponibilidade")
  public ResponseEntity<
          com.deliverytech.delivery_api.dto.response.ApiResult<java.util.Map<String, Object>>>
      alterarDisponibilidade(@PathVariable Long id, @RequestBody Map<String, Boolean> request) {
    try {
      Boolean disponivel = request.get("disponivel");
      if (disponivel == null) {
        return ResponseEntity.badRequest()
            .body(
                new com.deliverytech.delivery_api.dto.response.ApiResult<>(
                    null, "Campo 'disponivel' ausente", false));
      }
      produtoService.alterarDisponibilidade(id, disponivel);
      Map<String, Object> response = new java.util.HashMap<>();
      response.put("produtoId", id);
      response.put("disponivel", disponivel);
      response.put("message", "Disponibilidade atualizada com sucesso");
      return ResponseEntity.ok(
          new com.deliverytech.delivery_api.dto.response.ApiResult<>(
              response, "Disponibilidade atualizada", true));
    } catch (RuntimeException e) {
      return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND)
          .body(
              new com.deliverytech.delivery_api.dto.response.ApiResult<>(
                  null, "Produto não encontrado", false));
    }
  }

  @Operation(
      summary = "Buscar produtos por nome",
      description = "Busca produtos cujo nome contenha o termo informado.")
  @Parameters({
    @Parameter(name = "page", description = "Índice da página (0-based)", example = "0"),
    @Parameter(name = "size", description = "Tamanho da página", example = "20")
  })
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Produtos paginados (ApiResult<PagedResponse<ProdutoResponse>>)",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        implementation =
                            com.deliverytech.delivery_api.dto.response.ApiResultProdutoPaged
                                .class))),
    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  @GetMapping("/buscar")
    public ResponseEntity<
                    com.deliverytech.delivery_api.dto.response.ApiResult<
                            com.deliverytech.delivery_api.dto.response.PagedResponse<
                                    com.deliverytech.delivery_api.dto.response.ProdutoResponse>>>
            buscarPorNome(
                    @RequestParam String nome,
                    @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                    @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
    var pageable = com.deliverytech.delivery_api.util.PageableUtil.buildPageable(page, size, (String) null, com.deliverytech.delivery_api.util.SortableProperties.PRODUTO);
        var pageResult = produtoService.buscarProdutosPorNome(nome, pageable);

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
                        "Produtos encontrados",
                        true);
        return ResponseEntity.ok(new com.deliverytech.delivery_api.dto.response.ApiResult<>(paged, "Produtos encontrados", true));
    }

  @Operation(
      summary = "Buscar produtos por categoria",
      description = "Retorna todos os produtos disponíveis de uma categoria específica.")
  @Parameters({
    @Parameter(name = "page", description = "Índice da página (0-based)", example = "0"),
    @Parameter(name = "size", description = "Tamanho da página", example = "20")
  })
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description =
            "Produtos paginados por categoria (ApiResult<PagedResponse<ProdutoResponse>>)",
        content =
            @Content(
                mediaType = "application/json",
                schema =
                    @Schema(
                        implementation =
                            com.deliverytech.delivery_api.dto.response.ApiResultProdutoPaged
                                .class))),
    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
  })
  @GetMapping("/categoria/{categoria}")
    public ResponseEntity<
                    com.deliverytech.delivery_api.dto.response.ApiResult<
                            com.deliverytech.delivery_api.dto.response.PagedResponse<
                                    com.deliverytech.delivery_api.dto.response.ProdutoResponse>>>
            buscarPorCategoria(
                    @PathVariable String categoria,
                    @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                    @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
    var pageable = com.deliverytech.delivery_api.util.PageableUtil.buildPageable(page, size, (String) null, com.deliverytech.delivery_api.util.SortableProperties.PRODUTO);
        var pageResult = produtoService.buscarProdutosPorCategoria(categoria, pageable);

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
                        "Produtos por categoria",
                        true);
        return ResponseEntity.ok(new com.deliverytech.delivery_api.dto.response.ApiResult<>(paged, "Produtos por categoria", true));
    }
}
