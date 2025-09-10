package com.deliverytech.delivery_api.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class LoggingServiceTest {

  @Test
  void testServiceCreation() {
    LoggingService loggingService = new LoggingService("target/logs_test");
    assertNotNull(loggingService);
  }
}
