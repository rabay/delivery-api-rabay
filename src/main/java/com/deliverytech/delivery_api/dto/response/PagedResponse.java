package com.deliverytech.delivery_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;

/** Resposta para listagens paginadas. */
@Schema(
    name = "PagedResponse",
    description = "Estrutura de páginação padrão",
    example =
        "{\"items\":[{\"id\":1,\"nome\":\"Item"
            + " Exemplo\",\"valor\":19.9}],\"totalItems\":1,\"page\":0,\"size\":10,\"totalPages\":1,\"links\":{\"first\":\"...\"},\"message\":\"Página"
            + " retornada com sucesso\",\"success\":true}")
public record PagedResponse<T>(
    List<T> items,
    long totalItems,
    int page,
    int size,
    int totalPages,
    Map<String, String> links,
    String message,
    boolean success) {}
