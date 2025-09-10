package com.deliverytech.delivery_api.filter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.deliverytech.delivery_api.service.LoggingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RequestResponseLoggingFilterTest {

  @Mock private LoggingService loggingService;

  @InjectMocks private RequestResponseLoggingFilter requestResponseLoggingFilter;

  @Test
  void testFilterCreation() {
    assertNotNull(requestResponseLoggingFilter);
  }
}
