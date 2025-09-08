package com.deliverytech.delivery_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

/**
 * Wrapper used only for OpenAPI schema generation: represents
 * ApiResult<PagedResponse<Map<String,Object>>>
 */
@Schema(
    description = "ApiResult contendo uma página genérica de mapas",
    example =
        "{\n"
            + "  \"data\": {\n"
            + "    \"items\": [ { \"key\": \"value\" } ],\n"
            + "    \"page\": 0,\n"
            + "    \"size\": 10,\n"
            + "    \"totalItems\": 1,\n"
            + "    \"totalPages\": 1,\n"
            + "    \"links\": { \"first\": \"/api/resource?page=0\", \"last\": \"/api/resource?page=0\" }\n"
            + "  },\n"
            + "  \"message\": \"Consulta realizada com sucesso\",\n"
            + "  \"success\": true\n"
            + "}")
public final class ApiResultMapPaged {
  public PagedResponse<Map<String, Object>> data;
  public String message;
  public boolean success;
}
