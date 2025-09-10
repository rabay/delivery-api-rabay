# Documentação de Observabilidade - Delivery API Rabay

Este documento detalha as funcionalidades de observabilidade implementadas na aplicação, como parte do plano de trabalho `ENTREGA-09`.

## 1. Visão Geral

A aplicação foi instrumentada para fornecer visibilidade completa sobre seu comportamento em ambientes de produção, cobrindo as áreas de Métricas, Logs e Tracing.

## 2. Spring Boot Actuator

O Spring Boot Actuator foi configurado para expor um conjunto de endpoints de gerenciamento, fornecendo informações sobre a saúde e o estado da aplicação.

### Endpoints Expostos

Os seguintes endpoints estão habilitados e acessíveis sob o caminho base `/actuator`:

-   `/actuator/health`: Mostra a saúde da aplicação, incluindo o estado do banco de dados, espaço em disco e conectividade com serviços externos.
-   `/actuator/info`: Exibe informações customizadas da aplicação.
-   `/actuator/metrics`: Fornece métricas detalhadas da aplicação, JVM, e mais.
-   `/actuator/prometheus`: Expõe as métricas no formato compatível com o Prometheus, para scraping.
-   `/actuator/env`: Mostra as variáveis de ambiente e propriedades de configuração.
-   `/actuator/configprops`: Descreve os beans de configuração.
-   `/actuator/loggers`: Permite visualizar e modificar os níveis de log em tempo de execução.

### Health Checks Customizados

-   **ExternalServicesHealthIndicator**: Verifica a conectividade com serviços externos (Google e Amazon).

## 3. Métricas com Micrometer e Prometheus

A aplicação utiliza o Micrometer para coletar métricas e as expõe no formato Prometheus.

### Métricas Customizadas

Além das métricas padrão (JVM, CPU, HTTP, etc.), as seguintes métricas de negócio foram implementadas:

-   `usuarios_ativos`: (Gauge) Número de usuários ativos no sistema.
-   `pedidos_status`: (Gauge) Número de pedidos em cada um dos status (`CRIADO`, `CONFIRMADO`, `ENTREGUE`, etc.), com a tag `status`.
-   `pedidos_creation_timer`: (Timer) Mede a latência da operação de criação de novos pedidos, com percentis de 0.5 (mediana) e 0.95.

## 4. Logging Estruturado e Centralizado

O sistema de logging foi configurado para gerar logs em formato JSON estruturado, facilitando a busca e análise em ferramentas de agregação de logs.

### Logback

-   **Console Appender**: Envia logs em formato JSON para a saída padrão.
-   **File Appender**: Salva os logs em arquivos com política de rotação (`rolling policy`).

### Correlation ID

-   Um `correlationId` (rastreamento de requisição) é gerado para cada requisição recebida.
-   Este ID é adicionado ao `MDC` (Mapped Diagnostic Context) do Logback, sendo incluído em todas as linhas de log correspondentes àquela requisição.
-   O `correlationId` também é retornado no cabeçalho `X-Correlation-ID` da resposta HTTP, permitindo o rastreamento ponta-a-ponta.

## 5. Tracing Distribuído com OpenTelemetry

Para rastreamento distribuído, a aplicação foi configurada para usar o OpenTelemetry, a solução moderna adotada pelo Spring Boot 3.

### Nota sobre a Implementação

O plano original sugeria o uso do `Spring Cloud Sleuth`. No entanto, como o Sleuth foi descontinuado, optou-se por utilizar o OpenTelemetry, que é o padrão atual e oferece melhor integração e futuro suporte.

### Configuração

-   Os traces são gerados e propagados automaticamente para todas as requisições HTTP e interações com o banco de dados.
-   A aplicação está configurada para exportar os traces para um coletor OpenTelemetry (OTLP) no endpoint `http://localhost:4318/v1/traces`.
-   O sampling está configurado para `1.0` (100%), o que significa que todas as requisições serão rastreadas.

## 6. Dashboards e Alertas

### Dashboards (Grafana)

-   O diretório `config/grafana` foi criado para armazenar futuras configurações de dashboards do Grafana. Nenhum dashboard foi criado ainda.

### Alertas (TODO)

-   **A fazer**: A configuração de um sistema de alertas (ex: Alertmanager) ainda está pendente. As métricas expostas pelo Prometheus podem ser usadas para criar regras de alerta para condições como:
    -   Taxa de erros HTTP elevada.
    -   Latência de criação de pedidos acima de um limiar.
    -   Saúde da aplicação no estado `DOWN`.
    -   Uso de recursos da JVM (CPU, memória) acima de um limiar.
