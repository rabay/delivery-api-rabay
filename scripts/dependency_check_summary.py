#!/usr/bin/env python3
import xml.etree.ElementTree as ET
import os
import sys

def parse_dependency_check_xml(xml_path):
    tree = ET.parse(xml_path)
    root = tree.getroot()
    summary = {
        'dependencies': 0,
        'vulnerable': 0,
        'suppressed': 0,
        'failures': 0,
        'vulns': {}
    }
    for dep in root.findall('.//dependency'):
        summary['dependencies'] += 1
        vulns = dep.findall('vulnerability')
        if vulns:
            summary['vulnerable'] += 1
            for v in vulns:
                name = v.findtext('name') or v.findtext('cve') or 'UNKNOWN'
                severity = v.findtext('severity') or 'UNKNOWN'
                summary['vulns'].setdefault(severity, 0)
                summary['vulns'][severity] += 1
    return summary

def to_markdown(summary):
    lines = []
    lines.append('## Dependency-Check Summary')
    lines.append(f"- Total de dependências analisadas: {summary['dependencies']}")
    lines.append(f"- Dependências vulneráveis: {summary['vulnerable']}")
    if summary['vulns']:
        lines.append('\n### Vulnerabilidades encontradas por severidade:')
        lines.append('| Severidade | Qtde |')
        lines.append('|------------|------|')
        for sev, count in summary['vulns'].items():
            lines.append(f"| {sev} | {count} |")
    else:
        lines.append('\nNenhuma vulnerabilidade encontrada!')
    return '\n'.join(lines)

if __name__ == "__main__":
    xml_path = os.environ.get('DC_XML', 'dependency-check-report/dependency-check-report.xml')
    html_path = os.environ.get('DC_HTML', 'dependency-check-report/index.html')
    if not os.path.exists(xml_path):
        print(f"Arquivo XML não encontrado: {xml_path}")
        sys.exit(1)
    summary = parse_dependency_check_xml(xml_path)
    print(to_markdown(summary))
    print(f'\n[Ver relatório HTML Dependency-Check]({html_path})')
