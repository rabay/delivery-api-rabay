#!/bin/bash
# Script de limpeza de disco para Linux
# Uso: bash scripts/limpeza_disco.sh

set -e

echo "[1/7] Limpando arquivos temporários do sistema..."
sudo rm -rf /tmp/* || true

echo "[2/7] Limpando cache do apt..."
sudo apt-get clean

echo "[3/7] Removendo pacotes e dependências não utilizados..."
sudo apt-get autoremove -y

echo "[4/7] Limpando logs antigos (mantendo últimos 7 dias)..."
sudo journalctl --vacuum-time=7d || true

echo "[5/7] Limpando cache do usuário..."
rm -rf ~/.cache/* || true

echo "[6/7] Limpando imagens, containers e volumes não utilizados do Docker..."
echo "Atenção: Esta etapa remove imagens, containers e volumes não utilizados."
read -p "Deseja continuar com a limpeza avançada do Docker? (s/n): " resp
echo
if [[ "$resp" =~ ^[sS]$ ]]; then
  docker system prune -a --volumes -f
else
  echo "Pulando limpeza avançada do Docker."
fi

echo "[7/7] Listando os 20 maiores arquivos/diretórios para análise manual:"
sudo du -ahx / | sort -rh | head -20

echo "\nLimpeza concluída. Revise os arquivos grandes listados acima para possível remoção manual."
