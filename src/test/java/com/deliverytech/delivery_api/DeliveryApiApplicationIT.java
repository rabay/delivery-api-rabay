package com.deliverytech.delivery_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DeliveryApiApplicationIT extends AbstractIntegrationTest {

    @Test
    void contextLoads() {
        // Simple test to ensure the application context loads
        assertThat(true).isTrue();
    }
}
