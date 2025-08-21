---
applyTo: 'entregaveis/README-atividade4.md'
summary: 'Checklist, instruções e evidências da Atividade 4 do Delivery API Rabay.'
---

# Entregáveis Atividade 4 - Delivery API Rabay

## 1. Collection de Testes

- Arquivo: `entregaveis/delivery-api-rabay.postman_collection.json`
- Importe no Postman ou Insomnia para testar todos os endpoints REST.

## 2. Screenshots dos Testes

- Realize chamadas para cada endpoint usando a collection.
- Capture screenshots das respostas de sucesso e erro (exemplo: 405 para GET /pedidos).
- Salve as imagens em `entregaveis/screenshots/`.

## 3. Validação dos Dados no H2

- Acesse: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- JDBC URL: `jdbc:h2:mem:testdb`
- Usuário: `sa` (sem senha)
- Valide visualmente os dados populados pelo DataLoader.

## 4. Observações

- Todos os endpoints principais estão cobertos na collection.
- O DataLoader garante dados de exemplo para testes.
- As regras de negócio básicas foram validadas via API e H2.
- O escopo da atividade 4 foi rigorosamente seguido.

## 5. Como rodar e testar

1. Certifique-se que a aplicação está rodando:
 `nohup mvn spring-boot:run > springboot.log 2>&1 &`
2. Importe a collection no Postman/Insomnia.
3. Execute os testes e registre os resultados.
4. Acesse o H2 para validação visual.

---

Qualquer dúvida, consulte o README.md do projeto.
