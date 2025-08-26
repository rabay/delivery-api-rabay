# Acesso ao H2 Console e queries úteis

Este documento descreve como acessar o H2 Console embutido na aplicação e fornece um conjunto de consultas úteis para validar a criação de tabelas, constraints e dados (ambiente de desenvolvimento).

## Acesso ao H2 Console

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:deliverydb`
- User Name: `sa`
- Password: (deixe em branco)

Observação: o console deve estar habilitado apenas em ambiente `dev`. A aplicação deste repositório já possui `spring.h2.console.enabled=true` no arquivo `src/main/resources/application.properties`.

## Queries recomendadas (copiar/colar no H2 Console)

- Listar tabelas:

```sql
SELECT TABLE_NAME
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_SCHEMA = 'PUBLIC'
ORDER BY TABLE_NAME;
```

- Listar colunas de uma tabela (ex.: `PEDIDO`):

```sql
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'PEDIDO'
  AND TABLE_SCHEMA = 'PUBLIC'
ORDER BY ORDINAL_POSITION;
```

- Listar constraints (PK/UNIQUE) por tabela:

```sql
SELECT *
FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
WHERE TABLE_SCHEMA = 'PUBLIC'
ORDER BY TABLE_NAME, CONSTRAINT_NAME;
```

- Listar constraints referenciais (FKs):

```sql
SELECT *
FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS
WHERE CONSTRAINT_SCHEMA = 'PUBLIC'
ORDER BY CONSTRAINT_NAME;
```

- Listar colunas de chave (mostra FKs e colunas referenciadas):

```sql
SELECT CONSTRAINT_NAME, TABLE_NAME, COLUMN_NAME, REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'PUBLIC'
  AND REFERENCED_TABLE_NAME IS NOT NULL
ORDER BY TABLE_NAME, CONSTRAINT_NAME;
```

- Visualizar dados das entidades principais:

```sql
SELECT * FROM CLIENTE;
SELECT * FROM RESTAURANTE;
SELECT * FROM PRODUTO;
SELECT * FROM PEDIDO;
SELECT * FROM ITEM_PEDIDO;
```

- Teste rápido de relacionamento (join):

```sql
SELECT p.id AS pedido_id, p.numero_pedido, i.id AS item_id, i.produto_id, i.quantidade
FROM PEDIDO p
LEFT JOIN ITEM_PEDIDO i ON p.id = i.pedido_id
ORDER BY p.id;
```

- Obter DDL aproximado (usar SCRIPT e procurar CREATE TABLE):

```sql
SCRIPT; -- ou SCRIPT NODATA;
```

> Nota: o H2 não suporta `SHOW CREATE TABLE`. Use o comando `SCRIPT` e procure o CREATE TABLE desejado no resultado.

## Observações sobre logs SQL e parâmetros (bind values)

- A aplicação já registra SQL formatado via `spring.jpa.properties.hibernate.format_sql=true` e `logging.level.org.hibernate.SQL=DEBUG`.
- Para ver os valores dos parâmetros (bind values) no log, ajuste o `application.properties` adicionando ou garantindo:

```properties
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

- Após essa alteração, reinicie a aplicação para que as linhas de TRACE apareçam e mostrem os valores bindados.

## Dados iniciais

- O projeto inclui `src/main/resources/data.sql` com inserts idempotentes (MERGE) usados em ambiente de desenvolvimento. Se `spring.jpa.hibernate.ddl-auto=create-drop` estiver ativo, o schema é recriado a cada start e o `data.sql` é aplicado.

## Novos endpoints de inspeção do banco (para desenvolvimento)

Durante a Atividade 4 foi implementado um conjunto de endpoints REST que permitem inspecionar o estado do H2 em memória a partir da própria JVM da aplicação. Isso evita problemas ao tentar usar ferramentas externas contra bancos "in-memory" que vivem em uma instância diferente.

- GET `/db/schema`
  - Retorna lista de tabelas, constraints, metadados (quando solicitado) e uma contagem rápida de registros por tabela.
  - Exemplo: `curl -s http://localhost:8080/db/schema | jq .`

- GET `/db/schema?table=PEDIDO`
  - Retorna metadados (colunas) para a tabela informada. O nome da tabela é case-insensitive.
  - Exemplo: `curl -s "http://localhost:8080/db/schema?table=PEDIDO" | jq .`

- GET `/db/integrity`
  - Executa um teste de integridade simples: tenta inserir um registro inválido em `ITEM_PEDIDO` (chaves estrangeiras incorretas) e retorna o resultado da operação. Útil para confirmar que FK estão efetivamente aplicadas.
  - Exemplo: `curl -s http://localhost:8080/db/integrity | jq .`

- POST `/db/query`
  - Aceita um JSON com `{ "sql": "SELECT ..." }` e executa apenas queries `SELECT` (validação simples impede DML/DDL). Retorna linhas e contagem.
  - Uso: `curl -s -X POST -H "Content-Type: application/json" -d '{"sql":"SELECT * FROM CLIENTE LIMIT 10"}' http://localhost:8080/db/query | jq .`

Segurança: esses endpoints são destinados a ambiente de desenvolvimento; se for commitar ou expor o código, proteja-os via `@Profile("dev")` e/ou autenticação.

## Testes rápidos

1. Inicie a aplicação: `./scripts/start_server.sh --port 8080 --no-tail`
2. Abra o H2 Console e execute as queries acima ou use os endpoints internos `/db/*` para inspeção programática
3. Verifique em `TABLE_CONSTRAINTS`, `REFERENTIAL_CONSTRAINTS` e `KEY_COLUMN_USAGE` se as FKs aparecem conforme esperado

## Checklist da solicitação

Este checklist mostra o status atual dos requisitos solicitados na Atividade 4 (H2, logs e validação):

- [x] Configurar H2 (in-memory) e H2 Console ativado para dev
- [x] Fornecer `data.sql` idempotente e evitar sua execução automática quando conflitar com DataLoader
- [x] Habilitar logging de SQL (`org.hibernate.SQL=DEBUG`)
- [x] Capturar bind values (BasicBinder) — habilitado temporariamente para verificação; revertido depois
- [x] Implementar endpoints de inspeção em-JVM (`/db/schema`, `/db/integrity`, `/db/query`)
- [x] Remover scripts bash de verificação externos (scripts experimentais removidos)
- [x] Atualizar documentação (`README-H2.md` e README principal) com instruções de uso
- [x] Reverter TRACE root e reduzir verbosidade de logs (feito)
- [ ] Proteger endpoints `/db/*` com `@Profile("dev")` e/ou autenticação (recomendado)

Se precisar, eu aplico a proteção por profile nos controllers de inspeção e atualizo a documentação.

---
Arquivo gerado e atualizado para a Atividade 4 (H2). Mantê-lo em `README-H2.md` na raiz do projeto.
