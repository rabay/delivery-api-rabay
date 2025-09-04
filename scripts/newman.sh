#!/usr/bin/env bash
# Wrapper para executar Newman com boas práticas e mensagens em pt-BR
# - set -euo pipefail: encerra rápido em erros, trata variáveis não definidas como erro e faz pipelines falharem ao primeiro erro
# - exibe timestamps e retorna o código de saída do Newman

set -euo pipefail
IFS=$'\n\t'

# Caminhos configuráveis (relativos à raiz do repositório)
COLLECTION="entregaveis/delivery-api-rabay.postman_collection.json"
ENVIRONMENT="entregaveis/delivery-api-rabay.postman_environment.json"
REPORT_JSON="entregaveis/newman-result.json"
REPORT_HTML="entregaveis/newman-report.html"

# Opções padrão do Newman (ajuste conforme necessário)
DELAY_REQUEST=250
REPORTERS="cli,json,html"

# Função auxiliar: imprime mensagem com timestamp
log() { printf '\n[%s] %s\n' "$(date --iso-8601=seconds)" "$*"; }

# Valida dependências
if ! command -v newman >/dev/null 2>&1; then
	log "ERRO: newman não está instalado ou não está no PATH. Instale com 'npm i -g newman' ou use 'npx newman'." >&2
	exit 2
fi

# Valida existência dos arquivos necessários
for f in "$COLLECTION" "$ENVIRONMENT"; do
	if [ ! -f "$f" ]; then
		log "ERRO: arquivo necessário não encontrado: $f" >&2
		exit 3
	fi
done

log "Iniciando execução do Newman"
log "colecao=$COLLECTION ambiente=$ENVIRONMENT reportadores=$REPORTERS relatorio_json=$REPORT_JSON relatorio_html=$REPORT_HTML"

# Garante que o diretório de saída exista
mkdir -p "$(dirname "$REPORT_JSON")"
mkdir -p "$(dirname "$REPORT_HTML")"

# Executa o Newman e captura o código de saída para propagação adequada
if newman run "$COLLECTION" \
		-e "$ENVIRONMENT" \
		--delay-request "$DELAY_REQUEST" \
		--reporters "$REPORTERS" \
		--reporter-json-export "$REPORT_JSON" \
		--reporter-html-export "$REPORT_HTML"; then
	EXIT_CODE=0
else
	EXIT_CODE=$?
fi

log "Newman finalizado com código de saída $EXIT_CODE"
log "Relatórios gerados:"
log "  - JSON: $REPORT_JSON"
log "  - HTML: $REPORT_HTML"

exit $EXIT_CODE