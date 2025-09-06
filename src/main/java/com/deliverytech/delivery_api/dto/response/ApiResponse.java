package com.deliverytech.delivery_api.dto.response;

/** Wrapper genérico para respostas de sucesso. Campos simples para compatibilidade com clientes. */
public record ApiResponse<T>(T data, String message, boolean success) {}
