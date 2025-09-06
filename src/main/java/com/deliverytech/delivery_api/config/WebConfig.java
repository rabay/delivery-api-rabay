package com.deliverytech.delivery_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    // Use default path matching strategy
    // Commented out the custom configuration that might be causing issues
    // configurer.setUseTrailingSlashMatch(true);
    // configurer.setPatternParser(new PathPatternParser());
  }
}
