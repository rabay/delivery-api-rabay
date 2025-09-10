package com.deliverytech.delivery_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper used only for OpenAPI schema generation: represents
 * ApiResult<PagedResponse<ClienteResponse>>
 */
@Schema(
    description = "ApiResult contendo uma página de clientes",
    example =
        "{\n"
            + "  \"data\": {\n"
            + "    \"items\": [ { \"id\": 3, \"nome\": \"Maria\" } ],\n"
            + "    \"page\": 0,\n"
            + "    \"size\": 10,\n"
            + "    \"totalItems\": 1,\n"
            + "    \"totalPages\": 1,\n"
            + "    \"links\": { \"first\": \"/api/clientes?page=0\", \"last\":"
            + " \"/api/clientes?page=0\" }\n"
            + "  },\n"
            + "  \"message\": \"Consulta realizada com sucesso\",\n"
            + "  \"success\": true\n"
            + "}")
public final class ApiResultClientePaged {
  public PagedResponse<ClienteResponse> data;
  public String message;
  public boolean success;
}
