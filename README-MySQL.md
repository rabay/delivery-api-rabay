

# Configuração e Execução do Banco de Dados MySQL

Este guia detalha como configurar, inicializar e utilizar o banco de dados MySQL para a aplicação Delivery API Rabay, cobrindo desde a estrutura de arquivos até dicas de troubleshooting e segurança.

---

## 📦 Estrutura de Arquivos Relacionados ao Banco

```text
src/
├── main/
│   └── resources/
│       └── data.sql          # Schema e dados de produção/desenvolvimento
└── test/
    └── resources/
        └── data-test.sql     # Schema e dados de teste
docker-compose.yml           # Orquestração dos containers (MySQL e aplicação)
config/
└── mysql/
    ├── 01-init.sql          # Scripts customizados de inicialização
    ├── 02-charset.sql       # Configuração de charset
    └── 03-optimize.sql      # Otimizações de banco
```

---

## 🚀 Inicialização e Execução

⚠️ **IMPORTANTE:** Sempre utilize Docker Compose para garantir que o banco MySQL seja provisionado corretamente antes da aplicação.

1. **Suba o ambiente completo:**
   ```bash
   docker compose up --build
   ```
   - O serviço `mysql` será inicializado com os scripts de schema e dados.
   - O serviço da aplicação só tentará conectar após o banco estar pronto.

2. **Acesso ao banco para verificação:**
   ```bash
   docker exec -it delivery-mysql mysql -u deliveryuser -pdeliverypass deliverydb
   SHOW TABLES;
   SELECT COUNT(*) FROM cliente;
   ```

---

## 🗄️ Detalhes de Configuração

- **Usuário padrão:** `deliveryuser`
- **Senha:** `deliverypass`
- **Database:** `deliverydb`
- **Host:** `delivery-mysql` (no contexto do Docker Compose)
- **Porta:** `3306`
- **Variáveis de ambiente:** Definidas no `docker-compose.yml` para facilitar customização.
- **Scripts de inicialização:**
  - `data.sql` (schema e dados)
  - Scripts adicionais em `config/mysql/` são executados na ordem de prefixo numérico.

---

## 🏗️ Esquema e Relacionamentos

O banco de dados contém as seguintes tabelas principais:

1. **cliente** - Dados cadastrais dos clientes
2. **restaurante** - Dados dos restaurantes
3. **usuario** - Controle de autenticação e perfis
4. **produto** - Produtos ofertados (relacionados a restaurante)
5. **pedido** - Pedidos realizados (relacionados a cliente e restaurante)
6. **item_pedido** - Itens de cada pedido (relacionados a pedido e produto)

Todos os relacionamentos de chave estrangeira são definidos nos scripts SQL.

---

## 🧪 Ambientes de Execução

- **Desenvolvimento/Produção:**
  - Usa `data.sql` para schema e dados iniciais.
  - Scripts em `config/mysql/` podem ser customizados conforme necessidade.
- **Testes automatizados:**
  - Usa `data-test.sql` (mínimo necessário para integração).
  - Testcontainers inicializa o banco isoladamente para cada execução.

---

## 🛠️ Troubleshooting

1. **Erro de tabela inexistente:**
   - Confirme se os scripts SQL estão corretos e montados no container.
2. **Violação de chave estrangeira:**
   - Garanta a ordem de criação das tabelas e a existência dos dados referenciais.
3. **Falha na carga de dados:**
   - Verifique a sintaxe SQL e consulte os logs do container MySQL.
4. **Conexão recusada:**
   - Certifique-se de que o serviço MySQL está "healthy" antes de subir a aplicação.

---

## 🔒 Segurança

- As credenciais do banco são definidas via variáveis de ambiente e podem ser alteradas facilmente.
- Nunca armazene dados sensíveis ou senhas reais em scripts versionados.
- Senhas de usuários de teste/demonstrativo devem ser criptografadas.
- O acesso ao banco fora do ambiente Docker deve ser restrito.

---

## 📚 Referências e Dicas

- [Documentação oficial MySQL Docker](https://hub.docker.com/_/mysql)
- [Spring Boot DataSource Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#application-properties-data)
- [Testcontainers MySQL Module](https://www.testcontainers.org/modules/databases/mysql/)

---

**Dica:** Para customizar o banco, adicione scripts `.sql` em `config/mysql/` com prefixos numéricos para garantir a ordem de execução.