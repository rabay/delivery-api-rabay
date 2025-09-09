package com.deliverytech.delivery_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    SecurityScheme bearerScheme = new SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT")
        .in(SecurityScheme.In.HEADER)
        .name("Authorization");

    return new OpenAPI()
        .components(new Components().addSecuritySchemes("bearerAuth", bearerScheme))
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .info(
            new Info()
                .title("Delivery API Rabay")
                .version("1.0")
                .description(
                    "API pública para operações de delivery: cadastro de"
                        + " clientes, restaurantes, produtos e pedidos."
                        + " Documentação detalhada para integração e testes.")
                .termsOfService("https://github.com/rabay/delivery-api-rabay/terms")
                .contact(
                    new io.swagger.v3.oas.models.info.Contact()
                        .name("Equipe Rabay Delivery")
                        .email("contato@rabay.com.br")
                        .url("https://github.com/rabay/delivery-api-rabay"))
                .license(
                    new io.swagger.v3.oas.models.info.License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT")));
  }
}
