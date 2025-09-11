package com.deliverytech.delivery_api.health;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test-unit & !test") // Disable in test environments
public class ExternalServiceHealthIndicator implements HealthIndicator {

  private final HttpClient httpClient;

  public ExternalServiceHealthIndicator() {
    this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
  }

  @Override
  public Health health() {
    Health.Builder googleHealth = checkGoogleConnectivity();
    Health.Builder amazonHealth = checkAmazonConnectivity();

    // If both are up, return up, otherwise return down
    if (googleHealth.build().getStatus().equals(org.springframework.boot.actuate.health.Status.UP)
        && amazonHealth
            .build()
            .getStatus()
            .equals(org.springframework.boot.actuate.health.Status.UP)) {
      return Health.up()
          .withDetail("google", googleHealth.build().getDetails())
          .withDetail("amazon", amazonHealth.build().getDetails())
          .build();
    } else {
      return Health.down()
          .withDetail("google", googleHealth.build().getDetails())
          .withDetail("amazon", amazonHealth.build().getDetails())
          .build();
    }
  }

  private Health.Builder checkGoogleConnectivity() {
    try {
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(URI.create("https://www.google.com"))
              .timeout(Duration.ofSeconds(5))
              .build();

      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 200) {
        return Health.up()
            .withDetail("service", "Google")
            .withDetail("status", "Available")
            .withDetail("responseTime", "OK");
      } else {
        return Health.down()
            .withDetail("service", "Google")
            .withDetail("status", "Unavailable")
            .withDetail("statusCode", response.statusCode());
      }
    } catch (Exception e) {
      return Health.down()
          .withDetail("service", "Google")
          .withDetail("status", "Unavailable")
          .withDetail("error", e.getMessage());
    }
  }

  private Health.Builder checkAmazonConnectivity() {
    try {
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(URI.create("https://www.amazon.com"))
              .timeout(Duration.ofSeconds(5))
              .build();

      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 200) {
        return Health.up()
            .withDetail("service", "Amazon")
            .withDetail("status", "Available")
            .withDetail("responseTime", "OK");
      } else {
        return Health.down()
            .withDetail("service", "Amazon")
            .withDetail("status", "Unavailable")
            .withDetail("statusCode", response.statusCode());
      }
    } catch (Exception e) {
      return Health.down()
          .withDetail("service", "Amazon")
          .withDetail("status", "Unavailable")
          .withDetail("error", e.getMessage());
    }
  }
}
