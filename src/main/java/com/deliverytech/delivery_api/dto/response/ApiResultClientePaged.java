package com.deliverytech.delivery_api.dto.response;

/**
 * Wrapper used only for OpenAPI schema generation: represents ApiResult<PagedResponse<ClienteResponse>>
 */
public final class ApiResultClientePaged {
    public PagedResponse<ClienteResponse> data;
    public String message;
    public boolean success;
}
