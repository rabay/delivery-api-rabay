package com.deliverytech.deliveryapi.service;

import com.deliverytech.deliveryapi.domain.model.RestaurantCategory;
import com.deliverytech.deliveryapi.domain.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Service para operações de teste e limpeza de dados
 * APENAS PARA DESENVOLVIMENTO E TESTES
 */
@Service
public class TestService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RestaurantCategoryRepository restaurantCategoryRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RestaurantRepository restaurantRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private DeliveryAreaRepository deliveryAreaRepository;

    /**
     * Limpa todos os dados do banco de dados
     * CUIDADO: Remove TODOS os dados!
     */
    @Transactional
    public void cleanupDatabase() {
        try {
            System.out.println("🧹 Iniciando limpeza do banco de dados...");
            
            // Usar TRUNCATE ao invés de DELETE para ser mais eficiente com H2
            List<String> tablesToClean = Arrays.asList(
                "order_items",
                "orders", 
                "reviews",
                "notifications",
                "payments",
                "delivery_areas",
                "product_tags",
                "products",
                "product_categories",
                "restaurant_category_associations",
                "restaurants",
                "users",
                "restaurant_categories"
            );
            
            // Desabilitar constraint checking temporariamente
            entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
            
            // Limpar tabelas usando TRUNCATE
            for (String table : tablesToClean) {
                try {
                    entityManager.createNativeQuery("TRUNCATE TABLE " + table).executeUpdate();
                    System.out.println("✅ Tabela " + table + " limpa");
                } catch (Exception e) {
                    System.out.println("⚠️ Erro ao limpar tabela " + table + ": " + e.getMessage());
                }
            }
            
            // Reabilitar constraint checking
            entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
            
            // Reset das sequências do H2
            resetSequences();
            
            // Flush para garantir que as mudanças sejam persistidas
            entityManager.flush();
            entityManager.clear();
            
            System.out.println("✅ Limpeza do banco concluída com sucesso!");
            
        } catch (Exception e) {
            // Tentar reabilitar constraints em caso de erro
            try {
                entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
            } catch (Exception ignored) {
            }
            System.out.println("❌ Erro durante limpeza: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Inicializa dados básicos necessários para os testes
     */
    @Transactional
    public void initializeTestData() {
        try {
            System.out.println("🔄 Iniciando inicialização de dados básicos...");
            createRestaurantCategories();
            
            // Flush para garantir que as mudanças sejam persistidas
            entityManager.flush();
            System.out.println("✅ Dados básicos inicializados com sucesso!");
        } catch (Exception e) {
            System.out.println("❌ Erro na inicialização: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Reset completo: limpa e reinicializa
     * Usa uma abordagem sem transação única para evitar rollback
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void resetDatabase() {
        try {
            System.out.println("🔄 Iniciando reset completo do banco...");
            
            // Operação 1: Limpeza (transação isolada)
            cleanupDatabaseSimple();
            System.out.println("✅ Limpeza do banco concluída");
            
            // Forçar flush
            entityManager.flush();
            entityManager.clear();
            
            // Operação 2: Inicialização (mesmo contexto transacional)
            createRestaurantCategories();
            System.out.println("✅ Reset completo realizado com sucesso!");
        } catch (Exception e) {
            System.out.println("❌ Erro durante reset: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro durante reset do banco", e);
        }
    }

    /**
     * Limpeza simplificada sem transação explícita
     */
    private void cleanupDatabaseSimple() {
        try {
            System.out.println("🧹 Iniciando limpeza simples do banco...");
            
            // Limpar usando os repositórios ao invés de SQL nativo
            orderItemRepository.deleteAll();
            orderRepository.deleteAll();
            reviewRepository.deleteAll();
            notificationRepository.deleteAll();
            paymentRepository.deleteAll();
            deliveryAreaRepository.deleteAll();
            productRepository.deleteAll();
            productCategoryRepository.deleteAll();
            restaurantRepository.deleteAll();
            userRepository.deleteAll();
            restaurantCategoryRepository.deleteAll();
            
            System.out.println("✅ Limpeza simples concluída");
        } catch (Exception e) {
            System.out.println("❌ Erro durante limpeza simples: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Reset das sequências do H2 para começar do ID 1
     */
    private void resetSequences() {
        // Lista de sequências que podem existir no H2
        List<String> sequences = Arrays.asList(
            "users_seq",
            "restaurants_seq", 
            "restaurant_categories_seq",
            "product_categories_seq",
            "products_seq",
            "orders_seq",
            "order_items_seq",
            "payments_seq",
            "notifications_seq",
            "reviews_seq",
            "delivery_areas_seq"
        );
        
        for (String sequence : sequences) {
            try {
                // Primeiro tenta verificar se a sequência existe
                Long currentValue = (Long) entityManager.createNativeQuery("SELECT NEXT VALUE FOR " + sequence).getSingleResult();
                // Se chegou até aqui, a sequência existe, então pode resetar
                entityManager.createNativeQuery("ALTER SEQUENCE " + sequence + " RESTART WITH 1").executeUpdate();
                System.out.println("✅ Sequência " + sequence + " resetada");
            } catch (Exception e) {
                // Sequência não existe ou erro ao acessar, continuar silenciosamente
                // System.out.println("ℹ️ Sequência " + sequence + " não encontrada ou não precisa ser resetada");
            }
        }
    }

    /**
     * Cria categorias de restaurante básicas
     */
    private void createRestaurantCategories() {
        String[][] categories = {
            {"Italiana", "Restaurantes especializados em culinária italiana"},
            {"Hamburgueria", "Hambúrgueres gourmet e fast food"},
            {"Japonesa", "Sushi, sashimi e culinária oriental"},
            {"Cafeteria", "Cafés, doces e bebidas especiais"}
        };
        
        int categoriasControle = 0;
        
        for (String[] categoryData : categories) {
            try {
                // Verifica se já existe usando Optional
                Optional<RestaurantCategory> existingCategory = restaurantCategoryRepository.findByName(categoryData[0]);
                
                if (existingCategory.isEmpty()) {
                    RestaurantCategory category = new RestaurantCategory(categoryData[0], categoryData[1]);
                    RestaurantCategory saved = restaurantCategoryRepository.save(category);
                    categoriasControle++;
                    System.out.println("✅ Categoria criada: " + saved.getName() + " (ID: " + saved.getId() + ")");
                } else {
                    System.out.println("ℹ️ Categoria já existe: " + categoryData[0]);
                }
            } catch (Exception e) {
                System.out.println("❌ Erro ao criar categoria " + categoryData[0] + ": " + e.getMessage());
                // Não relançar a exceção para evitar rollback da transação toda
            }
        }
        
        System.out.println("📊 Total de categorias criadas: " + categoriasControle);
    }

    /**
     * Cria categorias de produto básicas
     * NOTA: ProductCategory requer um restaurant_id associado.
     * Este método não cria product_categories pois elas dependem de restaurantes específicos.
     */
    private void createProductCategories() {
        // ProductCategory na aplicação requer um Restaurant associado (campo obrigatório)
        // Não criamos categorias genéricas pois cada categoria pertence a um restaurante específico
        // As categorias de produto são criadas junto com os produtos no DataInitializer
        System.out.println("ℹ️ ProductCategories são criadas automaticamente pelo DataInitializer junto com os restaurantes");
    }
}
