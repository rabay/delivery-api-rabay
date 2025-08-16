# 🚀 Delivery Tech API - Collections Postman v2.0

Este diretório contém as collections e scripts automatizados para testar a **Delivery Tech API** usando **Newman** (CLI do Postman).

## 📦 Arquivos Principais

### Collections Unificadas
- **`Delivery-API-Unified-Collection.postman_collection.json`** ⭐ **(Collection Principal)**
  - Collection completa com 19 requests organizados em 6 seções
  - 66 assertions para validação robusta dos endpoints
  - Taxa de sucesso atual: ~74% (49/66 assertions passando)
  - Cobertura completa da API: Health, Customers, Restaurants, Products, Orders

- **`Delivery-API-LoadTest.postman_collection.json`**
  - Collection específica para testes de carga e performance
  - Otimizada para execução em múltiplas iterações

### Configuração e Ambiente
- **`Delivery-API-Local.postman_environment.json`**
  - Variáveis de ambiente para desenvolvimento local
  - Configurações de URL base, timeouts e dados de teste

### Scripts de Automação
- **`run-tests.sh`** ⭐ **(Script Principal - v2.0)**
  - Script refatorado com interface moderna e funcionalidades avançadas
  - Utiliza a Collection Unificada como padrão
  - 7 opções de execução diferentes com relatórios HTML detalhados

## 🎯 Collection Unificada - Estrutura

A Collection Unificada está organizada nas seguintes seções:

### 🏠 Health & System (3 requests)
- API Health Check
- System Status
- Database Initialization

### 👥 Customers - Gerenciamento de Clientes (4 requests)
- Listar todos os customers
- Obter customer por ID
- Criar novo customer
- Atualizar customer existente

### 🍽️ Restaurants - Gestão de Restaurantes (4 requests)
- Listar restaurants
- Obter restaurant por ID
- Criar novo restaurant
- Gerenciar produtos do restaurant

### 🍕 Products - Catálogo de Produtos (4 requests)  
- Listar todos os produtos
- Obter produto por ID
- Criar novo produto
- Atualizar produto existente

### 📦 Orders - Gestão de Pedidos (4 requests)
- Listar pedidos
- Obter pedido por ID
- Criar novo pedido
- Atualizar status do pedido

## 🚀 Como Executar os Testes

### 1. Executar com o Script Automatizado (Recomendado)

```bash
# Tornar o script executável (primeira vez)
chmod +x run-tests.sh

# Executar o script
./run-tests.sh
```

**Opções do Menu v2.0:**
1. **🚀 Testes Completos** - Collection Unificada completa (19 requests)
2. **⚡ Testes de Carga** - Performance testing com múltiplas iterações  
3. **🎯 Testes por Seção** - Executar apenas uma seção específica
4. **🔧 Diagnóstico Rápido** - Health check e validação básica
5. **📊 Mostrar Estatísticas** - Visualizar histórico de relatórios
6. **🔄 Todos os Testes** - Bateria completa (Diagnóstico + Completos + Carga)
7. **👋 Sair**

### 2. Executar Manualmente com Newman

#### Collection Completa
```bash
newman run Delivery-API-Unified-Collection.postman_collection.json \
  --environment Delivery-API-Local.postman_environment.json \
  --reporters cli,htmlextra \
  --reporter-htmlextra-export reports/manual-test-$(date +%Y%m%d_%H%M%S).html
```

#### Apenas uma Seção Específica
```bash
# Exemplo: testar apenas Customers
newman run Delivery-API-Unified-Collection.postman_collection.json \
  --environment Delivery-API-Local.postman_environment.json \
  --folder "👥 Customers - Gerenciamento de Clientes"
```

#### Testes de Carga
```bash
newman run Delivery-API-LoadTest.postman_collection.json \
  --environment Delivery-API-Local.postman_environment.json \
  --iteration-count 50 \
  --delay-request 100
```

## 📊 Relatórios e Métricas

### Status Atual da Collection Unificada
- ✅ **Requests Funcionando:** 19/19 (100%)
- ⚠️ **Assertions:** 49/66 passando (74%)
- 🔧 **Principais Issues:** Timing de variáveis, validações de schema

### Relatórios HTML
- Gerados automaticamente na pasta `reports/`
- Nomenclatura com timestamp: `unified-test-results-20250815_143022.html`
- Visualização detalhada com gráficos e métricas de performance
- Logs completos de requests/responses para debugging

### Monitoramento
O script v2.0 inclui:
- ✅ Verificação automática da API antes dos testes
- 📊 Contagem de dados disponíveis (customers, restaurants)
- ⏱️ Timeouts configuráveis e delays otimizados
- 🎨 Interface colorida com indicadores visuais de status

## 🛠️ Pré-requisitos

### Software Necessário
- **Node.js** (v14+)
- **Newman**: `npm install -g newman newman-reporter-htmlextra`
- **jq**: Para parsing de JSON (opcional, para estatísticas)
- **curl**: Para health checks da API

### API em Execução
```bash
# Na raiz do projeto
mvn spring-boot:run
```

A API deve estar respondendo em `http://localhost:8080`

## 🎯 Melhorias na v2.0

### ✨ Novas Funcionalidades
- **Collection Unificada:** Consolida múltiplas collections em uma única estrutura organizada
- **Interface Aprimorada:** Menu interativo com 7 opções e interface visual melhorada
- **Diagnóstico Inteligente:** Verificação automática da API e dados antes dos testes
- **Testes por Seção:** Capacidade de executar apenas partes específicas da collection
- **Estatísticas:** Histórico e métricas dos testes executados

### 🔧 Otimizações Técnicas
- **Delays Otimizados:** 300ms para collection completa, 100ms para diagnóstico
- **Timeouts Configuráveis:** 10s para requests normais, 15s para load tests
- **Relatórios Aprimorados:** Templates HTML com mais detalhes e melhor visualização
- **Error Handling:** Tratamento robusto de erros com messages informativos

### 📁 Organização Melhorada
- Limpeza de collections antigas desnecessárias
- Estrutura de pastas mais clara
- Documentação atualizada e completa
- Scripts com comentários detalhados

## 🐛 Debugging e Troubleshooting

### Problemas Comuns

**1. API não responde**
```bash
# Verificar se a API está rodando
curl http://localhost:8080/health
```

**2. Newman não encontrado**
```bash
# Instalar Newman globalmente
npm install -g newman newman-reporter-htmlextra
```

**3. Assertions falhando**
- Verificar se DataInitializer está populando dados corretamente
- Aguardar inicialização completa da API (~10 segundos)
- Checar logs da aplicação para erros

**4. Timeouts frequentes**
- Aumentar delay entre requests no script
- Verificar performance da máquina/rede
- Usar testes por seção para debugging específico

## 📈 Próximos Passos

### Melhorias Planejadas
- [ ] Aumentar taxa de sucesso das assertions para 90%+
- [ ] Implementar testes de integração com database real
- [ ] Adicionar collection para testes de security
- [ ] Integração com CI/CD pipeline
- [ ] Dashboard web para visualização de métricas

### Collection Evolution
- [ ] Implementar data-driven testing avançado
- [ ] Adicionar testes de edge cases
- [ ] Validações de schema mais rigorosas
- [ ] Mock servers para testes isolados

## 📞 Suporte

Para problemas ou dúvidas:
1. Verifique os logs nos relatórios HTML
2. Execute o diagnóstico rápido (opção 4)
3. Consulte a documentação da API
4. Revise os comentários no código das collections
