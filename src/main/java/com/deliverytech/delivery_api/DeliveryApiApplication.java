package com.deliverytech.delivery_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.validation.annotation.Validated;

@SpringBootApplication
@EntityScan(basePackages = "com.deliverytech.delivery_api.model")
@EnableJpaRepositories(basePackages = "com.deliverytech.delivery_api.repository")
@Validated
public class DeliveryApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(DeliveryApiApplication.class, args);
  }
}
