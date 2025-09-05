package com.deliverytech.delivery_api.dto.response;

import java.util.Map;

/**
 * Wrapper used only for OpenAPI schema generation: represents ApiResult<PagedResponse<Map<String,Object>>>
 */
public final class ApiResultMapPaged {
    public PagedResponse<Map<String,Object>> data;
    public String message;
    public boolean success;
}
