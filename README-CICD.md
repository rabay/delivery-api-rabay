# Guia de CI/CD – Build, Testes e Coverage com GitHub Actions

Este documento explica o funcionamento do pipeline de integração contínua (CI/CD) configurado no arquivo `.github/workflows/build.yml` deste projeto. O objetivo é detalhar cada etapa do workflow, como executar manualmente e como acompanhar o progresso e resultados pelo portal do GitHub.

---

## Visão Geral do Pipeline

O pipeline é executado automaticamente em dois cenários:

- **Push** para a branch `main` (alterações em `src/**`, `pom.xml` ou no próprio workflow)
- **Pull Request** para a branch `main` (mesmos arquivos acima)

O workflow é dividido em dois jobs principais:

- **build**: Compila, testa, gera relatório de cobertura Jacoco e publica o summary.
- **docker**: Valida o build da imagem Docker (não faz push nem upload).

---

## Etapas do Job `build`

1. **Checkout do código**
   - Baixa o código do repositório para o runner.

2. **Configuração do Java 21**
   - Instala o JDK Temurin 21 para garantir compatibilidade.

3. **Permissão de execução do Maven Wrapper**
   - Torna o script `mvnw` executável.

4. **Build, Testes Unitários e Cobertura**
   - Executa `./mvnw clean verify` para compilar, rodar testes e gerar relatório Jacoco.

5. **Publicação do Relatório Jacoco**
   - Salva o relatório de cobertura (`target/site/jacoco`) como artefato do workflow.

6. **Setup do Python**
   - Instala Python 3.x para rodar scripts auxiliares.

7. **Geração do Summary Jacoco**
   - Executa `scripts/jacoco_summary.py` para gerar resumo da cobertura e publica no summary do GitHub Actions.

---

## Etapas do Job `docker`

1. **Checkout do código**
   - Repete o checkout para garantir contexto atualizado.

2. **Build da imagem Docker**
   - Executa o build da imagem Docker localmente para validação (não faz push nem upload).

---

## Como Executar Manualmente

O pipeline é disparado automaticamente por push ou pull request na branch `main`. Para executar manualmente:

1. Acesse o repositório no GitHub.
2. Clique em **Actions** no menu superior.
3. Selecione o workflow "Build, Testes e Coverage Java Maven".
4. Clique em **Run workflow** (canto direito) e escolha a branch desejada.

---

## Como Acompanhar a Execução no Portal do GitHub

1. Acesse o repositório e clique em **Actions**.
2. Localize o workflow "Build, Testes e Coverage Java Maven" na lista.
3. Clique no workflow para ver o histórico de execuções.
4. Clique em uma execução específica para ver detalhes:
   - **Resumo**: Status geral, tempo de execução, branch, commit.
   - **Jobs**: Veja logs detalhados de cada etapa (build, docker).
   - **Artifacts**: Baixe o relatório de cobertura Jacoco.
   - **Summary**: Visualize o resumo da cobertura gerado pelo script Python.

---

## Observações Importantes

- Nenhum binário (JAR/WAR) ou imagem Docker é publicado como artefato.
- Apenas o relatório de cobertura Jacoco é salvo.
- O build da imagem Docker serve apenas para validação local.
- O workflow cobre build, testes, cobertura e validação Docker, mas não faz deploy.

---

## Referências
- [Documentação GitHub Actions](https://docs.github.com/pt/actions)
- [Jacoco – Cobertura de Testes Java](https://www.jacoco.org/jacoco/trunk/doc/)
- [Maven Wrapper](https://github.com/takari/maven-wrapper)

---

Dúvidas ou sugestões? Abra uma issue no repositório ou consulte os arquivos de configuração para mais detalhes.
