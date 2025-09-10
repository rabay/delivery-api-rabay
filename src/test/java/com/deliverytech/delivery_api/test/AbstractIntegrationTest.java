package com.deliverytech.delivery_api.test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.StreamUtils;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class AbstractIntegrationTest {

  private static final MySQLContainer<?> mysql =
      new MySQLContainer<>("mysql:8.0")
          .withDatabaseName("deliverydb")
          .withUsername("deliveryuser")
          .withPassword("deliverypass")
          .withUrlParam("serverTimezone", "UTC")
          .withUrlParam("allowPublicKeyRetrieval", "true");

  private static boolean dataInitialized = false;

  @BeforeAll
  static void setUp() {
    if (!mysql.isRunning()) {
      mysql.start();
      initializeData();
    }
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mysql::getJdbcUrl);
    registry.add("spring.datasource.username", mysql::getUsername);
    registry.add("spring.datasource.password", mysql::getPassword);
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    registry.add("spring.sql.init.mode", () -> "never");
  }

  private static synchronized void initializeData() {
    if (!dataInitialized) {
      try {
        String dataSql =
            StreamUtils.copyToString(
                new ClassPathResource("/data.sql").getInputStream(), StandardCharsets.UTF_8);

        try (Connection connection =
                DriverManager.getConnection(
                    mysql.getJdbcUrl(), mysql.getUsername(), mysql.getPassword());
            Statement statement = connection.createStatement()) {

          // Split the SQL script into individual statements
          String[] statements = dataSql.split(";");
          for (String sql : statements) {
            if (!sql.trim().isEmpty()) {
              statement.execute(sql);
            }
          }
        }

        dataInitialized = true;
      } catch (SQLException | IOException e) {
        throw new RuntimeException("Failed to initialize test data", e);
      }
    }
  }

  protected static MySQLContainer<?> getMysqlContainer() {
    return mysql;
  }
}
