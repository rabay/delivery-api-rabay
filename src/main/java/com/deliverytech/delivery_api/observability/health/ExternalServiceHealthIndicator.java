package com.deliverytech.delivery_api.observability.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component("externalService")
public class ExternalServiceHealthIndicator implements HealthIndicator {

    private final Random random = new Random();

    @Override
    public Health health() {
        // Simulate a check to an external service
        if (isServiceUp()) {
            return Health.up().withDetail("message", "Serviço externo está operacional.").build();
        } else {
            return Health.down().withDetail("message", "Serviço externo está fora do ar.").build();
        }
    }

    private boolean isServiceUp() {
        // 70% chance of being up
        return random.nextInt(10) < 7;
    }
}
