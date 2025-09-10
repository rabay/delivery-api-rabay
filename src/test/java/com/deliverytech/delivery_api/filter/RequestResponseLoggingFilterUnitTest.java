package com.deliverytech.delivery_api.filter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import com.deliverytech.delivery_api.service.LoggingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RequestResponseLoggingFilterUnitTest {

  @Mock private LoggingService loggingService;

  @Test
  void testFilterCreation() {
    RequestResponseLoggingFilter filter = new RequestResponseLoggingFilter(loggingService);
    assertNotNull(filter);
  }

  @Test
  void testFilterMethods() {
    RequestResponseLoggingFilter filter = new RequestResponseLoggingFilter(loggingService);
    assertNotNull(filter);

    // Verify that the loggingService mock was injected correctly
    verifyNoInteractions(loggingService);
  }
}
