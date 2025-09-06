package com.deliverytech.delivery_api.dto.response;

import com.deliverytech.delivery_api.projection.RelatorioVendas;

/**
 * Wrapper used only for OpenAPI schema generation: represents
 * ApiResult<PagedResponse<RelatorioVendas>>
 */
public final class ApiResultRelatorioVendasPaged {
  public PagedResponse<RelatorioVendas> data;
  public String message;
  public boolean success;
}
