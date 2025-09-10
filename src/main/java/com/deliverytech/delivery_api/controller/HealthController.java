package com.deliverytech.delivery_api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health", description = "Verificações de saúde e informações do sistema")
public class HealthController {

  // Os endpoints /health e /info foram removidos
  // e substituidos pelo Spring Boot Actuator.

}
