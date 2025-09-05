---
post_title: "Scripts - utilitários de desenvolvimento"
author1: "Rabay"
post_slug: "scripts-readme"
microsoft_alias: "rabay"
featured_image: ""
categories: ["devops"]
tags: ["scripts","dev","ci","testing"]
ai_note: "no"
summary: "Documentação dos scripts úteis para executar localmente tarefas de build, testes e análise do projeto."
post_date: 2025-09-03
---

# Scripts (pasta scripts/)

Este documento descreve os scripts presentes em `scripts/`. Para cada script há uma breve descrição e exemplos de execução.

> Observação: scripts que alteram o sistema ou contêm comportamento local sensível podem estar listados no `.gitignore` e não são documentados aqui por padrão.

## docker.sh

- O que faz: constrói a imagem Docker do projeto e sobe os serviços com `docker compose`.
- Quando usar: para criar a imagem localmente e levantar os serviços do projeto.

Exemplos:

```bash
# construir a imagem e subir os serviços (script usa Docker BuildKit)
bash scripts/docker.sh

# alternativa: usar docker compose manualmente
docker compose up --build -d
```

## gh_act.sh

- O que faz: wrapper para executar GitHub Actions localmente utilizando a ferramenta [act](https://github.com/nektos/act).
- Principais recursos: seleção de workflow, job, evento JSON, carregamento de `.env`, suporte a segredos inline ou via arquivo, dry-run e modo debug.

Exemplos:

```bash
# listar workflows detectados
./scripts/gh_act.sh -l

# dry-run: mostra o comando que seria executado
./scripts/gh_act.sh -w .github/workflows/ci.yml -n

# executar um workflow específico com arquivo .env e arquivo de segredos
./scripts/gh_act.sh -w .github/workflows/ci.yml -E .env -S secrets.env
```

## jacoco_summary.py

- O que faz: gera um resumo em Markdown a partir do relatório `jacoco.xml` (tabela com percentuais por tipo).
- Quando usar: após executar os testes com cobertura para obter um resumo legível.

Variáveis de ambiente:

- `JACOCO_XML` (padrão: `target/site/jacoco/jacoco.xml`)
- `JACOCO_HTML` (padrão: `target/site/jacoco/index.html`)

Exemplo:

```bash
python3 scripts/jacoco_summary.py

# ou com variáveis customizadas
JACOCO_XML=target/site/jacoco/jacoco.xml JACOCO_HTML=target/site/jacoco/index.html python3 scripts/jacoco_summary.py
```

## newman.sh

- O que faz: executa as coleções Postman com `newman` usando os artefatos em `entregaveis/` e exporta um relatório JSON.
- Dependência: `newman` disponível no PATH (p.ex. `npm i -g newman` ou `npx newman`).

Exemplos:

```bash
# executar a suíte Postman definida no repositório
bash scripts/newman.sh

# executar com npx (sem instalação global)
npx newman run entregaveis/delivery-api-rabay.postman_collection.json -e entregaveis/delivery-api-rabay.postman_environment.json --reporters cli,json --reporter-json-export entregaveis/newman-result.json
```
