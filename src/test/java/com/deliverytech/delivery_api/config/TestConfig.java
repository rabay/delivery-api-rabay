package com.deliverytech.delivery_api.config;

import org.springframework.boot.test.context.TestConfiguration;
// imports trimmed: no @Bean/@Primary required for this test configuration
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@SuppressWarnings("resource")
@TestConfiguration
public class TestConfig {

  private static MySQLContainer<?> mysqlContainer;

  static {
    mysqlContainer =
        new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
            .withDatabaseName("deliverydb")
            .withUsername("deliveryuser")
            .withPassword("deliverypass")
            .withCommand(
                "--character-set-server=utf8mb4",
                "--collation-server=utf8mb4_unicode_ci",
                "--innodb-buffer-pool-size=128M",
                "--max-connections=100");

    mysqlContainer.start();
  }

  @DynamicPropertySource
  public static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
    registry.add("spring.datasource.username", mysqlContainer::getUsername);
    registry.add("spring.datasource.password", mysqlContainer::getPassword);
    registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
    // Set sql init mode to always to execute data-test.sql
    registry.add("spring.sql.init.mode", () -> "always");
    // Add other properties to override application.yml
    registry.add("spring.jpa.show-sql", () -> "false");
    registry.add("spring.jpa.properties.hibernate.format_sql", () -> "true");
    // Defer data source initialization to allow proper ordering
    registry.add("spring.jpa.defer-datasource-initialization", () -> "true");
    // Disable open-in-view to prevent warnings
    registry.add("spring.jpa.open-in-view", () -> "false");
    // Override the default datasource configuration
    registry.add("spring.config.location", () -> "");
    registry.add("spring.profiles.active", () -> "");
  }

  public static MySQLContainer<?> getMysqlContainer() {
    return mysqlContainer;
  }
}
