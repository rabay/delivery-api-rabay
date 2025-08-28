-- Configurações de charset e collation para suporte completo a UTF-8
-- Executado após a inicialização básica do banco

USE deliverydb;

-- Garantir que o banco use utf8mb4
ALTER DATABASE deliverydb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Configurar variáveis de sessão para UTF-8
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET character_set_connection = utf8mb4;
SET character_set_results = utf8mb4;
SET character_set_client = utf8mb4;

-- Verificar configurações aplicadas
SHOW VARIABLES LIKE 'character_set%';
SHOW VARIABLES LIKE 'collation%';

SELECT 'Configurações de charset UTF-8 aplicadas!' as status;