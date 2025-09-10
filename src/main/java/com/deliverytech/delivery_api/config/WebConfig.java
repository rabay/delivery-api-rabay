package com.deliverytech.delivery_api.config;

import com.deliverytech.delivery_api.observability.log.RequestLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RequestLoggingInterceptor requestLoggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLoggingInterceptor);
    }

  @Override
  public void configurePathMatch(@NonNull PathMatchConfigurer configurer) {
    // Use default path matching strategy
    // Commented out the custom configuration that might be causing issues
    // configurer.setUseTrailingSlashMatch(true);
    // configurer.setPatternParser(new PathPatternParser());
  }
}
