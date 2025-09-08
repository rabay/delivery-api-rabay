package com.deliverytech.delivery_api.config;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class TestDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    
    private static final Logger logger = LoggerFactory.getLogger(TestDataLoader.class);
    
    private final DataSource dataSource;
    
    public TestDataLoader(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        // Load test data immediately since tables are created by data-test.sql
        try {
            loadTestData();
        } catch (Exception e) {
            logger.error("Erro ao carregar dados de teste", e);
        }
    }
    
    private void loadTestData() throws SQLException {
        logger.info("Carregando dados de teste...");
        
        try (Connection connection = dataSource.getConnection()) {
            // Execute o script de dados de teste
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("data-test.sql"));
            logger.info("✅ Dados de teste carregados com sucesso");
        } catch (Exception e) {
            logger.error("❌ Falha ao carregar dados de teste", e);
            throw e;
        }
    }
}