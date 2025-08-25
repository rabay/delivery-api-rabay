#!/usr/bin/env bash
# Script para iniciar a aplicação Spring Boot em background
# Local do projeto: assumido como o diretório onde o script é executado

set -euo pipefail

# Configurações padrão
JAR_PATH="target/delivery-api-0.0.1-SNAPSHOT.jar"
PID_FILE="/tmp/delivery-api.pid"
LOGPATH_FILE="/tmp/delivery-api.logpath"
DEFAULT_PORT=8080

usage() {
    echo "Uso: $0 [--port PORT] [--no-tail]"
    echo "  --port PORT    porta HTTP a usar (padrão: ${DEFAULT_PORT})"
    echo "  --no-tail       não exibir as últimas linhas do log após iniciar"
    exit 1
}

PORT=${DEFAULT_PORT}
TAIL_LOG=1

while [[ $# -gt 0 ]]; do
    case "$1" in
    --port)
        PORT="$2"
        shift 2
        ;;
    --no-tail)
        TAIL_LOG=0
        shift 1
        ;;
    -h | --help)
        usage
        ;;
    *)
        echo "Argumento inválido: $1"
        usage
        ;;
    esac
done

if [ ! -f "${JAR_PATH}" ]; then
    echo "Erro: JAR não encontrado em ${JAR_PATH}. Execute './mvnw package' antes." >&2
    exit 2
fi

# Checa se a porta está livre
echo "Verificando porta ${PORT}..."
if command -v lsof >/dev/null 2>&1; then
    BUSY_PIDS=$(lsof -t -iTCP:${PORT} -sTCP:LISTEN -Pn 2>/dev/null || true)
else
    BUSY_PIDS=$(ss -ltnp 2>/dev/null | awk '/:'"${PORT}"' /{ if(match($0,/pid=([0-9]+)/,a)) print a[1] }' || true)
fi

if [ -n "${BUSY_PIDS// /}" ]; then
    echo "Erro: porta ${PORT} já está em uso pelos PID(s): ${BUSY_PIDS}. Pare-os primeiro ou use --port diferente." >&2
    exit 3
fi

# Cria arquivo de log temporário e inicia a aplicação
LOGFILE=$(mktemp /tmp/delivery-api.XXXXXX.log)
echo "Log file: ${LOGFILE}"
echo "${LOGFILE}" >"${LOGPATH_FILE}"

echo "Iniciando ${JAR_PATH} na porta ${PORT} (background)..."
nohup java -jar "${JAR_PATH}" --server.port=${PORT} >"${LOGFILE}" 2>&1 &
NEW_PID=$!
echo ${NEW_PID} >"${PID_FILE}"
echo "PID salvo em ${PID_FILE}: ${NEW_PID}"

sleep 1

echo "Verificando processo iniciado..."
if ps -p ${NEW_PID} >/dev/null 2>&1; then
    echo "Aplicação iniciada (PID: ${NEW_PID})."
else
    echo "Falha ao iniciar a aplicação. Verifique o log: ${LOGFILE}" >&2
    exit 4
fi

if [ "${TAIL_LOG}" -eq 1 ]; then
    echo "Mostrando últimas 200 linhas do log (${LOGFILE}):"
    tail -n 200 "${LOGFILE}"
    echo "Para seguir o log use: tail -f ${LOGFILE}"
fi

echo "Pronto."
