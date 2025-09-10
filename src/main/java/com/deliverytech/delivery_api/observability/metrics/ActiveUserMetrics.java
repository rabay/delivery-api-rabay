package com.deliverytech.delivery_api.observability.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ActiveUserMetrics {

    private final AtomicInteger activeUsers = new AtomicInteger(0);
    private final Random random = new Random();

    public ActiveUserMetrics(MeterRegistry meterRegistry) {
        Gauge.builder("usuarios.ativos", activeUsers, AtomicInteger::get)
                .description("Número estimado de usuários ativos")
                .register(meterRegistry);

        // Simulate active user changes
        new Thread(() -> {
            while (true) {
                activeUsers.set(random.nextInt(1000));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // Restore interruption status
                    Thread.currentThread().interrupt();
                }
            }
        }, "active-user-simulator").start();
    }
}
