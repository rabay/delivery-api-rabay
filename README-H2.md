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

## Testes rápidos

1. Inicie a aplicação: `./scripts/start_server.sh --port 8080 --no-tail`
2. Abra o H2 Console e execute as queries acima
3. Verifique em `TABLE_CONSTRAINTS`, `REFERENTIAL_CONSTRAINTS` e `KEY_COLUMN_USAGE` se as FKs aparecem conforme esperado

Se quiser, posso executar automaticamente as queries de verificação (extração programática de `TABLE_CONSTRAINTS` / `KEY_COLUMN_USAGE`) e colar os resultados aqui — diga apenas “execute” e eu retorno com a saída.

---
Arquivo gerado automaticamente para a Atividade 4 (H2). Mantê-lo em `README-H2.md` na raiz do projeto.
