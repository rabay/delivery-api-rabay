package com.deliverytech.delivery_api.config;

import com.deliverytech.delivery_api.filter.RequestResponseLoggingFilter;
import com.deliverytech.delivery_api.service.LoggingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {

  @Bean
  public RequestResponseLoggingFilter requestResponseLoggingFilter(LoggingService loggingService) {
    return new RequestResponseLoggingFilter(loggingService);
  }
}
