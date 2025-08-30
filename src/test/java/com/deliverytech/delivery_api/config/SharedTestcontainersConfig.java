package com.deliverytech.delivery_api.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class SharedTestcontainersConfig {
    private static final String MYSQL_IMAGE = "mysql:8.0";
    private static final String DATABASE_NAME = "deliverydb";
    private static final String USERNAME = "deliveryuser";
    private static final String PASSWORD = "deliverypass";

    public static MySQLContainer<?> mysqlContainer;

    static {
        mysqlContainer = new MySQLContainer<>(DockerImageName.parse(MYSQL_IMAGE))
                .withDatabaseName(DATABASE_NAME)
                .withUsername(USERNAME)
                .withPassword(PASSWORD)
                .withCommand(
                        "--character-set-server=utf8mb4",
                        "--collation-server=utf8mb4_unicode_ci",
                        "--innodb-buffer-pool-size=128M",
                        "--max-connections=100"
                );

        // Start the container
        mysqlContainer.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.sql.init.mode", () -> "always");
    }

    public static MySQLContainer<?> getMysqlContainer() {
        return mysqlContainer;
    }
}