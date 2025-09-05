package com.deliverytech.delivery_api.dto.response;

import com.deliverytech.delivery_api.projection.RelatorioVendasClientes;

public final class ApiResultRelatorioClientesPaged {
    public PagedResponse<RelatorioVendasClientes> data;
    public String message;
    public boolean success;
}
