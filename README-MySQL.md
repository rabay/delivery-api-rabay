
# Configuração e Inicialização do Banco de Dados MySQL

## Visão Geral

Este documento descreve o processo de configuração e inicialização do banco de dados MySQL para a aplicação Delivery API Rabay. O setup do banco foi reestruturado para garantir a criação correta das tabelas, o estabelecimento dos relacionamentos e a carga de dados durante a inicialização do container MySQL.

## Processo de Inicialização do Banco de Dados

### 1. Criação do Schema

O schema do banco é criado durante a inicialização do container MySQL através do script `data.sql`, que é montado no diretório `/docker-entrypoint-initdb.d/`. Essa abordagem garante que:

- As tabelas sejam criadas antes do início da aplicação
- Os relacionamentos de chave estrangeira sejam corretamente estabelecidos
- Os dados iniciais sejam carregados durante a inicialização do banco
- O Hibernate não precise criar tabelas (ddl-auto está como "none")

### 2. Carga de Dados

Dois scripts SQL distintos realizam a carga de dados:

1. **Dados de Produção/Desenvolvimento**: `src/main/resources/data.sql`
   - Contém comandos de criação de tabelas
   - Inclui dados de exemplo para desenvolvimento
   - Usado no ambiente docker-compose

2. **Dados de Teste**: `src/test/resources/data-test.sql`
   - Contém comandos de criação de tabelas
   - Inclui dados mínimos para testes de integração
   - Usado no ambiente Testcontainers

### 3. Configuração da Aplicação

A aplicação está configurada para trabalhar com o banco já inicializado:

- **Hibernate ddl-auto**: Definido como "none" para evitar que o Hibernate crie/modifique tabelas
- **Spring SQL init mode**: Definido como "always" para garantir o processamento dos scripts
- **Inicialização da fonte de dados**: Postergada para garantir a ordem correta

## Estrutura de Arquivos

```
src/
├── main/
│   └── resources/
│       └── data.sql          # Schema e dados de produção/desenvolvimento
└── test/
   └── resources/
      └── data-test.sql     # Schema e dados de teste
```

## Esquema do Banco de Dados

O banco de dados contém as seguintes tabelas e seus relacionamentos:

1. **cliente** - Informações do cliente
2. **restaurante** - Informações do restaurante
3. **usuario** - Autenticação e autorização de usuários
4. **produto** - Informações de produtos (relacionado a restaurante)
5. **pedido** - Informações de pedidos (relacionado a cliente e restaurante)
6. **item_pedido** - Itens do pedido (relacionado a pedido e produto)

## Instruções de Uso

### Inicialização com Docker Compose

Ao iniciar a aplicação com Docker Compose:

```bash
docker compose up --build
```

O container MySQL irá:
1. Inicializar com o schema do banco
2. Carregar os dados iniciais do `data.sql`
3. Ficar pronto para conexão da aplicação

### Inicialização em Ambiente de Teste

Durante os testes de integração:
1. O Testcontainers inicia um container MySQL
2. Os dados de teste são carregados do `data-test.sql`
3. Os testes rodam contra o banco já inicializado

## Solução de Problemas

### Problemas Comuns

1. **Erro de tabela inexistente**
   - Certifique-se de que os scripts SQL possuem os comandos CREATE TABLE corretos
   - Verifique se os scripts estão montados corretamente no container MySQL

2. **Violação de restrição de chave estrangeira**
   - Confirme que as tabelas "pai" são criadas antes das "filhas"
   - Garanta que os dados referenciais existam antes de criar os relacionamentos

3. **Falha na carga de dados**
   - Verifique se a sintaxe SQL é compatível com o MySQL
   - Consulte os logs do container MySQL para mensagens de erro específicas

### Verificação

Para verificar a inicialização do banco:

```bash
# Conecte-se ao container MySQL
docker exec -it delivery-mysql mysql -u deliveryuser -pdeliverypass deliverydb

# Verifique as tabelas
SHOW TABLES;

# Verifique os dados
SELECT COUNT(*) FROM cliente;
SELECT COUNT(*) FROM restaurante;
SELECT COUNT(*) FROM produto;
```

## Considerações de Segurança

- As credenciais do banco são configuradas via variáveis de ambiente
- O script `data.sql` não deve conter dados sensíveis de produção
- Senhas nos scripts devem estar sempre criptografadas, nunca em texto puro