#!/usr/bin/env python3
import csv
import sys

def analyze_coverage(csv_file):
    """Analisa o relatório JaCoCo e identifica classes com baixa cobertura"""
    classes = []

    with open(csv_file, 'r') as f:
        reader = csv.DictReader(f)
        for row in reader:
            try:
                missed = int(row['INSTRUCTION_MISSED'])
                covered = int(row['INSTRUCTION_COVERED'])
                total = missed + covered

                if total > 0:  # Evita divisão por zero
                    coverage = (covered / total) * 100
                    classes.append({
                        'package': row['PACKAGE'],
                        'class': row['CLASS'],
                        'coverage': coverage,
                        'missed': missed,
                        'covered': covered,
                        'total': total
                    })
            except (ValueError, KeyError):
                continue

    # Ordena por cobertura ascendente (menor cobertura primeiro)
    classes.sort(key=lambda x: x['coverage'])

    return classes

def main():
    csv_file = '/root/fat-arq/delivery-api-rabay/target/site/jacoco/jacoco.csv'

    try:
        classes = analyze_coverage(csv_file)

        print("=== CLASSES COM MENOR COBERTURA DE TESTES ===")
        print(f"Total de classes analisadas: {len(classes)}")
        print()

        # Mostra as 20 classes com menor cobertura
        low_coverage = [c for c in classes if c['coverage'] < 50][:20]

        print("Top 20 classes com cobertura < 50%:")
        print("-" * 80)
        print(f"{'#':<3} {'Classe':<50} {'Cobertura':<10} {'Linhas':<8}")
        print("-" * 80)

        for i, cls in enumerate(low_coverage, 1):
            class_name = f"{cls['package']}.{cls['class']}"
            if len(class_name) > 47:
                class_name = class_name[:44] + "..."
            print(f"{i:<3} {class_name:<50} {cls['coverage']:<10.1f} {cls['total']:<8}")

        print()
        print("=== ANÁLISE POR PACOTE ===")

        # Agrupa por pacote
        package_stats = {}
        for cls in classes:
            pkg = cls['package']
            if pkg not in package_stats:
                package_stats[pkg] = {'total': 0, 'covered': 0, 'missed': 0, 'classes': 0}

            package_stats[pkg]['total'] += cls['total']
            package_stats[pkg]['covered'] += cls['covered']
            package_stats[pkg]['missed'] += cls['missed']
            package_stats[pkg]['classes'] += 1

        # Calcula cobertura por pacote
        package_coverage = []
        for pkg, stats in package_stats.items():
            if stats['total'] > 0:
                coverage = (stats['covered'] / stats['total']) * 100
                package_coverage.append({
                    'package': pkg,
                    'coverage': coverage,
                    'classes': stats['classes'],
                    'total_lines': stats['total'],
                    'covered_lines': stats['covered'],
                    'missed_lines': stats['missed']
                })

        package_coverage.sort(key=lambda x: x['coverage'])

        print("Pacotes com menor cobertura:")
        print("-" * 80)
        print(f"{'#':<3} {'Pacote':<40} {'Cobertura':<10} {'Classes':<8}")
        print("-" * 80)

        for i, pkg in enumerate(package_coverage[:10], 1):
            pkg_name = pkg['package']
            if len(pkg_name) > 37:
                pkg_name = pkg_name[:34] + "..."
            print(f"{i:<3} {pkg_name:<40} {pkg['coverage']:<10.1f} {pkg['classes']:<8}")

    except FileNotFoundError:
        print(f"Arquivo não encontrado: {csv_file}")
        sys.exit(1)
    except Exception as e:
        print(f"Erro ao analisar arquivo: {e}")
        sys.exit(1)

if __name__ == "__main__":
    main()