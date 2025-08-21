#!/usr/bin/env python3
import subprocess
import os
import sys

PROJECT_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DC_BIN = os.path.join(PROJECT_DIR, 'bin', 'dependency-check', 'bin', 'dependency-check.sh')
REPORT_DIR = os.path.join(PROJECT_DIR, 'dependency-check-report')
SCAN_DIR = PROJECT_DIR

# Parâmetros padrão
project = 'delivery-api'
format = 'HTML'
out = REPORT_DIR

# Permite argumentos opcionais
if len(sys.argv) > 1:
    for arg in sys.argv[1:]:
        if arg.startswith('--project='):
            project = arg.split('=',1)[1]
        elif arg.startswith('--format='):
            format = arg.split('=',1)[1]
        elif arg.startswith('--out='):
            out = arg.split('=',1)[1]
        elif arg.startswith('--scan='):
            SCAN_DIR = arg.split('=',1)[1]


# Obtém a NVD API Key da variável de ambiente
nvd_api_key = os.environ.get('NVD_API_KEY')
if not nvd_api_key:
    print('Erro: variável de ambiente NVD_API_KEY não definida.')
    sys.exit(1)

cmd = [
    DC_BIN,
    '--project', project,
    '--scan', SCAN_DIR,
    '--format', format,
    '--out', out,
    '--nvdApiKey', nvd_api_key
]
print(f"Executando: {' '.join(cmd)}")
subprocess.run(cmd, check=True)
print(f"Relatório salvo em: {out}")
