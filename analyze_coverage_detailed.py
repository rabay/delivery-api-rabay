import csv
import sys
from collections import defaultdict

def calculate_percentage(covered, missed):
    """Calcula percentual de cobertura."""
    total = covered + missed
    return (covered / total * 100) if total > 0 else 0

def analyze_coverage(csv_file):
    """Analisa o relatório de cobertura JaCoCo e gera estatísticas detalhadas."""

    # Dicionários para armazenar dados por pacote
    package_stats = defaultdict(lambda: {
        'instruction_missed': 0, 'instruction_covered': 0,
        'branch_missed': 0, 'branch_covered': 0,
        'line_missed': 0, 'line_covered': 0,
        'method_missed': 0, 'method_covered': 0,
        'class_missed': 0, 'class_covered': 0,
        'classes': []
    })

    # Totais globais
    total_instruction_missed = 0
    total_instruction_covered = 0
    total_branch_missed = 0
    total_branch_covered = 0
    total_line_missed = 0
    total_line_covered = 0
    total_method_missed = 0
    total_method_covered = 0
    total_class_missed = 0
    total_class_covered = 0

    with open(csv_file, 'r') as f:
        reader = csv.DictReader(f)

        for row in reader:
            package = row['PACKAGE']
            class_name = row['CLASS']

            # Converter valores para inteiros
            instruction_missed = int(row['INSTRUCTION_MISSED'])
            instruction_covered = int(row['INSTRUCTION_COVERED'])
            branch_missed = int(row['BRANCH_MISSED'])
            branch_covered = int(row['BRANCH_COVERED'])
            line_missed = int(row['LINE_MISSED'])
            line_covered = int(row['LINE_COVERED'])
            method_missed = int(row['METHOD_MISSED'])
            method_covered = int(row['METHOD_COVERED'])

            # Calcular classes (cada linha representa uma classe)
            class_total = 1
            class_covered = 1 if (instruction_covered + branch_covered + line_covered + method_covered) > 0 else 0
            class_missed = class_total - class_covered

            # Atualizar totais globais
            total_instruction_missed += instruction_missed
            total_instruction_covered += instruction_covered
            total_branch_missed += branch_missed
            total_branch_covered += branch_covered
            total_line_missed += line_missed
            total_line_covered += line_covered
            total_method_missed += method_missed
            total_method_covered += method_covered
            total_class_missed += class_missed
            total_class_covered += class_covered

            # Atualizar estatísticas por pacote
            package_stats[package]['instruction_missed'] += instruction_missed
            package_stats[package]['instruction_covered'] += instruction_covered
            package_stats[package]['branch_missed'] += branch_missed
            package_stats[package]['branch_covered'] += branch_covered
            package_stats[package]['line_missed'] += line_missed
            package_stats[package]['line_covered'] += line_covered
            package_stats[package]['method_missed'] += method_missed
            package_stats[package]['method_covered'] += method_covered
            package_stats[package]['class_missed'] += class_missed
            package_stats[package]['class_covered'] += class_covered
            package_stats[package]['classes'].append({
                'name': class_name,
                'instruction_coverage': calculate_percentage(instruction_covered, instruction_missed),
                'branch_coverage': calculate_percentage(branch_covered, branch_missed),
                'line_coverage': calculate_percentage(line_covered, line_missed),
                'method_coverage': calculate_percentage(method_covered, method_missed)
            })

    # Calcular percentuais globais
    global_stats = {
        'instruction': calculate_percentage(total_instruction_covered, total_instruction_missed),
        'branch': calculate_percentage(total_branch_covered, total_branch_missed),
        'line': calculate_percentage(total_line_covered, total_line_missed),
        'method': calculate_percentage(total_method_covered, total_method_missed),
        'class': calculate_percentage(total_class_covered, total_class_missed)
    }

    # Calcular estatísticas por pacote
    package_coverage = {}
    for package, stats in package_stats.items():
        package_coverage[package] = {
            'instruction': calculate_percentage(stats['instruction_covered'], stats['instruction_missed']),
            'branch': calculate_percentage(stats['branch_covered'], stats['branch_missed']),
            'line': calculate_percentage(stats['line_covered'], stats['line_missed']),
            'method': calculate_percentage(stats['method_covered'], stats['method_missed']),
            'class': calculate_percentage(stats['class_covered'], stats['class_missed']),
            'classes_count': len(stats['classes'])
        }

    return global_stats, package_coverage, package_stats

def print_report(global_stats, package_coverage, package_stats):
    """Imprime o relatório de cobertura de forma organizada."""

    print("=== RELATÓRIO DE COBERTURA DE TESTES ===\n")

    print("📊 COBERTURA GERAL:")
    print(".1f")
    print(".1f")
    print(".1f")
    print(".1f")
    print(".1f")
    print()

    print("📁 COBERTURA POR PACOTE:")
    print("-" * 80)
    print("<30")
    print("-" * 80)

    # Ordenar pacotes por cobertura de instruções (do menor para o maior)
    sorted_packages = sorted(package_coverage.items(),
                           key=lambda x: x[1]['instruction'],
                           reverse=False)

    for package, coverage in sorted_packages:
        classes_count = coverage['classes_count']
        print("<30")

    print()

    print("🎯 ÁREAS QUE PRECISAM DE MAIS TESTES:")
    print("-" * 80)

    # Identificar pacotes com baixa cobertura
    low_coverage_packages = []
    for package, coverage in package_coverage.items():
        if coverage['instruction'] < 70:  # Menos de 70% de cobertura
            low_coverage_packages.append((package, coverage))

    if low_coverage_packages:
        low_coverage_packages.sort(key=lambda x: x[1]['instruction'])

        for package, coverage in low_coverage_packages:
            print(f"📦 {package}:")
            print(".1f")
            print(f"   📄 Classes no pacote: {coverage['classes_count']}")

            # Mostrar classes com baixa cobertura neste pacote
            low_coverage_classes = []
            for class_info in package_stats[package]['classes']:
                if class_info['instruction_coverage'] < 70:
                    low_coverage_classes.append(class_info)

            if low_coverage_classes:
                print("   🔴 Classes com baixa cobertura:")
                for class_info in sorted(low_coverage_classes, key=lambda x: x['instruction_coverage'])[:5]:  # Top 5
                    print(".1f")
            print()
    else:
        print("✅ Todos os pacotes têm cobertura adequada (>70%)")

    print("\n💡 RECOMENDAÇÕES PARA MELHORAR A COBERTURA:")
    print("-" * 80)

    # Análise específica por tipo de componente
    controller_coverage = package_coverage.get('com.deliverytech.delivery_api.controller', {})
    service_coverage = package_coverage.get('com.deliverytech.delivery_api.service', {})
    service_impl_coverage = package_coverage.get('com.deliverytech.delivery_api.service.impl', {})

    if controller_coverage.get('instruction', 100) < 80:
        print("🎮 Controllers: Focar em testes de integração e casos extremos")
        print("   - Adicionar testes para validação de entrada")
        print("   - Testar tratamento de erros e exceções")
        print("   - Cobrir cenários de autenticação/autorização")

    if service_coverage.get('instruction', 100) < 80 or service_impl_coverage.get('instruction', 100) < 80:
        print("🔧 Services: Expandir testes unitários e de integração")
        print("   - Testar lógica de negócio complexa")
        print("   - Cobrir cenários de concorrência")
        print("   - Testar integração com repositórios")

    if global_stats['branch'] < 80:
        print("🌿 Branches: Melhorar cobertura de condições")
        print("   - Testar caminhos alternativos no código")
        print("   - Cobrir casos de erro e validações")

    print("\n✅ PRÓXIMOS PASSOS:")
    print("1. Priorizar testes para pacotes com menor cobertura")
    print("2. Adicionar testes de integração para controllers")
    print("3. Expandir testes unitários para services")
    print("4. Melhorar cobertura de branches com testes de condições")
    print("5. Considerar testes de mutação para validar qualidade")

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Uso: python3 analyze_coverage_detailed.py <arquivo_csv>")
        sys.exit(1)

    csv_file = sys.argv[1]
    global_stats, package_coverage, package_stats = analyze_coverage(csv_file)
    print_report(global_stats, package_coverage, package_stats)