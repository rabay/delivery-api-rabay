package com.deliverytech.delivery_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import java.util.Map;

/** Resposta para erros de validação com campos. */
@Schema(
    name = "ValidationErrorResponse",
    description = "Erros de validação por campo",
    example =
        "{\"error\":\"ValidationFailed\",\"fieldErrors\":{\"email\":\"Formato"
            + " inválido\",\"senha\":\"Tamanho mínimo 8"
            + " caracteres\"},\"status\":422,\"timestamp\":\"2025-09-05T12:34:56Z\"}")
public record ValidationErrorResponse(
    String error, Map<String, String> fieldErrors, int status, OffsetDateTime timestamp) {}
