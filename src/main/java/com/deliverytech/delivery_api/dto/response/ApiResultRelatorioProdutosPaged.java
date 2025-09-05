package com.deliverytech.delivery_api.dto.response;

import com.deliverytech.delivery_api.projection.RelatorioVendasProdutos;

public final class ApiResultRelatorioProdutosPaged {
    public PagedResponse<RelatorioVendasProdutos> data;
    public String message;
    public boolean success;
}
