package com.deliverytech.delivery_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper used only for OpenAPI schema generation: represents ApiResult<PagedResponse<PedidoResponse>>
 */
@Schema(description = "ApiResult contendo uma página de pedidos com envelope { data, message, success }", example = "{\n  \"data\": {\n    \"items\": [ { \"id\": 10, \"valorTotal\": 99.9, \"status\": \"CRIADO\" } ],\n    \"page\": 0,\n    \"size\": 10,\n    \"totalItems\": 1,\n    \"totalPages\": 1\n  },\n  \"message\": \"Consulta realizada com sucesso\",\n  \"success\": true\n}")
public final class ApiResultPedidoPaged {
    public PagedResponse<PedidoResponse> data;
    public String message;
    public boolean success;
}
