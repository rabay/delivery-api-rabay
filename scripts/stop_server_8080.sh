#!/usr/bin/env bash
# Encerra processos que estão escutando na porta 80
# Nome do arquivo pedido: stop_server_8080.sh (opera na porta 80 conforme instrução)

set -uo pipefail

PORT=8080
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
