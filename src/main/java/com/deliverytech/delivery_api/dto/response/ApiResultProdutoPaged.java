package com.deliverytech.delivery_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper used only for OpenAPI schema generation: represents ApiResult<PagedResponse<ProdutoResponse>>
 */
@Schema(description = "ApiResult contendo uma página de produtos com envelope { data, message, success }", example = "{\n  \"data\": {\n    \"items\": [ { \"id\": 1, \"nome\": \"Pizza Margherita\", \"preco\": 29.90 } ],\n    \"page\": 0,\n    \"size\": 10,\n    \"totalItems\": 1,\n    \"totalPages\": 1\n  },\n  \"message\": \"Consulta realizada com sucesso\",\n  \"success\": true\n}")
public final class ApiResultProdutoPaged {
    public PagedResponse<ProdutoResponse> data;
    public String message;
    public boolean success;
}
