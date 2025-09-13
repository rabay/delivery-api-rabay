# CI/CD: Build, Testes e Coverage (GitHub Actions)

Este documento descreve o workflow de CI configurado em `.github/workflows/build.yml`. Explica cada etapa do pipeline, como executá-lo manualmente e como acompanhar a execução no portal do GitHub.

## Objetivo
O workflow realiza build, execução de testes unitários com Maven, coleta de cobertura via JaCoCo e validação do build da imagem Docker (apenas local/runner, sem push).

## Quando o workflow é disparado
- On push para a branch `main` quando arquivos em `src/**`, `pom.xml` ou o próprio workflow forem alterados.
- On pull_request para a branch `main` quando os mesmos arquivos acima forem alterados.

## Jobs
O workflow define dois jobs:

1. `build` (obrigatório) — compila, testa e gera relatório de cobertura.
2. `docker` (dependente de `build`) — faz build da imagem Docker apenas para validação.

---

## Detalhamento do job `build`

- runs-on: `ubuntu-latest` — runner padrão do GitHub Actions.

Etapas:

1. Checkout código
   - Ação: `actions/checkout@v4`
   - Objetivo: clonar o repositório no runner.

2. Configurar Java 21
   - Ação: `actions/setup-java@v4`
   - Parâmetros: `distribution: temurin`, `java-version: 21`
   - Objetivo: instalar JDK 21 para garantir compatibilidade com o projeto.

3. Permitir execução do Maven Wrapper
   - Comando: `chmod +x ./mvnw`
   - Objetivo: garantir que o wrapper Maven possa ser executado no runner.

4. Build, testes unitários e cobertura
   - Comando: `./mvnw clean verify`
   - Objetivo: compilar o projeto e executar os testes. O `verify` normalmente executa o ciclo de testes e plugins de cobertura (JaCoCo) configurados no `pom.xml`.

5. Publicar relatório de cobertura Jacoco
   - Ação: `actions/upload-artifact@v4`
   - Parâmetros: `name: jacoco-coverage-report`, `path: target/site/jacoco`
   - Objetivo: subir o relatório de cobertura como artefato do workflow para download e análise.

6. Setup Python
   - Ação: `actions/setup-python@v5`
   - Parâmetros: `python-version: '3.x'`
   - Objetivo: permitir execução de scripts Python auxiliares (ex.: geração de summary).

7. Gerar summary do Jacoco
   - Comando:
     python scripts/jacoco_summary.py > jacoco-summary.md
     cat jacoco-summary.md >> $GITHUB_STEP_SUMMARY
   - Variáveis de ambiente usadas:
     - `JACOCO_XML: target/site/jacoco/jacoco.xml`
     - `JACOCO_HTML: target/site/jacoco/index.html`
   - Objetivo: gerar um resumo em Markdown da cobertura e anexá-lo ao summary do passo no GitHub Actions (visível no portal).

---

## Detalhamento do job `docker`

- runs-on: `ubuntu-latest`
- depends on: `build` (executa somente se `build` for bem-sucedido)

Etapas:

1. Checkout código — `actions/checkout@v4`
2. Build da imagem Docker — `docker/build-push-action@v5` com `push: false` e `tags: delivery-api-rabay:latest`
   - Objetivo: validar que a imagem consegue ser construída no runner; NÃO faz push para nenhum registro.

---

## Como executar manualmente pelo GitHub (disparar o workflow)

1. Vá ao repositório no GitHub.
2. Clique em **Actions** no topo.
3. Selecione o workflow "Build, Testes e Coverage Java Maven" na lista.
4. Clique em **Run workflow** (botão no canto direito) e selecione a branch.

Observação: se o repositório não tiver a permissão habilitada para executar workflows manualmente, o botão pode não aparecer. Nesse caso, faça um push de uma branch com um pequeno commit ou crie um pull request.

---

## Como acompanhar a execução no portal do GitHub

1. Abra **Actions** e selecione a execução desejada na lista.
2. Na página da execução, você verá os jobs (build, docker) com status e tempo.
3. Clique em um job para abrir os passos e visualizar logs linha a linha.
4. Na aba **Artifacts** (lado direito) você poderá baixar o `jacoco-coverage-report` gerado.
5. O summary gerado pelo script Python é anexado ao resumo do passo e aparece no topo da execução (procure por "Job summary").

---

## Notas e recomendações

- O workflow não publica artefatos binários (JAR/WAR) nem envia imagens Docker para registries — alterar isso exige adicionar steps de build/push com credenciais seguras (secrets).
- Verifique se o `pom.xml` contém a configuração do plugin JaCoCo para gerar os relatórios esperados.
- Se quiser publicar a imagem Docker, adicione um job extra que faça login no Docker Hub/GHCR usando secrets (`DOCKER_USERNAME`, `DOCKER_PASSWORD` ou `GITHUB_TOKEN`) e `push: true`.
- Para monitorar falhas recorrentes, configure notificações (Slack/Teams) usando actions apropriadas.

---

## Comandos locais úteis

Rodar build e testes localmente (Linux/macOS):

```bash
./mvnw clean verify
```

Fazer build da imagem Docker localmente (mesma configuração do workflow):

```bash
docker build -t delivery-api-rabay:latest .
```

---

## Referências
- GitHub Actions: https://docs.github.com/pt/actions
- JaCoCo: https://www.jacoco.org/jacoco/trunk/doc/
- Docker Build Action: https://github.com/docker/build-push-action

---

Arquivo gerado automaticamente pelo agente de suporte em 2025-09-13.