package com.deliverytech.delivery_api.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class ProductMetrics {

  private final Counter productCreatedCounter;
  private final Counter productUpdatedCounter;
  private final Counter productDeletedCounter;

  public ProductMetrics(MeterRegistry meterRegistry) {
    // Product counters
    productCreatedCounter =
        Counter.builder("products.created")
            .description("Number of products created")
            .register(meterRegistry);

    productUpdatedCounter =
        Counter.builder("products.updated")
            .description("Number of products updated")
            .register(meterRegistry);

    productDeletedCounter =
        Counter.builder("products.deleted")
            .description("Number of products deleted")
            .register(meterRegistry);
  }

  public void incrementProductCreated() {
    productCreatedCounter.increment();
  }

  public void incrementProductUpdated() {
    productUpdatedCounter.increment();
  }

  public void incrementProductDeleted() {
    productDeletedCounter.increment();
  }
}
