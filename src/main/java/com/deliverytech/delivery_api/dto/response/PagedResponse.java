package com.deliverytech.delivery_api.dto.response;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resposta para listagens paginadas.
 */
@Schema(name = "PagedResponse", description = "Estrutura de páginação padrão", example = "{\"items\":[{\"id\":1,\"nome\":\"Item Exemplo\",\"valor\":19.9}],\"totalItems\":1,\"page\":0,\"size\":10,\"message\":\"Página retornada com sucesso\",\"success\":true}")
public record PagedResponse<T>(List<T> items, long totalItems, int page, int size, String message, boolean success) {}
