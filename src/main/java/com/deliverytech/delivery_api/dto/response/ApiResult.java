package com.deliverytech.delivery_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Wrapper genérico para respostas de sucesso e erro. Renomeado para evitar colisão com as anotações
 * OpenAPI (@ApiResponse).
 */
@Schema(
    name = "ApiResult",
    description = "Envelope padrão das respostas da API",
    example = "{\"data\":null, \"message\":\"Operação concluída com sucesso\", \"success\":true}")
public record ApiResult<T>(
    @Schema(description = "Dados retornados", nullable = true) T data,
    @Schema(description = "Mensagem legível para cliente") String message,
    @Schema(description = "Indicador de sucesso da operação") boolean success) {}
