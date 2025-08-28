-- Configurações de performance para ambiente de desenvolvimento
-- Otimizações leves que não impactam a estabilidade

USE deliverydb;

-- Configurações de timeout para desenvolvimento (não muito restritivas)
SET GLOBAL wait_timeout = 600;
SET GLOBAL interactive_timeout = 600;

-- Configurações de log para desenvolvimento (reduzir logging excessivo)
SET GLOBAL slow_query_log = 0;
SET GLOBAL general_log = 0;

-- Configurações de InnoDB para desenvolvimento (valores conservadores)
-- Nota: Algumas configurações podem não ser alteráveis em runtime
SET GLOBAL innodb_flush_log_at_trx_commit = 2;  -- Menos fsync, melhor performance
SET GLOBAL innodb_lock_wait_timeout = 60;       -- Timeout de locks

-- Configurações de conexão
SET GLOBAL max_connections = 100;
SET GLOBAL max_connect_errors = 1000;

-- Mostrar configurações aplicadas
SHOW VARIABLES LIKE 'wait_timeout';
SHOW VARIABLES LIKE 'max_connections';
SHOW VARIABLES LIKE 'innodb_flush_log_at_trx_commit';

SELECT 'Otimizações de performance aplicadas!' as status;