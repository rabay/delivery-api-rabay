package com.deliverytech.delivery_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=none",
    "spring.sql.init.mode=always"
})
class DeliveryApiApplicationTests extends BaseIntegrationTest {

	@Test
	void contextLoads() {
	}

}