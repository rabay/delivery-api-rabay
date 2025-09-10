package com.deliverytech.delivery_api;

import com.deliverytech.delivery_api.config.TestConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@TestPropertySource(
    properties = {"spring.jpa.hibernate.ddl-auto=none", "spring.sql.init.mode=always"})
@Import(TestConfig.class)
public abstract class BaseIntegrationTest {

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    TestConfig.configureProperties(registry);
  }
}
