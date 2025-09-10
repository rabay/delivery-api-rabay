package com.deliverytech.delivery_api.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

  @Value("${app.security.cors.allowed-origins:*}")
  private String allowedOriginsProp;

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    // allowed origins from property (comma separated) or wildcard
    List<String> allowedOrigins =
        Arrays.stream(allowedOriginsProp.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .toList();

    if (allowedOrigins.isEmpty()
        || (allowedOrigins.size() == 1 && "*".equals(allowedOrigins.get(0)))) {
      config.addAllowedOriginPattern("*");
    } else {
      allowedOrigins.forEach(config::addAllowedOrigin);
    }

    // Common defaults
    config.setAllowCredentials(true);
    config.addAllowedHeader(CorsConfiguration.ALL);
    config.addAllowedMethod(HttpMethod.GET);
    config.addAllowedMethod(HttpMethod.POST);
    config.addAllowedMethod(HttpMethod.PUT);
    config.addAllowedMethod(HttpMethod.PATCH);
    config.addAllowedMethod(HttpMethod.DELETE);
    config.addAllowedMethod(HttpMethod.OPTIONS);

    // Expose common headers (Location, Content-Type, Cache-Control if present)
    config.addExposedHeader("Location");
    config.addExposedHeader("Content-Type");
    config.addExposedHeader("Cache-Control");

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}
