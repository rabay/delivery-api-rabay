#!/usr/bin/env bash
# Encerra processos que estão escutando na porta especificada (padrão 8081)

set -uo pipefail

# Porta padrão (mantida para compatibilidade)
DEFAULT_PORT=8080
PORT=${DEFAULT_PORT}
PIDS_FILE=/tmp/stop_server_${PORT}.pids

usage() {
    echo "Uso: $0 [--port PORT]"
    echo "  --port, -p    Porta que o servidor está escutando (padrão: ${DEFAULT_PORT})"
}

# Parse de argumentos simples: --port PORT, --port=PORT ou -p PORT
while [[ $# -gt 0 ]]; do
    case "$1" in
        --port|-p)
            if [[ -n "${2:-}" && ! "$2" =~ ^- ]]; then
                PORT="$2"
                shift 2
            else
                echo "ERRO: argumento para $1 ausente" >&2
                usage
                exit 2
            fi
            ;;
        --port=*)
            PORT="${1#*=}"
            shift
            ;;
        -h|--help)
            usage
            exit 0
            ;;
        *)
            echo "ERRO: argumento desconhecido: $1" >&2
            usage
            exit 2
            ;;
    esac
done

# Validação básica da porta
if ! [[ "$PORT" =~ ^[0-9]+$ ]]; then
    echo "Porta inválida: $PORT" >&2
    exit 2
fi
if [ "$PORT" -lt 1 ] || [ "$PORT" -gt 65535 ]; then
    echo "Porta fora do intervalo (1-65535): $PORT" >&2
    exit 2
fi

# atualizar caminho do arquivo de pids com a porta final
PIDS_FILE=/tmp/stop_server_${PORT}.pids

echo "Procurando processos escutando na porta ${PORT}..."

PIDS=""
if command -v lsof >/dev/null 2>&1; then
    PIDS=$(lsof -t -iTCP:${PORT} -sTCP:LISTEN -Pn 2>/dev/null || true)
else
    # Fallback para ss/awk parsing
    PIDS=$(ss -ltnp 2>/dev/null | awk '/:'"${PORT}"' /{ if(match($0,/pid=([0-9]+)/,a)) print a[1] }' || true)
fi

if [ -z "${PIDS// /}" ]; then
    echo "Nenhum processo encontrado escutando na porta ${PORT}. Nada a fazer."
    exit 0
fi

echo "PID(s) encontrados: ${PIDS}"
echo "${PIDS}" >"${PIDS_FILE}"
echo "PID(s) gravados em ${PIDS_FILE}"

if command -v lsof >/dev/null 2>&1; then
    echo "Detalhes dos processos (lsof):"
    lsof -iTCP:${PORT} -sTCP:LISTEN -Pn || true
fi

for pid in ${PIDS}; do
    if [ -z "$pid" ]; then
        continue
    fi
    echo "Enviando SIGTERM para PID $pid..."
    kill "$pid" 2>/dev/null || true
done

# Aguarda até 5 segundos para encerramento gracioso
for i in {1..5}; do
    sleep 1
    ALIVE=""
    for pid in ${PIDS}; do
        if ps -p "$pid" >/dev/null 2>&1; then
            ALIVE=1
            break
        fi
    done
    if [ -z "$ALIVE" ]; then
        echo "Todos os processos encerraram graciosamente."
        exit 0
    fi
    echo "Aguardando encerramento... (${i}/5)"
done

echo "Alguns processos ainda estão vivos; aplicando SIGKILL..."
for pid in ${PIDS}; do
    if ps -p "$pid" >/dev/null 2>&1; then
        echo "Enviando SIGKILL para PID $pid"
        kill -9 "$pid" 2>/dev/null || true
    fi
done

# Verifica resultado final
sleep 1
REMAINING=""
for pid in ${PIDS}; do
    if ps -p "$pid" >/dev/null 2>&1; then
        REMAINING="$REMAINING $pid"
    fi
done

if [ -z "${REMAINING// /}" ]; then
    echo "Todos os processos na porta ${PORT} foram encerrados."
    exit 0
else
    echo "Falha ao encerrar PIDs:${REMAINING}. Talvez privilégio insuficiente (use sudo)."
    exit 1
fi
