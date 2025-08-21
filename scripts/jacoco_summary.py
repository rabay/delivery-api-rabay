#!/usr/bin/env python3
import xml.etree.ElementTree as ET
import os

def parse_counters(xml_path):
    tree = ET.parse(xml_path)
    root = tree.getroot()
    counters = {}
    for counter in root.findall('counter'):
        ctype = counter.attrib['type']
        missed = int(counter.attrib['missed'])
        covered = int(counter.attrib['covered'])
        total = missed + covered
        pct = 100.0 * covered / total if total > 0 else 0.0
        counters[ctype] = {'missed': missed, 'covered': covered, 'total': total, 'pct': pct}
    return counters

def to_markdown_table(counters):
    header = '| Tipo | Cobertos | Perdidos | Total | Cobertura (%) |\n|------|----------|----------|-------|---------------|'
    lines = [header]
    for ctype in ['INSTRUCTION','BRANCH','LINE','COMPLEXITY','METHOD','CLASS']:
        if ctype in counters:
            c = counters[ctype]
            lines.append(f"| {ctype} | {c['covered']} | {c['missed']} | {c['total']} | {c['pct']:.2f} |")
    return '\n'.join(lines)

if __name__ == "__main__":
    xml_path = os.environ.get('JACOCO_XML', 'target/site/jacoco/jacoco.xml')
    html_path = os.environ.get('JACOCO_HTML', 'target/site/jacoco/index.html')
    counters = parse_counters(xml_path)
    table = to_markdown_table(counters)
    print('## Cobertura Jacoco')
    print(table)
    print(f'\n[Ver relatório HTML Jacoco]({html_path})')
