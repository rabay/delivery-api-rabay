package com.deliverytech.delivery_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/** Wrapper genérico para respostas de sucesso. Campos simples para compatibilidade com clientes. */
@Schema(
    name = "ApiResponse",
    description = "Envelope simples para respostas da API",
    example = "{\"data\":null, \"message\":\"Operação concluída com sucesso\", \"success\":true}")
public record ApiResponse<T>(T data, String message, boolean success) {}
