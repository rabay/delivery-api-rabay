#!/usr/bin/env bash
# gh_act.sh - wrapper para executar GitHub Actions localmente com act
# Boas práticas: fail-fast, carregamento de .env, proteção de segredos, help, e exemplos.

set -euo pipefail
IFS=$'\n\t'

SCRIPT_NAME="$(basename "$0")"
REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
DEFAULT_WORKFLOW_DIR="$REPO_ROOT/.github/workflows"

usage() {
	cat <<-EOF
		Uso: $SCRIPT_NAME [opções]

		Opções:
		  -w PATH    Arquivo de workflow (padrão: detecta .github/workflows/*)
		  -e PATH    Evento JSON para o act (ex: .github/event.json)
		  -j NAME    Executa apenas o job NAME
		  -s KEY=VAL Define segredo para o run (pode repetir)
		  -S FILE    Arquivo com segredos no formato KEY=VAL por linha (sensible: não comitar)
		  -E FILE    Arquivo .env a ser carregado (padrão: .env se existir)
		  -p PLATFORM Mapea runtimes de imagens docker (ex: ubuntu-latest=nektos/act-environments-ubuntu:18.04)
		  -n         Dry-run (mostra o comando que será executado)
		  -d         Ativa modo debug (set -x)
		  -l         Lista workflows encontrados
		  -h         Mostra este help

		Exemplos:
		  $SCRIPT_NAME -w .github/workflows/ci.yml -E .env -S secrets.env
		  $SCRIPT_NAME -l

		Notas de segurança:
		  - Nunca comite arquivos de segredos.
		  - Para executar em CI real, prefira GitHub Actions remotos com secrets do repositório.
	EOF
}

# Check command exists
_check_cmd() {
	command -v "$1" >/dev/null 2>&1 || return 1
}

# Carrega arquivo .env simples (KEY=VAL) sem exportar linhas vazias ou comentários
_load_env_file() {
	local file="$1"
	[ -f "$file" ] || return 0
	echo "Carregando variáveis de ambiente de $file"
	set -o allexport
	# shellcheck disable=SC1090
	# read only lines with = and ignoring comments
	while IFS='=' read -r key val; do
		[[ "$key" =~ ^# ]] && continue
		[[ -z "$key" ]] && continue
		# remove possible surrounding quotes
		val="$(echo "$val" | sed -e 's/^\"//' -e 's/\"$//' -e "s/^'//" -e "s/'$//")"
		export "$key"="$val"
	done < <(grep -E "^[A-Za-z_][A-Za-z0-9_]*=" "$file" || true)
	set +o allexport
}

# Build act command args
_build_act_cmd() {
	local args=("act")
	# prefer --workflows if explicit workflow provided
	if [ -n "${WORKFLOW:-}" ]; then
		args+=("-W" "$WORKFLOW")
	fi
	if [ -n "${EVENT_FILE:-}" ]; then
		args+=("-e" "$EVENT_FILE")
	fi
	if [ -n "${JOB_NAME:-}" ]; then
		args+=("-j" "$JOB_NAME")
	fi
	# secrets from inline
	for s in "${SECRETS_INLINE[@]:-}"; do
		args+=("-s" "$s")
	done
	# secrets from file
	if [ -n "${SECRETS_FILE:-}" ] && [ -f "$SECRETS_FILE" ]; then
		while IFS= read -r line; do
			[[ "$line" =~ ^# ]] && continue
			[[ -z "$line" ]] && continue
			args+=("-s" "$line")
		done < <(grep -E "^[A-Za-z_][A-Za-z0-9_]*=" "$SECRETS_FILE" || true)
	fi
	# platform mappings
	for p in "${PLATFORMS[@]:-}"; do
		args+=("-P" "$p")
	done
	echo "${args[@]}"
}

# Main
WORKFLOW=""
EVENT_FILE=""
JOB_NAME=""
SECRETS_INLINE=()
SECRETS_FILE=""
ENV_FILE=""
PLATFORMS=()
DRY_RUN=false
DEBUG=false

while getopts ":w:e:j:s:S:E:p:ndlh" opt; do
	case $opt in
	w) WORKFLOW="$OPTARG" ;;
	e) EVENT_FILE="$OPTARG" ;;
	j) JOB_NAME="$OPTARG" ;;
	s) SECRETS_INLINE+=("$OPTARG") ;;
	S) SECRETS_FILE="$OPTARG" ;;
	E) ENV_FILE="$OPTARG" ;;
	p) PLATFORMS+=("$OPTARG") ;;
	n) DRY_RUN=true ;;
	d) DEBUG=true ;;
	l)
		echo "Workflows encontrados em $DEFAULT_WORKFLOW_DIR:"
		ls -1 "$DEFAULT_WORKFLOW_DIR"/*.yml 2>/dev/null || ls -1 "$DEFAULT_WORKFLOW_DIR"/*.yaml 2>/dev/null || echo "Nenhum workflow encontrado"
		exit 0
		;;
	h)
		usage
		exit 0
		;;
	:)
		echo "Erro: opção -$OPTARG requer um argumento"
		usage
		exit 2
		;;
	\?)
		echo "Opção inválida: -$OPTARG"
		usage
		exit 2
		;;
	esac
done

# Ativa debug se pedido
${DEBUG:+set -x}

# Carrega .env padrão se existir e se não foi passado -E
if [ -z "${ENV_FILE}" ] && [ -f "$REPO_ROOT/.env" ]; then
	ENV_FILE="$REPO_ROOT/.env"
fi
[ -n "${ENV_FILE}" ] && _load_env_file "$ENV_FILE"

# Valida dependência 'act'
if ! _check_cmd act; then
	echo "Erro: 'act' não encontrado. Instale via 'brew install act' (mac) ou siga https://github.com/nektos/act#installation" >&2
	exit 3
fi

# Se não foi informado workflow, tenta detectar o primeiro
if [ -z "$WORKFLOW" ]; then
	# pick first yaml in workflows dir
	WORKFLOW_CANDIDATE=("$DEFAULT_WORKFLOW_DIR"/*.yml)
	if [ -f "${WORKFLOW_CANDIDATE[0]}" ]; then
		WORKFLOW="${WORKFLOW_CANDIDATE[0]}"
	else
		# try yaml
		WORKFLOW_CANDIDATE=("$DEFAULT_WORKFLOW_DIR"/*.yaml)
		if [ -f "${WORKFLOW_CANDIDATE[0]}" ]; then
			WORKFLOW="${WORKFLOW_CANDIDATE[0]}"
		fi
	fi
fi

if [ -z "$WORKFLOW" ]; then
	echo "Nenhum workflow especificado e nenhum arquivo encontrado em $DEFAULT_WORKFLOW_DIR" >&2
	exit 4
fi

# Monta o comando act
ACT_CMD_STR="$(_build_act_cmd)"

if [ "$DRY_RUN" = true ]; then
	echo "Dry-run: comando que seria executado:"
	echo "$ACT_CMD_STR"
	exit 0
fi

# Exec
echo "Executando workflow localmente: $WORKFLOW"
# Use eval pois _build_act_cmd devolve a linha completa
eval "$ACT_CMD_STR"

# Fim
exit 0
