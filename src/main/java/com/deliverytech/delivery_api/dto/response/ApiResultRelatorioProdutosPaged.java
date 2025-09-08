package com.deliverytech.delivery_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.deliverytech.delivery_api.projection.RelatorioVendasProdutos;

@Schema(
    description = "ApiResult contendo uma página do relatório de vendas por produto",
    example =
        "{\n"
            + "  \"data\": {\n"
            + "    \"items\": [ { \"produto\": \"Pizza\", \"quantidade\": 10, \"total\": 299.0 } ],\n"
            + "    \"page\": 0,\n"
            + "    \"size\": 10,\n"
            + "    \"totalItems\": 1,\n"
            + "    \"totalPages\": 1,\n"
            + "    \"links\": { \"first\": \"/api/relatorios/produtos?page=0\", \"last\": \"/api/relatorios/produtos?page=0\" }\n"
            + "  },\n"
            + "  \"message\": \"Consulta realizada com sucesso\",\n"
            + "  \"success\": true\n"
            + "}")
public final class ApiResultRelatorioProdutosPaged {
  public PagedResponse<RelatorioVendasProdutos> data;
  public String message;
  public boolean success;
}
