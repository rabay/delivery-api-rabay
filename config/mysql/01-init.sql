-- Script de inicialização do MySQL para Delivery API
-- Executado automaticamente na primeira inicialização do container

-- Criar banco de dados se não existir
CREATE DATABASE IF NOT EXISTS deliverydb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Usar o banco criado
USE deliverydb;

-- Configurar timezone para UTC
SET time_zone = '+00:00';

-- Configurações de sessão para desenvolvimento
SET sql_mode = 'STRICT_TRANS_TABLES,NO_ZERO_DATE,NO_ZERO_IN_DATE,ERROR_FOR_DIVISION_BY_ZERO';
SET autocommit = 1;

-- Log de inicialização
SELECT 'Banco deliverydb inicializado com sucesso!' as status;