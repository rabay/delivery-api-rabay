package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.Produto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProdutoServiceTest {
    @Autowired
    private ProdutoService produtoService;

    @Test
    void contextLoads() {
        assertThat(produtoService).isNotNull();
    }
}
