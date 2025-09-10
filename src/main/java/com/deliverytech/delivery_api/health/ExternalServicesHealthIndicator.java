package com.deliverytech.delivery_api.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class ExternalServicesHealthIndicator implements HealthIndicator {

    private static final String GOOGLE_URL = "https://www.google.com";
    private static final String AMAZON_URL = "https://www.amazon.com";

    @Override
    public Health health() {
        boolean googleUp = checkUrl(GOOGLE_URL);
        boolean amazonUp = checkUrl(AMAZON_URL);

        if (googleUp && amazonUp) {
            return Health.up()
                    .withDetail("google", "UP")
                    .withDetail("amazon", "UP")
                    .build();
        } else {
            return Health.down()
                    .withDetail("google", googleUp ? "UP" : "DOWN")
                    .withDetail("amazon", amazonUp ? "UP" : "DOWN")
                    .build();
        }
    }

    private boolean checkUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int responseCode = connection.getResponseCode();
            return (200 <= responseCode && responseCode <= 299);
        } catch (IOException e) {
            return false;
        }
    }
}
