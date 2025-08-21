package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.Restaurante;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RestauranteServiceTest {
    @Autowired
    private RestauranteService restauranteService;

    @Test
    void contextLoads() {
        assertThat(restauranteService).isNotNull();
    }
}
