package com.deliverytech.delivery_api.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

@Component
public class OrderMetrics {

  private final Counter orderCreatedCounter;
  private final Counter orderConfirmedCounter;
  private final Counter orderDeliveredCounter;
  private final Counter orderCancelledCounter;
  private final Timer orderProcessingTimer;

  public OrderMetrics(MeterRegistry meterRegistry) {
    // Order counters by status
    orderCreatedCounter =
        Counter.builder("orders.created")
            .description("Number of orders created")
            .register(meterRegistry);

    orderConfirmedCounter =
        Counter.builder("orders.confirmed")
            .description("Number of orders confirmed")
            .register(meterRegistry);

    orderDeliveredCounter =
        Counter.builder("orders.delivered")
            .description("Number of orders delivered")
            .register(meterRegistry);

    orderCancelledCounter =
        Counter.builder("orders.cancelled")
            .description("Number of orders cancelled")
            .register(meterRegistry);

    // Order processing timer
    orderProcessingTimer =
        Timer.builder("order.processing.time")
            .description("Time taken to process orders")
            .register(meterRegistry);
  }

  public void incrementOrderCreated() {
    orderCreatedCounter.increment();
  }

  public void incrementOrderConfirmed() {
    orderConfirmedCounter.increment();
  }

  public void incrementOrderDelivered() {
    orderDeliveredCounter.increment();
  }

  public void incrementOrderCancelled() {
    orderCancelledCounter.increment();
  }

  public Timer.Sample startOrderProcessingTimer() {
    return Timer.start();
  }

  public void recordOrderProcessingTime(Timer.Sample sample) {
    sample.stop(orderProcessingTimer);
  }
}
