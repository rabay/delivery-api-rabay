# Implementação de Observabilidade

Este documento fornece uma documentação abrangente dos recursos de observabilidade implementados na aplicação Delivery API Rabay. A implementação segue os três pilares da observabilidade: métricas, logs e traces.

## Visão Geral

A solução de observabilidade está integrada à aplicação Spring Boot 3.5.5 existente utilizando:

- Spring Boot Actuator para verificação de saúde e exposição de métricas
- Micrometer para coleta de métricas e exportação para Prometheus
- Logback para geração de logs estruturados em arquivos locais
- OpenTelemetry para rastreamento distribuído

## Documentação no Swagger

Os endpoints do Actuator estão documentados no Swagger UI. Para acessar:

1. Inicie a aplicação
2. Acesse o Swagger UI em [Swagger UI](http://localhost:8080/swagger-ui/index.html)
3. Os endpoints de observabilidade estarão disponíveis na mesma interface que os endpoints principais da aplicação
4. Os endpoints do Actuator estarão identificados com o prefixo `/actuator/`

## Lista de Endpoints

### Endpoints do Actuator

| Endpoint | Método | Descrição | Autenticação |
|----------|--------|-----------|--------------|
| `/actuator/health` | GET | Informações detalhadas de saúde | Nenhuma (básico), Admin (detalhado) |
| `/actuator/metrics` | GET | Lista de métricas disponíveis | Admin |
| `/actuator/metrics/{name}` | GET | Detalhes de métrica específica | Admin |
| `/actuator/prometheus` | GET | Exportação de métricas para Prometheus | Admin |
| `/actuator/info` | GET | Informações da aplicação | Nenhuma |
| `/actuator/env` | GET | Propriedades de ambiente | Admin |
| `/actuator/loggers` | GET | Configuração de loggers | Admin |

## Métricas

### Métricas de Negócio

Métricas de negócio customizadas foram implementadas utilizando Micrometer:

1. **Métricas de Pedido**
   - `orders.created` - Contador de pedidos criados
   - `orders.confirmed` - Contador de pedidos confirmados
   - `orders.delivered` - Contador de pedidos entregues
   - `orders.cancelled` - Contador de pedidos cancelados
   - `order.processing.time` - Temporizador para duração do processamento de pedidos

2. **Métricas de Produto**
   - `products.created` - Contador de produtos criados
   - `products.updated` - Contador de produtos atualizados
   - `products.deleted` - Contador de produtos excluídos
   - `products.available` - Gauge para produtos disponíveis

3. **Métricas de Usuário**
   - `users.registered` - Contador de usuários registrados
   - `users.login.success` - Contador de logins bem-sucedidos
   - `users.login.failed` - Contador de logins falhos
   - `user.login.time` - Temporizador para duração do login

### Métricas de Sistema

A aplicação também expõe métricas padrão da JVM e do sistema via Micrometer:

- Uso de memória da JVM
- Estatísticas de garbage collection
- Utilização de threads
- Latência de requisições HTTP
- Taxa de erros HTTP

## Logging

### Logging Estruturado em JSON

A aplicação utiliza Logback com Logstash encoder para gerar logs estruturados em JSON. Os logs são gravados em arquivos locais no diretório `entregaveis/logs` com a seguinte estrutura:

- `application.log` - Logs gerais da aplicação
- `clientes.log` - Logs relacionados a clientes
- `restaurantes.log` - Logs relacionados a restaurantes
- `produtos.log` - Logs relacionados a produtos
- `pedidos.log` - Logs relacionados a pedidos
- `auth.log` - Logs de autenticação
- `relatorios.log` - Logs de relatórios
- `health.log` - Logs de verificação de saúde
- `db.log` - Logs relacionados ao banco de dados
- `outros.log` - Outros logs

### Formato dos Logs

Cada entrada de log é estruturada em JSON com os seguintes campos:

- `timestamp` - Timestamp no formato ISO 8601
- `log.level` - Nível do log (INFO, WARN, ERROR, etc.)
- `logger.name` - Nome do logger
- `message` - Mensagem do log
- `mdc` - Mapped Diagnostic Context (inclui IDs de correlação)
- `stack_trace` - Stack trace para exceções

### Correlation ID

Cada requisição recebe um correlation ID único, propagado via MDC (Mapped Diagnostic Context), permitindo rastreamento da requisição entre os arquivos de log.

## Rastreamento Distribuído

A aplicação utiliza OpenTelemetry para rastreamento distribuído. Os traces são gerados automaticamente para todas as requisições e podem ser visualizados em sistemas compatíveis.

## Health Checks

### Indicadores de Saúde Customizados

A aplicação inclui indicadores de saúde customizados para:

1. **Saúde do Banco de Dados**
   - Verifica conectividade com o banco MySQL
   - Reporta status do banco como UP ou DOWN

2. **Saúde de Serviços Externos**
   - Verifica conectividade com Google ([https://www.google.com](https://www.google.com))
   - Verifica conectividade com Amazon ([https://www.amazon.com](https://www.amazon.com))
   - Reporta status do serviço como UP ou DOWN

## Monitoramento e Alertas

### Serviços Docker Compose

O ambiente de desenvolvimento inclui serviços de monitoramento:

1. **Prometheus** - Servidor de coleta de métricas

   - Acessível em [http://localhost:9090](http://localhost:9090)
   - Coleta métricas da aplicação a cada 15 segundos

2. **Grafana** - Dashboard de visualização

   - Acessível em [http://localhost:3000](http://localhost:3000)
   - Pré-configurado com Prometheus como fonte de dados

### Métricas-Chave para Monitorar

- Uso de memória da JVM
- Latência de requisições HTTP
- Taxa de erros por endpoint
- Uso do pool de conexões do banco
- Vazão de processamento de pedidos

### Regras de Alerta

As seguintes regras de alerta devem ser configuradas no Prometheus:

- Taxa de erro alta (>5% em 5 minutos)
- Respostas lentas (>2s para o percentil 95)
- Espaço em disco baixo (<10% restante)
- Falhas de health check (3 falhas consecutivas)

## Considerações de Segurança

### Segurança dos Endpoints do Actuator

- Endpoints públicos: `/actuator/health`, `/actuator/info` (apenas detalhes básicos)
- Endpoints protegidos: `/actuator/metrics`, `/actuator/env`, `/actuator/loggers` (apenas acesso admin)
- Todos os endpoints protegidos exigem autenticação JWT

### Segurança dos Logs

- Dados sensíveis (senhas, tokens, PII) são mascarados nos logs
- Arquivos de log são armazenados localmente com acesso restrito
- Políticas de rotação e retenção de logs são implementadas

## Considerações de Deploy

### Configuração Docker

- Configuração do endpoint de health check
- Limites de recursos para o container
- Montagem de volume para logs
- Exposição da porta para endpoint de métricas (8080)

### Configuração Específica por Ambiente

- Níveis de log por ambiente
- Taxas de amostragem de métricas
- Limites de health check
- Configurações de alertas

## Testes

Todos os testes de integração estendem `BaseIntegrationTest` para centralizar a instância MySQL do Testcontainers. O teste existente `HealthControllerTest` foi atualizado para testar o endpoint `/actuator/health` do Actuator ao invés do endpoint customizado `/health`.

## Arquivos de Configuração

### Configuração da Aplicação

O arquivo application.yml inclui a configuração do Actuator:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus,env,loggers
  endpoint:
    health:
      show-details: always
```

### Configuração do Logback

O arquivo logback-spring.xml configura logging estruturado em JSON com appenders de rotação e integração MDC.

### Configuração do Prometheus

O arquivo config/prometheus/prometheus.yml configura a coleta de métricas da aplicação.
