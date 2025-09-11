package com.deliverytech.delivery_api.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

  private final JdbcTemplate jdbcTemplate;

  public DatabaseHealthIndicator(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public Health health() {
    try {
      // Execute a simple query to check database connectivity
      jdbcTemplate.queryForObject("SELECT 1", Integer.class);
      return Health.up().withDetail("database", "MySQL").withDetail("status", "Available").build();
    } catch (Exception e) {
      return Health.down()
          .withDetail("database", "MySQL")
          .withDetail("status", "Unavailable")
          .withDetail("error", e.getMessage())
          .build();
    }
  }
}
