package com.deliverytech.deliveryapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração do OpenAPI/Swagger para documentação da API
 * 
 * Acesso à documentação:
 * - Swagger UI: http://localhost:8080/swagger-ui/index.html
 * - JSON Schema: http://localhost:8080/v3/api-docs
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI deliveryOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Delivery API")
                        .description("API REST para sistema de delivery moderno e escalável com CRUD completo para todas as entidades")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Delivery Tech Team")
                                .email("tech@deliveryapi.com")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desenvolvimento")))
                .tags(List.of(
                        new Tag()
                                .name("Customers")
                                .description("Operações CRUD para gerenciamento de clientes do sistema de delivery"),
                        new Tag()
                                .name("Restaurants")
                                .description("Operações CRUD para gerenciamento de restaurantes com busca por categoria"),
                        new Tag()
                                .name("Products")
                                .description("Operações CRUD para gerenciamento de produtos com busca por restaurante"),
                        new Tag()
                                .name("Orders")
                                .description("Operações para gerenciamento de pedidos, consultas por cliente/restaurante e controle de status"),
                        new Tag()
                                .name("Health")
                                .description("Endpoints de monitoramento e saúde da aplicação")));
    }
}
