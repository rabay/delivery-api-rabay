#!/usr/bin/env python3
"""
Script para analisar relatório de cobertura JaCoCo
"""
import csv
import os
from collections import defaultdict

def analyze_coverage():
    csv_file = 'target/site/jacoco/jacoco.csv'

    if not os.path.exists(csv_file):
        print(f"Arquivo {csv_file} não encontrado!")
        return

    # Dicionários para armazenar dados por pacote
    package_data = defaultdict(lambda: {
        'instructions_covered': 0,
        'instructions_missed': 0,
        'branches_covered': 0,
        'branches_missed': 0,
        'lines_covered': 0,
        'lines_missed': 0,
        'methods_covered': 0,
        'methods_missed': 0,
        'classes': set()  # Para contar classes únicas
    })

    # Ler arquivo CSV
    with open(csv_file, 'r', encoding='utf-8') as f:
        reader = csv.DictReader(f)
        for row in reader:
            package = row['PACKAGE']
            if package:  # Ignorar linhas vazias
                data = package_data[package]
                data['instructions_covered'] += int(row['INSTRUCTION_COVERED'])
                data['instructions_missed'] += int(row['INSTRUCTION_MISSED'])
                data['branches_covered'] += int(row['BRANCH_COVERED'])
                data['branches_missed'] += int(row['BRANCH_MISSED'])
                data['lines_covered'] += int(row['LINE_COVERED'])
                data['lines_missed'] += int(row['LINE_MISSED'])
                data['methods_covered'] += int(row['METHOD_COVERED'])
                data['methods_missed'] += int(row['METHOD_MISSED'])
                data['classes'].add(row['CLASS'])  # Adicionar classe ao set

    print("## Análise de Cobertura por Pacote\n")
    print("| Pacote | Instruções | Branches | Linhas | Métodos | Classes |")
    print("|--------|------------|----------|--------|---------|---------|")

    for package, data in sorted(package_data.items()):
        # Calcular percentuais
        inst_total = data['instructions_covered'] + data['instructions_missed']
        inst_pct = (data['instructions_covered'] / inst_total * 100) if inst_total > 0 else 0

        branch_total = data['branches_covered'] + data['branches_missed']
        branch_pct = (data['branches_covered'] / branch_total * 100) if branch_total > 0 else 0

        line_total = data['lines_covered'] + data['lines_missed']
        line_pct = (data['lines_covered'] / line_total * 100) if line_total > 0 else 0

        method_total = data['methods_covered'] + data['methods_missed']
        method_pct = (data['methods_covered'] / method_total * 100) if method_total > 0 else 0

        class_count = len(data['classes'])

        print(".1f")

    print("\n## Pacotes com Menor Cobertura\n")

    # Encontrar pacotes com baixa cobertura
    low_coverage = []
    for package, data in package_data.items():
        inst_total = data['instructions_covered'] + data['instructions_missed']
        inst_pct = (data['instructions_covered'] / inst_total * 100) if inst_total > 0 else 0

        if inst_pct < 50:  # Menos de 50% cobertura
            low_coverage.append((package, inst_pct))

    low_coverage.sort(key=lambda x: x[1])  # Ordenar por cobertura ascendente

    for package, pct in low_coverage[:5]:  # Top 5 com menor cobertura
        print(".1f")

if __name__ == "__main__":
    analyze_coverage()