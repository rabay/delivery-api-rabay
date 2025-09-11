package com.deliverytech.delivery_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    SecurityScheme bearerScheme =
        new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            .name("Authorization");

    // Create tags for the main API endpoints
    List<Tag> tags = new ArrayList<>();
    tags.add(new Tag().name("Clientes").description("Operações relacionadas a clientes"));
    tags.add(new Tag().name("Restaurantes").description("Operações relacionadas a restaurantes"));
    tags.add(new Tag().name("Produtos").description("Operações relacionadas a produtos"));
    tags.add(new Tag().name("Pedidos").description("Operações relacionadas a pedidos"));
    tags.add(new Tag().name("Autenticação").description("Endpoints de autenticação e autorização"));

    return new OpenAPI()
        .components(new Components().addSecuritySchemes("bearerAuth", bearerScheme))
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .tags(tags)
        .info(
            new Info()
                .title("Delivery API Rabay")
                .version("1.0")
                .description(
                    "API pública para operações de delivery: cadastro de"
                        + " clientes, restaurantes, produtos e pedidos."
                        + " Documentação detalhada para integração e testes."
                        + "\n\n## Observabilidade"
                        + "\n\nEsta API inclui endpoints de observabilidade do Spring Boot Actuator."
                        + " Os principais endpoints de observabilidade incluem:"
                        + "\n- **Health**: `/actuator/health` - Verificação de saúde do sistema"
                        + "\n- **Metrics**: `/actuator/metrics` - Métricas da aplicação"
                        + "\n- **Info**: `/actuator/info` - Informações da aplicação"
                        + "\n- **Prometheus**: `/actuator/prometheus` - Exportação de métricas para Prometheus"
                        + "\n\nOs endpoints de health e info são públicos, enquanto os demais requerem autenticação de administrador.")
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
