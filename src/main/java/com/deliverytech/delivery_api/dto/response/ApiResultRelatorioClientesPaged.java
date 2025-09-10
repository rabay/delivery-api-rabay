package com.deliverytech.delivery_api.dto.response;

import com.deliverytech.delivery_api.projection.RelatorioVendasClientes;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    description = "ApiResult contendo uma página do relatório de clientes",
    example =
        "{\n"
            + "  \"data\": {\n"
            + "    \"items\": [ { \"cliente\": \"Maria\", \"totalPedidos\": 5, \"totalGasto\":"
            + " 450.0 } ],\n"
            + "    \"page\": 0,\n"
            + "    \"size\": 10,\n"
            + "    \"totalItems\": 1,\n"
            + "    \"totalPages\": 1,\n"
            + "    \"links\": { \"first\": \"/api/relatorios/clientes?page=0\", \"last\":"
            + " \"/api/relatorios/clientes?page=0\" }\n"
            + "  },\n"
            + "  \"message\": \"Consulta realizada com sucesso\",\n"
            + "  \"success\": true\n"
            + "}")
public final class ApiResultRelatorioClientesPaged {
  public PagedResponse<RelatorioVendasClientes> data;
  public String message;
  public boolean success;
}
