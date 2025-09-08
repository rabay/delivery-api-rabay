# ACID e Garantia de Integridade Transacional

Este documento explica de forma prática os princípios ACID, exemplos de rollback e a recomendação de testes de concorrência para aplicações de delivery.

## 1. Princípios ACID

- Atomicidade (Atomicity): cada transação é atômica — ou todas as suas alterações são aplicadas, ou nenhuma.
- Consistência (Consistency): transações levam o sistema de um estado consistente para outro, respeitando constraints e regras de negócio.
- Isolamento (Isolation): transações concorrentes não devem interferir de forma que corrompa dados; níveis de isolamento controlam visibilidade parcial.
- Durabilidade (Durability): uma vez confirmada (commit), a alteração persiste mesmo após falhas de sistema.

## 2. Comportamento de rollback no Spring

Por padrão, o Spring faz rollback automático para `RuntimeException` e `Error`. Para checked exceptions é necessário configurar `rollbackFor` explicitamente.

Exemplo (rollback para checked exceptions):

```java
@Transactional(rollbackFor = Exception.class)
public void metodoQuePodeLancarChecked() throws Exception {
  // lógica
  throw new Exception("Forçar rollback para checked exception");
}
```

Também é possível marcar a transação programaticamente para rollback:

```java
TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
```

## 3. Exemplos de rollback

- Validação de dados falha -> lançar `RuntimeException`/`BusinessException` para garantir rollback.
- Falha em integração externa (pagamento) -> relançar exceção apropriada para provocar rollback.
- Erro em reserva de estoque por concorrência -> lançar `EstoqueInsuficienteException` e garantir que a transação foi revertida.

## 4. Concorrência e por que é crítica

No cenário de delivery, múltiplos pedidos concorrentes podem tentar reservar o mesmo produto. Sem proteção, é possível "oversell" (venda de estoque inexistente).

Estratégias comuns:
- Optimistic Locking (@Version) com retry limitado.
- Pessimistic Locking (SELECT ... FOR UPDATE) quando necessário.
- Testes de concorrência automatizados que simulem múltiplas threads.

## 5. Como testar concorrência (passo a passo)

1. Use um banco real nos testes de integração (Testcontainers) ou um profile com H2 configurado para testes.
2. Insira um produto com estoque conhecido (por exemplo, 5 unidades).
3. Dispare N threads (ex.: 10) que tentem reservar 1 unidade cada.
4. Verifique que o número de reservas bem sucedidas não excede o estoque inicial.
5. Valide que transações que lançaram exceção não alteraram o estoque (rollback).

## 6. Checklist rápido para desenvolvedores

- [ ] Métodos que alteram estado crítico anotados com `@Transactional`.
- [ ] Exceções que devem provocar rollback são `RuntimeException` ou configuradas em `rollbackFor`.
- [ ] Tests de concorrência implementados e executados regularmente.
- [ ] Uso de `@Version` em entidades críticas (ex.: `Produto`) para evitar oversell.
- [ ] Logs/MDC para rastrear transações por `orderId`/`produtoId`.

## 7. Referências no repositório

- Serviço de estoque: `src/main/java/com/deliverytech/delivery_api/service/impl/ProdutoServiceImpl.java`
- Entidade `Produto`: `src/main/java/com/deliverytech/delivery_api/model/Produto.java`

----

Este README-ACID.md foi gerado para documentar práticas e testes mínimos necessários para garantir propriedades transacionais em fluxos críticos do sistema.
