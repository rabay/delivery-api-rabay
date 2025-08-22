---
applyTo: '**'
---

# User Memory

## User Preferences

- Linguagem principal: Java 21
- Framework: Spring Boot 3.2.x
- Banco de dados: H2 (dev), PostgreSQL/MySQL (prod)
- Ferramentas: Maven, Spring DevTools, Spring Data JPA, Spring MVC, Thymeleaf
- Comunicação: Respostas e documentação em português do Brasil
- Estilo de código: Seguir instruções java.instructions.md e junit.instructions.md

## Project Context

- Projeto: delivery-api (módulo delivery-api-rabay)
- Estrutura inicial robusta para delivery: usuários, restaurantes, produtos, pedidos, pagamentos, monitoramento
- application.properties configurado para H2, JPA, devtools, logs debug
- Perfis: dev (H2, DevTools), prod (PostgreSQL/MySQL, HikariCP, SSL)
- Virtual Threads ativado (recomendação)
- Testes automatizados presentes

## Coding Patterns

- Uso de Java Records para DTOs
- Imutabilidade e uso de Streams
- Padrão de pacotes e nomes conforme Google Java Style
- Testes em src/test/java, nomeação descritiva, uso de mocks e assertivas claras
- Cobertura mínima de 80% nos testes, foco em cenários críticos

## Context7 Research History

- Seguir orientações context7 para exemplos, setup, documentação de libs
- Priorizar pesquisa context7 para integração de libs/frameworks
- Jacoco: configuração padrão Maven, não requer pesquisa context7

# Pesquisa sobre uso do subdiretório 'impl' para implementações em Java (22/08/2025)
## Resumo das Melhores Práticas
- O uso de um subpacote `impl` para classes de implementação é prática comum e aceita em projetos Java, especialmente para separar interfaces (API) das implementações concretas.
- Motivações: organização, clareza, encapsulamento e facilidade de manutenção/evolução.
- Recomendado limitar a visibilidade das classes `impl` (package-private) e documentar que apenas as interfaces devem ser consumidas externamente.
- Consistência do padrão é fundamental.
- Alternativa: implementar no mesmo pacote da interface, usando sufixo Impl, mas o padrão `impl` é mais comum em projetos Spring e open source.
- Referências:
	- StackOverflow: https://stackoverflow.com/questions/49002/preferred-java-package-structure-for-interfaces-and-implementations
	- Software Engineering StackExchange: https://softwareengineering.stackexchange.com/questions/179652/is-it-a-good-practice-to-have-an-impl-package-for-implementations
	- Exemplos em projetos Spring Framework e Apache Commons.

## Conversation History


- 22/08/2025: Corrigida action do Dependency-Check no workflow para usar @main (não @v7, que não existe). Adicionado upload do relatório como artefato (dependency-check-report).
- Script Python run_dependency_check.py agora exige a variável de ambiente NVD_API_KEY (não mais hardcoded), garantindo segurança e flexibilidade.
- Pipeline agora executa verificação de dependências localmente e no CI/CD, com API Key configurável.

## Notes

- Se remover ou alterar o Jacoco, garantir sempre a geração do relatório HTML em target/site/jacoco/index.html
- Pipeline e documentação estão alinhados

# ATIVIDADE 2: Padrão para consultas com relacionamentos LAZY (fetch join)

## Resumo e decisão
- Problema: LazyInitializationException ao acessar coleções LAZY (ex: Pedido.itens) fora da sessão do Hibernate, especialmente em validações do DataLoader e testes.
- Solução: Implementado método customizado no PedidoRepository usando @Query com fetch join (findAllWithItens), garantindo que os pedidos sejam carregados já com os itens associados.
- O DataLoader passou a utilizar esse método para validação dos relacionamentos, eliminando o erro e seguindo boas práticas JPA/Spring Data.
- Padrão documentado: sempre que for necessário acessar coleções LAZY em contexto fora do controller/service, criar método com fetch join no repositório.

## Exemplo:
```java
@Query("SELECT DISTINCT p FROM Pedido p LEFT JOIN FETCH p.itens i LEFT JOIN FETCH i.produto")
List<Pedido> findAllWithItens();
```
Uso no DataLoader:
```java
pedidoRepository.findAllWithItens().forEach(p -> {
	System.out.println("Pedido ID: " + p.getId() + ", Itens: " + p.getItens().size());
});
```

## Checklist
- [x] Identificado e documentado padrão para evitar LazyInitializationException
- [x] Implementado método fetch join no PedidoRepository
- [x] DataLoader atualizado para usar consulta correta
- [x] Testes automatizados passaram sem erros

# ATIVIDADE 1: Implementação dos Repositórios (progresso em 21/08/2025)

## Entendimento e ordem de execução
- Análise do padrão dos repositórios de exemplo em /delivery-tech realizada.
- Verificação dos arquivos de repositório existentes no projeto atual realizada.
- Métodos customizados faltantes identificados:
	- ClienteRepository: findByNomeContainingIgnoreCase, existsByEmail
	- RestauranteRepository: findTop5ByOrderByNomeAsc, findByTaxaEntregaLessThanEqual (ajuste assinatura)
	- ProdutoRepository: findByRestauranteId, findByPrecoLessThanEqual
	- PedidoRepository: findTop10ByOrderByDataPedidoDesc
- Implementação incremental dos métodos faltantes concluída para todos os repositórios.
- Checklist de execução:
	- [x] Analisar padrão dos repositórios de exemplo em /delivery-tech
	- [x] Verificar arquivos de repositório existentes no projeto atual
	- [x] Listar métodos customizados faltantes em cada repositório
	- [x] Implementar métodos faltantes em ClienteRepository
	- [x] Implementar métodos faltantes em RestauranteRepository
	- [x] Implementar métodos faltantes em ProdutoRepository
	- [x] Implementar métodos faltantes em PedidoRepository
	- [ ] Revisar e validar se todos os métodos pedidos estão presentes e corretos

## Conversation History

 - Última análise: 21/08/2025
 - Análise sequencial realizada para criação de Dockerfile multi-stage (build e runtime) para Spring Boot Java 21.
 - Dockerfile criado conforme melhores práticas: build com openjdk:21-jdk, runtime com openjdk:21-jre-slim, cópia apenas do JAR, ENTRYPOINT configurado, porta 8080 exposta.
 - Decisão documentada e alinhada com requisitos de segurança, performance e compatibilidade.

## Notes

- Instruções gerais do projeto:
	- Estruturar com Spring Boot BOM
	- Separar perfis dev/prod
	- Ativar Virtual Threads
	- Modelagem: @Version em Product, value objects para Money/Address
	- RBAC com Spring Security, roles específicas
	- Proteção de dados sensíveis (Jasypt, PCI DSS)
	- Idempotência em pagamentos (Idempotency-Key, Redis)
	- Async para gateways de pagamento
	- HATEOAS em respostas de pedidos
	- Códigos de erro padronizados (428, 425)
	- Versionamento explícito da API
	- Observabilidade: Micrometer, logs com MDC, métricas customizadas
	- Testes de concorrência e integração
	- Otimizações: cache de menus, Spring Batch para relatórios
- Seguir instruções java.instructions.md, junit.instructions.md, markdown.instructions.md para qualidade e padronização
