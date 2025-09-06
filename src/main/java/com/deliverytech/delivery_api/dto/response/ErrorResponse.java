package com.deliverytech.delivery_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;

/** Resposta de erro padronizada. */
@Schema(
    name = "ErrorResponse",
    description = "Formato padrão de erro",
    example =
        "{\"error\":\"BadRequest\",\"message\":\"Requisição"
            + " inválida\",\"status\":400,\"timestamp\":\"2025-09-05T12:34:56Z\"}")
public record ErrorResponse(String error, String message, int status, OffsetDateTime timestamp) {}
