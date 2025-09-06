#!/usr/bin/env bash
## checkstyle.sh - baixar e executar Checkstyle para este repositório
#
# Local: scripts/checkstyle.sh
# Objetivo: baixar o JAR do Checkstyle (downloads/) e executar a verificação
# Uso: bash scripts/checkstyle.sh [opções] [-- <paths>...]
#
# Boas práticas: set -euo pipefail, tratamento de erros, mensagens claras,
# help em português.

set -euo pipefail
IFS=$'\n\t'

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DOWNLOAD_DIR="$SCRIPT_DIR/downloads"
REPORT_DIR="$SCRIPT_DIR/reports"

DEFAULT_VERSION="11.0.1"
DEFAULT_URL_BASE="https://github.com/checkstyle/checkstyle/releases/download"
DEFAULT_CONFIG="/google_checks.xml"
DEFAULT_TARGETS=("src/main/java" "src/test/java")

usage() {
  cat <<-EOF
Uso: $0 [opções] [-- <paths>...]

Opções:
  -v, --version <versão>      Versão do Checkstyle (default: ${DEFAULT_VERSION})
  -d, --download-only         Apenas baixar o JAR e sair
  -c, --config <arquivo>      Arquivo de configuração Checkstyle (default: ${DEFAULT_CONFIG})
  -f, --format <formato>      Formato de saída (plain, xml, sarif) (default: plain)
  -o, --output <arquivo>      Arquivo de saída para os resultados
  -p, --properties <arquivo>  Arquivo de propriedades a passar com -p
  -e, --exclude <path>        Excluir caminho (pode repetir)
  -x, --exclude-regexp <re>   Excluir por regex (pode repetir)
  -h, --help                  Mostrar esta ajuda

Exemplos:
  # Baixar e rodar com config default (google)
  bash scripts/checkstyle.sh

  # Baixar versão 11.0.1 explicitamente e rodar em src
  bash scripts/checkstyle.sh -v 11.0.1 -- src/main/java

  # Apenas baixar o JAR
  bash scripts/checkstyle.sh -d

Observações:
  - O JAR será salvo em: ${DOWNLOAD_DIR}
  - Por padrão o script verifica as pastas: ${DEFAULT_TARGETS[*]}
  - Se não houver java instalado, o script aborta.
EOF
}

log() { printf '%s\n' "$*" >&2; }
error() { log "ERRO: $*"; exit 1; }

ensure_java() {
  if ! command -v java >/dev/null 2>&1; then
    error "Java não encontrado. Instale o JRE/JDK (java) antes de executar." 
  fi
}

download_jar() {
  local version="$1"
  local jar_name="checkstyle-${version}-all.jar"
  local url="${DEFAULT_URL_BASE}/checkstyle-${version}/${jar_name}"
  mkdir -p "$DOWNLOAD_DIR"
  local dest="$DOWNLOAD_DIR/$jar_name"

  if [[ -f "$dest" ]]; then
    log "JAR já existe: $dest"
    return 0
  fi

  log "Baixando Checkstyle ${version} para: $dest"
  # tenta curl, se não disponível usa wget
  if command -v curl >/dev/null 2>&1; then
    curl -fSL --retry 3 --output "$dest" "$url" || {
      rm -f "$dest" || true
      error "Falha ao baixar $url"
    }
  elif command -v wget >/dev/null 2>&1; then
    wget -O "$dest" "$url" || {
      rm -f "$dest" || true
      error "Falha ao baixar $url"
    }
  else
    error "Nem curl nem wget disponíveis para baixar o JAR. Instale um deles."
  fi

  log "Download concluído: $dest"
}

run_checkstyle() {
  local version="$1"
  shift
  local jar_path="$DOWNLOAD_DIR/checkstyle-${version}-all.jar"

  if [[ ! -f "$jar_path" ]]; then
    error "JAR não encontrado em $jar_path. Rode com --download-only ou verifique o download."
  fi

  local java_cmd=(java -jar "$jar_path")
  # passar argumentos extras já formatados
  log "Executando Checkstyle (jar: $jar_path)"
  "${java_cmd[@]}" "$@"
}

# --- parse args ---
VERSION="$DEFAULT_VERSION"
DOWNLOAD_ONLY=false
CONFIG_FILE="$DEFAULT_CONFIG"
FORMAT="plain"
OUTPUT_PATH=""
PROPERTIES_FILE=""
EXCLUDES=()
EXCLUDE_REGEXS=()

ARGS=()
while [[ $# -gt 0 ]]; do
  case "$1" in
    -v|--version)
      VERSION="$2"; shift 2;;
    -d|--download-only)
      DOWNLOAD_ONLY=true; shift;;
    -c|--config)
      CONFIG_FILE="$2"; shift 2;;
    -f|--format)
      FORMAT="$2"; shift 2;;
    -o|--output)
      OUTPUT_PATH="$2"; shift 2;;
    -p|--properties)
      PROPERTIES_FILE="$2"; shift 2;;
    -e|--exclude)
      EXCLUDES+=("$2"); shift 2;;
    -x|--exclude-regexp)
      EXCLUDE_REGEXS+=("$2"); shift 2;;
    -h|--help)
      usage; exit 0;;
    --)
      shift; ARGS+=("$@"); break;;
    --* )
      error "Opção desconhecida: $1";;
    *)
      ARGS+=("$1"); shift;;
  esac
done

# Se paths foram passados pos-args, usam-se eles; caso contrário, usa DEFAULT_TARGETS
if [[ "${#ARGS[@]}" -eq 0 ]]; then
  TARGETS=("${DEFAULT_TARGETS[@]}")
else
  TARGETS=("${ARGS[@]}")
fi

# Construir options CLI
CLI_OPTS=()
CLI_OPTS+=( -c "$CONFIG_FILE" )
CLI_OPTS+=( -f "$FORMAT" )
if [[ -n "$PROPERTIES_FILE" ]]; then
  CLI_OPTS+=( -p "$PROPERTIES_FILE" )
fi
if [[ -n "$OUTPUT_PATH" ]]; then
  CLI_OPTS+=( -o "$OUTPUT_PATH" )
fi
for ex in "${EXCLUDES[@]:-}"; do
  if [[ -n "$ex" ]]; then
    CLI_OPTS+=( -e "$ex" )
  fi
done
for rex in "${EXCLUDE_REGEXS[@]:-}"; do
  if [[ -n "$rex" ]]; then
    CLI_OPTS+=( -x "$rex" )
  fi
done

# final targets (quote-safe)
TARGETS_ARGS=()
for t in "${TARGETS[@]}"; do
  TARGETS_ARGS+=( "$t" )
done

# --- execução ---
ensure_java
download_jar "$VERSION"

if [[ "$DOWNLOAD_ONLY" == true ]]; then
  log "Download realizado. JAR salvo em: $DOWNLOAD_DIR"
  exit 0
fi

# montar comando final: java -jar jar -c config -f format [opts] targets...
FINAL_ARGS=()
FINAL_ARGS+=( -c "$CONFIG_FILE" )
FINAL_ARGS+=( -f "$FORMAT" )
if [[ -n "$PROPERTIES_FILE" ]]; then
  FINAL_ARGS+=( -p "$PROPERTIES_FILE" )
fi
if [[ -n "$OUTPUT_PATH" ]]; then
  FINAL_ARGS+=( -o "$OUTPUT_PATH" )
fi
for ex in "${EXCLUDES[@]:-}"; do
  if [[ -n "$ex" ]]; then
    FINAL_ARGS+=( -e "$ex" )
  fi
done
for rex in "${EXCLUDE_REGEXS[@]:-}"; do
  if [[ -n "$rex" ]]; then
    FINAL_ARGS+=( -x "$rex" )
  fi
done

# acrescenta os alvos
for t in "${TARGETS_ARGS[@]}"; do
  FINAL_ARGS+=( "$t" )
done

# Executa e gera relatórios em XML + SARIF(JSON) e um HTML simples
mkdir -p "$REPORT_DIR"

# arquivos de saída
XML_REPORT="$REPORT_DIR/checkstyle-report.xml"
JSON_REPORT="$REPORT_DIR/checkstyle-report.json"
HTML_REPORT="$REPORT_DIR/checkstyle-report.html"

# garantir que o comando checkstyle não quebre o script devido ao set -e
set +e
# preparar FINAL_ARGS limpos: remover quaisquer -f <val> e -o <val> que já existam
CLEAN_ARGS=()
skip_next=false
for a in "${FINAL_ARGS[@]}"; do
  if [[ "$skip_next" == true ]]; then
    skip_next=false
    continue
  fi
  if [[ "$a" == "-f" || "$a" == "-o" ]]; then
    skip_next=true
    continue
  fi
  CLEAN_ARGS+=("$a")
done

# XML
run_checkstyle "$VERSION" -f xml -o "$XML_REPORT" "${CLEAN_ARGS[@]}"
# SARIF (JSON)
run_checkstyle "$VERSION" -f sarif -o "$JSON_REPORT" "${CLEAN_ARGS[@]}"
set -e

# gerar HTML simples a partir do XML (escapar conteúdo)
if [[ -f "$XML_REPORT" ]]; then
  # tentar formatar o XML para melhor legibilidade
  if command -v xmllint >/dev/null 2>&1; then
    xmllint --format "$XML_REPORT" -o "$XML_REPORT.tmp" 2>/dev/null || cp "$XML_REPORT" "$XML_REPORT.tmp"
  else
    cp "$XML_REPORT" "$XML_REPORT.tmp"
  fi
  # escapar para HTML
  awk 'BEGIN{print "<html><head><meta charset=\"utf-8\"><title>Checkstyle Report</title></head><body><h1>Checkstyle Report</h1><pre>"} {gsub("&","&amp;"); gsub("<","&lt;"); gsub(">","&gt;"); print} END{print "</pre></body></html>"}' "$XML_REPORT.tmp" > "$HTML_REPORT"
  rm -f "$XML_REPORT.tmp"
fi

# determinar sucesso: relatórios gerados (existem e não estão vazios)
success=true
if [[ ! -s "$XML_REPORT" ]]; then success=false; fi
if [[ ! -s "$JSON_REPORT" ]]; then success=false; fi

# Imprimir somente status curto em STDOUT (logs continuam em stderr)
if [[ "$success" == true ]]; then
  # antes de sair, mostrar resumo do relatório
  print_checkstyle_summary() {
    local xml="$1"
    if [[ ! -f "$xml" ]]; then
      echo "(sem relatório XML para resumir)"
      return
    fi

    # total de violações
    local total_errors
    total_errors=$(grep -o '<error ' "$xml" 2>/dev/null | wc -l | tr -d ' ')

    # arquivos com violações e contagem por arquivo
    local tmpfile
    tmpfile=$(mktemp)
    awk '
      /<file /{ cur=""; if (match($0,/name="/)) { tm=$0; sub(/.*name="/,"",tm); sub(/".*/,"",tm); cur=tm; next } }
      /<error /{ if (cur!="") print cur }
    ' "$xml" | sort | uniq -c | awk '{print $1 " " $2}' > "$tmpfile"

    local files_with
    files_with=0
    if [[ -s "$tmpfile" ]]; then
      files_with=$(wc -l < "$tmpfile" | tr -d ' ')
    fi

    echo "Checkstyle summary:" 
    echo "  Total violations: ${total_errors:-0}"
    echo "  Files with violations: ${files_with:-0}"

    # severities
    echo "  Violations by severity:"
  awk '/<error /{ s=$0; if (match(s,/severity="/)){ sub(/.*severity="/,"",s); sub(/".*/,"",s); print s } }' "$xml" 2>/dev/null | sort | uniq -c | awk '{printf "    %s: %s\n", $2, $1}' || echo "    (nenhuma)"

    # top 10 arquivos
    if [[ -s "$tmpfile" ]]; then
      echo "  Top files (mais violações):"
      sort -nr "$tmpfile" | head -n 10 | awk '{printf "    %d %s\n", $1, $2}'
    fi

    rm -f "$tmpfile"
  }

  print_checkstyle_summary "$XML_REPORT"
  printf 'SUCCESS\n'
  exit 0
else
  printf 'FAIL\n'
  # deixar logs mais detalhados em stderr
  log "Relatórios gerados em: $REPORT_DIR (XML: $XML_REPORT ; JSON: $JSON_REPORT ; HTML: $HTML_REPORT)"
  if [[ -f "$XML_REPORT" ]]; then
    log "Tamanho XML: $(stat -c%s "$XML_REPORT") bytes"
  fi
  if [[ -f "$JSON_REPORT" ]]; then
    log "Tamanho JSON: $(stat -c%s "$JSON_REPORT") bytes"
  fi
  exit 2
fi
