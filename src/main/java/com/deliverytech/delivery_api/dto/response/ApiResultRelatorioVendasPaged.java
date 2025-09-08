package com.deliverytech.delivery_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.deliverytech.delivery_api.projection.RelatorioVendas;

@Schema(
    description = "ApiResult contendo uma página do relatório de vendas",
    example =
        "{\n"
            + "  \"data\": {\n"
            + "    \"items\": [ { \"periodo\": \"2025-08\", \"total\": 1234.5 } ],\n"
            + "    \"page\": 0,\n"
            + "    \"size\": 10,\n"
            + "    \"totalItems\": 1,\n"
            + "    \"totalPages\": 1,\n"
            + "    \"links\": { \"first\": \"/api/relatorios/vendas?page=0\", \"last\": \"/api/relatorios/vendas?page=0\" }\n"
            + "  },\n"
            + "  \"message\": \"Consulta realizada com sucesso\",\n"
            + "  \"success\": true\n"
            + "}")
public final class ApiResultRelatorioVendasPaged {
  public PagedResponse<RelatorioVendas> data;
  public String message;
  public boolean success;
}
