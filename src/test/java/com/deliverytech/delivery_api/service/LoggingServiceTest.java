package com.deliverytech.delivery_api.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LoggingServiceTest {

    @Test
    void testServiceCreation() {
        LoggingService loggingService = new LoggingService("target/logs_test");
        assertNotNull(loggingService);
    }
}