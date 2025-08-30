package com.deliverytech.delivery_api.projection;

import java.math.BigDecimal;

public interface FaturamentoPorCategoriaProjection {
    String getCategoria();

    BigDecimal getTotalFaturado();
}
