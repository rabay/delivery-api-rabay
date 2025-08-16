#!/bin/bash

# Script refatorado para executar testes da Delivery Tech API com Newman
# Usando a Collection Unificada e funcionalidades aprimoradas
# Autor: Delivery Tech API Team
# Última atualização: 2025-08-15

echo "🚀 Delivery Tech API - Executor de Testes (v2.0)"
echo "================================================="
echo "📦 Collection: Unified Collection (74% de sucesso)"
echo ""

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Variáveis
UNIFIED_COLLECTION="Delivery-API-Unified-Collection.postman_collection.json"
LOAD_TEST_COLLECTION="Delivery-API-LoadTest.postman_collection.json"
ENVIRONMENT="Delivery-API-Local.postman_environment.json"
REPORTS_DIR="reports"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)

# Verificar se Newman está instalado
check_newman() {
    if ! command -v newman &> /dev/null; then
        echo -e "${YELLOW}⚠️ Newman não está instalado. Instalando...${NC}"
        npm install -g newman newman-reporter-htmlextra
        if [ $? -ne 0 ]; then
            echo -e "${RED}❌ Erro ao instalar Newman. Verifique sua instalação do Node.js${NC}"
            exit 1
        fi
        echo -e "${GREEN}✅ Newman instalado com sucesso!${NC}"
    else
        echo -e "${GREEN}✅ Newman encontrado: $(newman --version)${NC}"
    fi
}

# Verificar se a API está rodando
check_api() {
    echo -e "${BLUE}🔍 Verificando se a API está rodando...${NC}"
    
    local max_attempts=5
    local attempt=1
    
    while [ $attempt -le $max_attempts ]; do
        if curl -s http://localhost:8080/health > /dev/null; then
            echo -e "${GREEN}✅ API está rodando corretamente!${NC}"
            
            # Verificar dados básicos (corrigindo warnings de lint)
            local customers
            local restaurants
            customers=$(curl -s http://localhost:8080/api/v1/customers | jq '. | length' 2>/dev/null || echo "0")
            restaurants=$(curl -s http://localhost:8080/api/v1/restaurants | jq '.data | length' 2>/dev/null || echo "0")
            
            echo -e "${BLUE}📊 Dados disponíveis: ${customers} customers, ${restaurants} restaurants${NC}"
            return 0
        fi
        
        echo -e "${YELLOW}⏳ Tentativa $attempt/$max_attempts - API não respondeu, aguardando...${NC}"
        sleep 5
        ((attempt++))
    done
    
    echo -e "${RED}❌ API não está respondendo em http://localhost:8080${NC}"
    echo -e "${YELLOW}   💡 Dica: Execute 'mvn spring-boot:run' na pasta do projeto${NC}"
    exit 1
}

# Verificar arquivos necessários
check_files() {
    echo -e "${BLUE}📁 Verificando arquivos necessários...${NC}"
    
    if [ ! -f "$UNIFIED_COLLECTION" ]; then
        echo -e "${RED}❌ Collection não encontrada: $UNIFIED_COLLECTION${NC}"
        exit 1
    fi
    
    if [ ! -f "$ENVIRONMENT" ]; then
        echo -e "${RED}❌ Environment não encontrado: $ENVIRONMENT${NC}"
        exit 1
    fi
    
    echo -e "${GREEN}✅ Arquivos encontrados${NC}"
}

# Função para executar testes da collection unificada
run_unified_tests() {
    echo -e "${BLUE}📋 Executando Collection Unificada...${NC}"
    echo "   🎯 19 requests, 66 assertions esperadas"
    echo "   📈 Taxa de sucesso atual: ~74%"
    echo ""
    
    newman run "$UNIFIED_COLLECTION" \
        --environment "$ENVIRONMENT" \
        --reporters cli,htmlextra \
        --reporter-htmlextra-export "$REPORTS_DIR/unified-test-results-$TIMESTAMP.html" \
        --reporter-htmlextra-title "Delivery API - Unified Collection Tests" \
        --delay-request 300 \
        --timeout-request 10000 \
        --color on
        
    local exit_code=$?
    
    if [ $exit_code -eq 0 ]; then
        echo -e "${GREEN}✅ Testes da Collection Unificada concluídos com sucesso!${NC}"
    else
        echo -e "${YELLOW}⚠️ Testes concluídos com algumas falhas (esperado: ~17 assertions failing)${NC}"
    fi
    
    return $exit_code
}

# Função para executar testes de carga
run_load_tests() {
    if [ ! -f "$LOAD_TEST_COLLECTION" ]; then
        echo -e "${YELLOW}⚠️ Collection de Load Test não encontrada: $LOAD_TEST_COLLECTION${NC}"
        echo "   Pulando testes de carga..."
        return 0
    fi
    
    echo -e "${BLUE}⚡ Executando testes de carga...${NC}"
    echo "   🔄 Iterações: 20"
    echo "   ⏱️ Delay: 100ms entre requests"
    echo ""
    
    newman run "$LOAD_TEST_COLLECTION" \
        --environment "$ENVIRONMENT" \
        --iteration-count 20 \
        --delay-request 100 \
        --reporters cli,htmlextra \
        --reporter-htmlextra-export "$REPORTS_DIR/load-test-results-$TIMESTAMP.html" \
        --reporter-htmlextra-title "Delivery API - Load Tests" \
        --timeout-request 15000 \
        --color on
        
    local exit_code=$?
    
    if [ $exit_code -eq 0 ]; then
        echo -e "${GREEN}✅ Testes de carga concluídos com sucesso!${NC}"
    else
        echo -e "${YELLOW}⚠️ Testes de carga concluídos com algumas falhas${NC}"
    fi
    
    return $exit_code
}

# Função para executar testes específicos de uma seção
run_section_tests() {
    local section="$1"
    echo -e "${BLUE}🎯 Executando testes da seção: $section${NC}"
    echo ""
    
    newman run "$UNIFIED_COLLECTION" \
        --environment "$ENVIRONMENT" \
        --folder "$section" \
        --reporters cli,htmlextra \
        --reporter-htmlextra-export "$REPORTS_DIR/section-${section,,}-results-$TIMESTAMP.html" \
        --reporter-htmlextra-title "Delivery API - $section Tests" \
        --delay-request 300 \
        --color on
        
    local exit_code=$?
    
    if [ $exit_code -eq 0 ]; then
        echo -e "${GREEN}✅ Testes da seção '$section' concluídos com sucesso!${NC}"
    else
        echo -e "${YELLOW}⚠️ Testes da seção '$section' concluídos com algumas falhas${NC}"
    fi
    
    return $exit_code
}

# Função para executar diagnóstico rápido
run_quick_diagnostic() {
    echo -e "${BLUE}🔧 Executando diagnóstico rápido...${NC}"
    echo "   Testando apenas Health e System endpoints"
    echo ""
    
    newman run "$UNIFIED_COLLECTION" \
        --environment "$ENVIRONMENT" \
        --folder "🏠 Health & System" \
        --reporters cli \
        --delay-request 100 \
        --color on
        
    local exit_code=$?
    
    if [ $exit_code -eq 0 ]; then
        echo -e "${GREEN}✅ Diagnóstico: API está funcionando corretamente!${NC}"
    else
        echo -e "${RED}❌ Diagnóstico: Problemas detectados na API${NC}"
    fi
    
    return $exit_code
}

# Função para exibir estatísticas dos relatórios
show_statistics() {
    echo -e "${BLUE}📊 Estatísticas dos últimos testes:${NC}"
    
    if [ -d "$REPORTS_DIR" ] && [ "$(ls -A $REPORTS_DIR 2>/dev/null)" ]; then
        local report_count
        report_count=$(ls $REPORTS_DIR/*.html 2>/dev/null | wc -l)
        echo "   📄 Relatórios disponíveis: $report_count"
        echo "   📁 Pasta de relatórios: ./$REPORTS_DIR/"
        
        # Mostrar os 3 relatórios mais recentes
        echo ""
        echo -e "${BLUE}📋 Últimos relatórios:${NC}"
        ls -lt $REPORTS_DIR/*.html 2>/dev/null | head -3 | while read -r line; do
            filename=$(echo "$line" | awk '{print $9}')
            echo "   - $(basename "$filename")"
        done
    else
        echo "   📄 Nenhum relatório encontrado ainda"
    fi
}

# Inicializar
mkdir -p "$REPORTS_DIR"

# Verificações iniciais
check_newman
check_files
check_api

echo ""
echo -e "${YELLOW}📋 Menu de Opções:${NC}"
echo "1) 🚀 Testes Completos (Collection Unificada)"
echo "2) ⚡ Testes de Carga (Performance)"
echo "3) 🎯 Testes por Seção"
echo "4) 🔧 Diagnóstico Rápido (Health Check)"
echo "5) 📊 Mostrar Estatísticas"
echo "6) 🔄 Todos os Testes (Completo + Carga)"
echo "7) 👋 Sair"
echo ""

read -p "Digite sua opção [1-7]: " option

case $option in
    1)
        run_unified_tests
        ;;
    2)
        run_load_tests
        ;;
    3)
        echo ""
        echo -e "${YELLOW}📋 Seções disponíveis:${NC}"
        echo "  a) 🏠 Health & System"
        echo "  b) 👥 Customers - Gerenciamento de Clientes"  
        echo "  c) 🍽️ Restaurants - Gestão de Restaurantes"
        echo "  d) 🍕 Products - Catálogo de Produtos"
        echo "  e) 📦 Orders - Gestão de Pedidos"
        echo ""
        read -p "Escolha a seção [a-e]: " section_choice
        
        case $section_choice in
            a) run_section_tests "🏠 Health & System" ;;
            b) run_section_tests "👥 Customers - Gerenciamento de Clientes" ;;
            c) run_section_tests "🍽️ Restaurants - Gestão de Restaurantes" ;;
            d) run_section_tests "🍕 Products - Catálogo de Produtos" ;;
            e) run_section_tests "📦 Orders - Gestão de Pedidos" ;;
            *) echo -e "${RED}❌ Seção inválida${NC}"; exit 1 ;;
        esac
        ;;
    4)
        run_quick_diagnostic
        ;;
    5)
        show_statistics
        ;;
    6)
        echo -e "${BLUE}🏃‍♂️ Executando bateria completa de testes...${NC}"
        echo ""
        
        # Diagnóstico primeiro
        run_quick_diagnostic
        echo ""
        
        # Testes principais
        echo -e "${YELLOW}⏳ Iniciando testes principais em 5 segundos...${NC}"
        sleep 5
        run_unified_tests
        echo ""
        
        # Testes de carga
        echo -e "${YELLOW}⏳ Aguardando 10 segundos antes dos testes de carga...${NC}"
        sleep 10
        run_load_tests
        ;;
    7)
        echo -e "${BLUE}👋 Saindo...${NC}"
        exit 0
        ;;
    *)
        echo -e "${RED}❌ Opção inválida. Tente novamente.${NC}"
        exit 1
        ;;
esac

echo ""
echo -e "${GREEN}✅ Execução concluída!${NC}"
echo -e "${BLUE}📄 Relatórios salvos em: ./$REPORTS_DIR/${NC}"
echo ""
echo -e "${YELLOW}💡 Dicas:${NC}"
echo "   • Abra os arquivos .html no navegador para visualizar relatórios detalhados"
echo "   • Use a opção 5 para ver estatísticas dos testes executados"
echo "   • Para problemas, verifique se a API está rodando com dados inicializados"
echo ""
echo -e "${BLUE}📚 Para mais informações, consulte o README.md${NC}"
