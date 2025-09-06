#!/usr/bin/env bash
## google-java-format.sh - baixar e executar google-java-format neste repositório
#
# Local: scripts/google-java-format.sh
# Objetivo: baixar o JAR do google-java-format (downloads/) e executar a formatação
# Uso: bash scripts/google-java-format.sh [opções] [-- <paths>...]
#
# Boas práticas: set -euo pipefail, tratamento de erros, mensagens claras,
# help em português.

set -euo pipefail
IFS=$'\n\t'

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DOWNLOAD_DIR="$SCRIPT_DIR/downloads"

DEFAULT_VERSION="1.28.0"
DEFAULT_JAR="google-java-format-${DEFAULT_VERSION}-all-deps.jar"
DEFAULT_URL_BASE="https://github.com/google/google-java-format/releases/download"
DEFAULT_TARGETS=("src/main/java" "src/test/java")

usage() {
  cat <<-EOF
Uso: $0 [opções] [-- <paths>...]

Opções:
  -v, --version <versão>        Versão do google-java-format (default: ${DEFAULT_VERSION})
  -d, --download-only           Apenas baixar o JAR e sair
  -r, --replace                 Aplicar alterações in-place (equivale a --replace)
  -s, --set-exit-if-changed     Sair com código !=0 se arquivos foram alterados
  -l, --lines <start:end>       Limitar a formatação a linhas (pode repetir)
  -h, --help                    Mostrar esta ajuda

Exemplos:
  # Baixar e executar em src
  bash scripts/google-java-format.sh

  # Apenas baixar o binário
  bash scripts/google-java-format.sh -d

Observações:
  - O JAR será salvo em: ${DOWNLOAD_DIR}/${DEFAULT_JAR}
  - Por padrão o script formata as pastas: ${DEFAULT_TARGETS[*]}
  - Requer JDK (não apenas JRE). google-java-format precisa de JDK >= Java 17 para a versão atual.
EOF
}

log() { printf '%s\n' "$*" >&2; }
error() { log "ERRO: $*"; exit 1; }

ensure_java() {
  if ! command -v java >/dev/null 2>&1; then
    error "Java não encontrado. Instale o JDK (java) antes de executar." 
  fi
}

download_jar() {
  local version="$1"
  local jar_name="google-java-format-${version}-all-deps.jar"
  local url="${DEFAULT_URL_BASE}/v${version}/${jar_name}"
  mkdir -p "$DOWNLOAD_DIR"
  local dest="$DOWNLOAD_DIR/$jar_name"

  if [[ -f "$dest" ]]; then
    log "JAR já existe: $dest"
    return 0
  fi

  log "Baixando google-java-format ${version} para: $dest"
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

run_formatter() {
  local version="$1"
  shift
  local jar_path="$DOWNLOAD_DIR/google-java-format-${version}-all-deps.jar"

  if [[ ! -f "$jar_path" ]]; then
    error "JAR não encontrado em $jar_path. Rode com --download-only ou verifique o download."
  fi

  local java_cmd=(java -jar "$jar_path")
  log "Executando google-java-format (jar: $jar_path)"
  "${java_cmd[@]}" "$@"
}

# parse args
VERSION="$DEFAULT_VERSION"
DOWNLOAD_ONLY=false
REPLACE=false
SET_EXIT_IF_CHANGED=false
LINES=()

ARGS=()
while [[ $# -gt 0 ]]; do
  case "$1" in
    -v|--version)
      VERSION="$2"; shift 2;;
    -d|--download-only)
      DOWNLOAD_ONLY=true; shift;;
    -r|--replace)
      REPLACE=true; shift;;
    -s|--set-exit-if-changed)
      SET_EXIT_IF_CHANGED=true; shift;;
    -l|--lines)
      LINES+=("$2"); shift 2;;
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

# targets padrão
if [[ "${#ARGS[@]}" -eq 0 ]]; then
  TARGETS=("${DEFAULT_TARGETS[@]}")
else
  TARGETS=("${ARGS[@]}")
fi

# compor args do google-java-format
GJF_ARGS=()
if [[ "$REPLACE" == true ]]; then
  GJF_ARGS+=( --replace )
fi
if [[ "$SET_EXIT_IF_CHANGED" == true ]]; then
  # opção suportada em google-java-format CLI a partir de versões recentes
  GJF_ARGS+=( --set-exit-if-changed )
fi
for l in "${LINES[@]}"; do
  if [[ -n "$l" ]]; then
    GJF_ARGS+=( --lines "$l" )
  fi
done

ensure_java
download_jar "$VERSION"

if [[ "$DOWNLOAD_ONLY" == true ]]; then
  log "Download realizado. JAR salvo em: $DOWNLOAD_DIR"
  exit 0
fi

# montar lista de arquivos/pastas para passar
TARGET_ARGS=()
for t in "${TARGETS[@]}"; do
  TARGET_ARGS+=( "$t" )
done

# expandir diretórios para arquivos .java recursivamente
FILE_ARGS=()
for t in "${TARGET_ARGS[@]}"; do
  if [[ -d "$t" ]]; then
    # adicionar todos os .java dentro do diretório
    while IFS= read -r -d '' f; do
      FILE_ARGS+=("$f")
    done < <(find "$t" -type f -name '*.java' -print0)
  elif [[ -f "$t" ]]; then
    FILE_ARGS+=("$t")
  else
    # tentar expansão de glob (ex.: src/**/*.java ou arquivos específicos)
    shopt -s nullglob
    found=false
    for m in $t; do
      if [[ -f "$m" ]]; then
        FILE_ARGS+=("$m")
        found=true
      fi
    done
    shopt -u nullglob
    if [[ "$found" != true ]]; then
      log "Aviso: alvo não encontrado (pular): $t"
    fi
  fi
done

if [[ ${#FILE_ARGS[@]} -eq 0 ]]; then
  error "Nenhum arquivo .java encontrado em: ${TARGETS[*]}"
fi

# executar: calcular checksums antes, executar e comparar após para detectar arquivos modificados
declare -A _before_sums
for f in "${FILE_ARGS[@]}"; do
  if [[ -f "$f" ]]; then
    if command -v sha1sum >/dev/null 2>&1; then
      _before_sums["$f"]=$(sha1sum "$f" | awk '{print $1}')
    elif command -v shasum >/dev/null 2>&1; then
      _before_sums["$f"]=$(shasum -a 1 "$f" | awk '{print $1}')
    else
      # sem utilitário de checksum; saltar detecção e só executar
      log "Aviso: sha1sum/shasum não disponível, pulando detecção de mudanças." >&2
      run_formatter "$VERSION" "${GJF_ARGS[@]}" "${FILE_ARGS[@]}"
      exit $?
    fi
  fi
done

run_formatter "$VERSION" "${GJF_ARGS[@]}" "${FILE_ARGS[@]}"
rc=$?

# comparar checksums e listar arquivos modificados
CHANGED=()
if [[ $rc -eq 0 ]]; then
  for f in "${FILE_ARGS[@]}"; do
    if [[ -f "$f" ]]; then
      if command -v sha1sum >/dev/null 2>&1; then
        newsum=$(sha1sum "$f" | awk '{print $1}')
      else
        newsum=$(shasum -a 1 "$f" | awk '{print $1}')
      fi
      if [[ "${_before_sums["$f"]}" != "$newsum" ]]; then
        CHANGED+=("$f")
      fi
    fi
  done

  if [[ ${#CHANGED[@]} -gt 0 ]]; then
    log "Arquivos formatados/alterados (${#CHANGED[@]}):"
    for cf in "${CHANGED[@]}"; do
      printf '  %s\n' "$cf"
    done
  else
    log "Nenhuma alteração aplicada pelo google-java-format."
  fi
else
  error "google-java-format retornou código $rc"
fi

exit $rc
