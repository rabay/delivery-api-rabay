#!/bin/bash

# Script para executar testes da Delivery Tech API com Newman
# Autor: Victor Rabay (TI 58A 02728)
# Curso: Arquitetura de Sistemas

echo "🚀 Delivery Tech API - Executor de Testes"
echo "=========================================="
echo ""

# Verificar se Newman está instalado
if ! command -v newman &> /dev/null; then
    echo "❌ Newman não está instalado. Instalando..."
    npm install -g newman
    if [ $? -ne 0 ]; then
        echo "❌ Erro ao instalar Newman. Verifique sua instalação do Node.js"
        exit 1
    fi
fi

# Verificar se a API está rodando
echo "🔍 Verificando se a API está rodando..."
curl -s http://localhost:8080/health > /dev/null
if [ $? -ne 0 ]; then
    echo "❌ API não está respondendo em http://localhost:8080"
    echo "   Certifique-se de que a aplicação está rodando"
    exit 1
fi

echo "✅ API está rodando corretamente!"
echo ""

# Função para executar teste básico
run_basic_tests() {
    echo "📋 Executando testes básicos..."
    newman run "Delivery-API-Collection.postman_collection.json" \
        --environment "Delivery-API-Local.postman_environment.json" \
        --reporters cli,html \
        --reporter-html-export "reports/basic-test-results-$(date +%Y%m%d_%H%M%S).html" \
        --delay-request 100
}

# Função para executar testes de carga
run_load_tests() {
    echo "⚡ Executando testes de carga..."
    newman run "Delivery-API-LoadTest.postman_collection.json" \
        --environment "Delivery-API-Local.postman_environment.json" \
        --iteration-count 50 \
        --delay-request 50 \
        --reporters cli,html \
        --reporter-html-export "reports/load-test-results-$(date +%Y%m%d_%H%M%S).html"
}

# Função para executar testes com dados CSV
run_data_driven_tests() {
    echo "📊 Executando testes com dados CSV..."
    newman run "Delivery-API-Collection.postman_collection.json" \
        --environment "Delivery-API-Local.postman_environment.json" \
        --data "test-data.csv" \
        --iteration-count 5 \
        --reporters cli,html \
        --reporter-html-export "reports/data-driven-test-results-$(date +%Y%m%d_%H%M%S).html"
}

# Criar diretório de relatórios se não existir
mkdir -p reports

# Menu de opções
echo "Escolha o tipo de teste a executar:"
echo "1) Testes Básicos (Coleção Principal)"
echo "2) Testes de Carga (Performance)"
echo "3) Testes com Dados CSV (Data-Driven)"
echo "4) Todos os Testes"
echo "5) Sair"
echo ""

read -p "Digite sua opção [1-5]: " option

case $option in
    1)
        run_basic_tests
        ;;
    2)
        run_load_tests
        ;;
    3)
        run_data_driven_tests
        ;;
    4)
        echo "🏃‍♂️ Executando todos os testes sequencialmente..."
        run_basic_tests
        echo ""
        echo "⏳ Aguardando 30 segundos antes dos testes de carga..."
        sleep 30
        run_load_tests
        echo ""
        echo "⏳ Aguardando 30 segundos antes dos testes data-driven..."
        sleep 30
        run_data_driven_tests
        ;;
    5)
        echo "👋 Saindo..."
        exit 0
        ;;
    *)
        echo "❌ Opção inválida. Tente novamente."
        exit 1
        ;;
esac

echo ""
echo "✅ Testes concluídos!"
echo "📄 Relatórios salvos em: ./reports/"
echo ""
echo "Para visualizar os relatórios HTML:"
echo "   - Abra os arquivos .html na pasta reports/ no seu navegador"
echo ""
echo "Para mais opções avançadas, consulte o README.md"
