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
                        .description("""
                            API REST para sistema de delivery moderno e escalável com CRUD completo para todas as entidades.
                            
                            ### Funcionalidades Principais:
                            - **Gestão de Clientes** - Cadastro e gerenciamento completo de usuários
                            - **Gestão de Restaurantes** - CRUD com categorias e busca avançada
                            - **Catálogo de Produtos** - Gerenciamento por restaurante com disponibilidade
                            - **Sistema de Pedidos** - Fluxo completo com validações de negócio
                            - **Monitoramento** - Health checks e métricas via Actuator
                            
                            ### Padrões da API:
                            - Todos os endpoints retornam JSON estruturado
                            - Validação completa de entrada com mensagens de erro descritivas
                            - Status HTTP apropriados para cada operação
                            - Paginação e filtros quando aplicável
                            """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Delivery Tech Team")
                                .email("tech@deliveryapi.com")
                                .url("https://github.com/deliverytech/api"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desenvolvimento"),
                        new Server()
                                .url("https://api.deliverytech.com")
                                .description("Servidor de Produção")))
                .tags(List.of(
                        new Tag()
                                .name("Health")
                                .description("Endpoints de monitoramento e saúde da aplicação"),
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
                                .name("Test")
                                .description("Operações de teste - APENAS DESENVOLVIMENTO")
                ));
    }
}
