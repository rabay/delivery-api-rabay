package com.deliverytech.delivery_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper used only for OpenAPI schema generation: represents
 * ApiResult<PagedResponse<RestauranteResponse>>
 */
@Schema(
    description =
        "ApiResult contendo uma página de restaurantes com envelope { data, message, success }",
    example =
        "{\n"
            + "  \"data\": {\n"
            + "    \"items\": [ { \"id\": 2, \"nome\": \"Restaurante do Bairro\", \"categoria\": \"Italiana\" } ],\n"
            + "    \"page\": 0,\n"
            + "    \"size\": 10,\n"
            + "    \"totalItems\": 1,\n"
            + "    \"totalPages\": 1,\n"
            + "    \"links\": { \"first\": \"/api/restaurantes?page=0\", \"last\": \"/api/restaurantes?page=0\" }\n"
            + "  },\n"
            + "  \"message\": \"Consulta realizada com sucesso\",\n"
            + "  \"success\": true\n"
            + "}")
public final class ApiResultRestaurantePaged {
  public PagedResponse<RestauranteResponse> data;
  public String message;
  public boolean success;
}
