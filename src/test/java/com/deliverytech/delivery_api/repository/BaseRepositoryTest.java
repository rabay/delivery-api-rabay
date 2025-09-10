package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.config.TestConfig;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Import(TestConfig.class)
public abstract class BaseRepositoryTest {

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    TestConfig.configureProperties(registry);
  }
}
