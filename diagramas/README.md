# Diagramas - Delivery API Rabay

Este diretório contém todos os diagramas arquiteturais do sistema de delivery API, utilizando a sintaxe Mermaid para visualização.

## Índice de Diagramas

### 📋 [Diagrama de Arquitetura](./arquitetura.md)
- **Arquitetura Geral do Sistema**: Visão completa das camadas e componentes
- **Arquitetura de Segurança JWT**: Fluxos de autenticação e autorização
- **Componentes Principais**: Descrição detalhada de cada camada

**Quando usar**: Para entender a estrutura geral do sistema, dependências entre componentes e fluxos de segurança.

### 🔄 [Diagrama de Sequência](./sequencia.md)
- **Fluxo de Autenticação e Criação de Pedido**: Processo completo do login até criação do pedido
- **Fluxo de Consulta de Relatórios**: Autorização admin e consultas paralelas
- **Fluxo de Registro de Usuário**: Criação de nova conta com validações
- **Fluxo de Atualização de Status do Pedido**: Transições de status e regras de negócio

**Quando usar**: Para entender a ordem cronológica das operações, interações entre componentes e fluxos de dados.

### 🗺️ [Mapa de Transações CRUD](./mapa-transacoes.md)
- **Operações CRUD por Entidade**: Mapeamento completo de todas as operações
- **Mapa de Transações Detalhado**: Validações e regras de negócio por operação
- **Descrição Detalhada das Operações**: Documentação completa de cada endpoint

**Quando usar**: Para entender todas as operações disponíveis, suas validações, regras de negócio e comportamentos esperados.

## Como Visualizar os Diagramas

### Opção 1: GitHub (Recomendado)
Os arquivos `.md` com diagramas Mermaid são renderizados automaticamente no GitHub. Basta navegar pelos arquivos para visualizar os diagramas.

### Opção 2: VS Code
Instale a extensão "Mermaid Preview" no VS Code para visualizar os diagramas localmente.

### Opção 3: Mermaid Live Editor
Copie o código Mermaid dos arquivos e cole no [Mermaid Live Editor](https://mermaid.live/) para visualização online.

### Opção 4: Documentação Local
Use ferramentas como `mdbook`, `gitbook` ou `docsify` para gerar documentação local com os diagramas renderizados.

## Estrutura dos Arquivos

```
diagramas/
├── README.md              # Este arquivo (índice)
├── arquitetura.md         # Diagramas de arquitetura do sistema
├── sequencia.md          # Diagramas de sequência dos fluxos principais
└── mapa-transacoes.md    # Mapeamento completo das operações CRUD
```

## Convenções Utilizadas

### Cores e Simbolos nos Diagramas
- **Azul**: Componentes de infraestrutura (Controllers, Services)
- **Verde**: Operações de sucesso e fluxos positivos
- **Vermelho**: Tratamento de erros e validações
- **Amarelo**: Componentes de segurança e autenticação
- **Roxo**: Banco de dados e persistência

### Notações Especiais
- `-->` : Chamada síncrona
- `-.->` : Dependência ou configuração
- `-->>` : Resposta ou retorno
- `par/and/end` : Processamento paralelo
- `alt/else/end` : Fluxos condicionais
- `loop/end` : Iterações

## Manutenção dos Diagramas

### Quando Atualizar
- **Novos endpoints**: Atualizar mapa de transações
- **Mudanças na arquitetura**: Atualizar diagrama de arquitetura
- **Novos fluxos de negócio**: Adicionar ao diagrama de sequência
- **Mudanças de segurança**: Atualizar arquitetura de segurança

### Como Contribuir
1. Edite os arquivos `.md` diretamente
2. Mantenha a sintaxe Mermaid válida
3. Teste a renderização antes de commitar
4. Atualize este README se necessário
5. Documente as mudanças no commit

## Ferramentas Relacionadas

- **Mermaid Documentation**: https://mermaid.js.org/
- **Mermaid Live Editor**: https://mermaid.live/
- **VS Code Extension**: Mermaid Preview
- **IntelliJ Plugin**: Mermaid
- **Confluence**: Suporte nativo ao Mermaid

## Próximos Passos

### Diagramas Futuros (Roadmap)
- [ ] Diagrama de Classes (Model/Entity)
- [ ] Diagrama de Componentes Detalhado
- [ ] Diagrama de Implantação (Docker/Cloud)
- [ ] Fluxograma de Tratamento de Erros
- [ ] Diagrama de Casos de Uso
- [ ] Arquitetura de Monitoramento e Observabilidade

### Melhorias Planejadas
- [ ] Adicionar métricas de performance nos fluxos
- [ ] Incluir diagramas de estado para entidades
- [ ] Documentar padrões de retry e circuit breaker
- [ ] Adicionar diagramas de integração externa