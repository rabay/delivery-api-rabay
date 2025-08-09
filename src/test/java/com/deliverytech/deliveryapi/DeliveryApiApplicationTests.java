package com.deliverytech.deliveryapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.sql.init.mode=never"
})
class DeliveryApiApplicationTests {

	@Test
	void contextLoads() {
		// Teste básico de contexto com JPA configurado para criar/deletar schema
	}

}
