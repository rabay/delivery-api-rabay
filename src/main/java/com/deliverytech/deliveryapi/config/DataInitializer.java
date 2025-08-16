package com.deliverytech.deliveryapi.config;

import com.deliverytech.deliveryapi.domain.model.Address;
import com.deliverytech.deliveryapi.domain.model.Restaurant;
import com.deliverytech.deliveryapi.domain.model.ProductCategory;
import com.deliverytech.deliveryapi.domain.model.RestaurantCategory;
import com.deliverytech.deliveryapi.domain.model.User;
import com.deliverytech.deliveryapi.domain.model.UserType;
import com.deliverytech.deliveryapi.domain.model.Product;
import com.deliverytech.deliveryapi.domain.model.Money;
import com.deliverytech.deliveryapi.domain.model.PaymentMethod;
import com.deliverytech.deliveryapi.domain.model.PaymentStatus;
import com.deliverytech.deliveryapi.domain.model.OrderStatus;
import com.deliverytech.deliveryapi.domain.model.NotificationType;
import com.deliverytech.deliveryapi.domain.model.DeliveryArea;
import com.deliverytech.deliveryapi.domain.model.Payment;
import com.deliverytech.deliveryapi.domain.model.Order;
import com.deliverytech.deliveryapi.domain.model.OrderItem;
import com.deliverytech.deliveryapi.domain.model.Review;
import com.deliverytech.deliveryapi.domain.model.ReviewType;
import com.deliverytech.deliveryapi.domain.model.Notification;
import com.deliverytech.deliveryapi.domain.repository.RestaurantRepository;
import com.deliverytech.deliveryapi.domain.repository.ProductCategoryRepository;
import com.deliverytech.deliveryapi.domain.repository.RestaurantCategoryRepository;
import com.deliverytech.deliveryapi.domain.repository.UserRepository;
import com.deliverytech.deliveryapi.domain.repository.ProductRepository;
import com.deliverytech.deliveryapi.domain.repository.OrderRepository;
import com.deliverytech.deliveryapi.domain.repository.OrderItemRepository;
import com.deliverytech.deliveryapi.domain.repository.PaymentRepository;
import com.deliverytech.deliveryapi.domain.repository.DeliveryAreaRepository;
import com.deliverytech.deliveryapi.domain.repository.NotificationRepository;
import com.deliverytech.deliveryapi.domain.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Configuração para popular o banco de dados com dados iniciais
 * Executado apenas na inicialização da aplicação
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private RestaurantCategoryRepository restaurantCategoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private DeliveryAreaRepository deliveryAreaRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public void run(String... args) throws Exception {
        // Verificar se dados básicos existem (restaurantes e categorias são essenciais)
        boolean needsInitialization = restaurantCategoryRepository.count() == 0 || 
                                     restaurantRepository.count() == 0;
        
        if (needsInitialization) {
            System.out.println("🚀 Iniciando criação de dados de teste...");
            System.out.println("📊 Status atual: Users=" + userRepository.count() + 
                              ", RestaurantCategories=" + restaurantCategoryRepository.count() + 
                              ", Restaurants=" + restaurantRepository.count() + 
                              ", Products=" + productRepository.count());
            
            // Primeiro: Categorias e dados básicos (ordem importante)
            createRestaurantCategories();
            
            // Verificar se precisa criar usuário padrão
            if (userRepository.count() == 0) {
                createSampleUsers(); // Usuários básicos se não existir nenhum
            }
            
            // Segundo: Restaurantes (dependem de categorias e podem ter owners)
            createSampleRestaurants();
            
            // Terceiro: Categorias de produtos (dependem de restaurantes)
            createSampleProductCategories();
            
            // Quarto: Produtos (dependem de restaurantes e categorias)
            createSampleProducts();
            
            // Quinto: Funcionalidades avançadas (dependem de usuários e restaurantes)
            createSamplePaymentMethods();
            createSampleOrderStatuses();
            createSamplePaymentStatuses();
            createSampleDeliveryAreas();
            createSampleNotificationTypes();
            createSampleReviewTypes();
            createSampleNotifications();
            createSampleReviews();
            createSampleOrdersAndItems();
            createSampleBusinessHours();
            
            System.out.println("✅ Dados de teste criados com sucesso!");
            System.out.println("📊 Status final: Users=" + userRepository.count() + 
                              ", RestaurantCategories=" + restaurantCategoryRepository.count() + 
                              ", Restaurants=" + restaurantRepository.count() + 
                              ", ProductCategories=" + productCategoryRepository.count() + 
                              ", Products=" + productRepository.count());
        } else {
            System.out.println("ℹ️ Dados básicos já existem no banco, pulando inicialização...");
            System.out.println("📊 Status atual: Users=" + userRepository.count() + 
                              ", RestaurantCategories=" + restaurantCategoryRepository.count() + 
                              ", Restaurants=" + restaurantRepository.count() + 
                              ", ProductCategories=" + productCategoryRepository.count() + 
                              ", Products=" + productRepository.count());
        }
    }
    // Métodos de expansão para todos os tipos de dados usados pela lógica
    private void createSamplePaymentMethods() {
        // Cadastrar métodos de pagamento de teste
        User testCustomer = userRepository.findById(1L).orElse(null);
        if (testCustomer != null) {
            // Pagamento 1: PIX
            Payment pixPayment = new Payment(PaymentMethod.PIX, new Money(new BigDecimal("50.00")), 999L);
            pixPayment.setTransactionId("PIX-TEST-123456");
            pixPayment.setStatus(PaymentStatus.APPROVED);
            paymentRepository.save(pixPayment);

            // Pagamento 2: Cartão de Crédito
            Payment creditCardPayment = new Payment(PaymentMethod.CREDIT_CARD, new Money(new BigDecimal("75.90")), 999L);
            creditCardPayment.setTransactionId("CC-TEST-789012");
            creditCardPayment.setStatus(PaymentStatus.PENDING);
            paymentRepository.save(creditCardPayment);

            // Pagamento 3: Cartão de Débito
            Payment debitCardPayment = new Payment(PaymentMethod.DEBIT_CARD, new Money(new BigDecimal("32.50")), 999L);
            debitCardPayment.setTransactionId("DC-TEST-345678");
            debitCardPayment.setStatus(PaymentStatus.PROCESSING);
            paymentRepository.save(debitCardPayment);

            // Pagamento 4: Dinheiro
            Payment cashPayment = new Payment(PaymentMethod.CASH, new Money(new BigDecimal("28.00")), 999L);
            cashPayment.setStatus(PaymentStatus.APPROVED);
            paymentRepository.save(cashPayment);
        }

        // Exibir métodos de pagamento disponíveis
        System.out.println("✅ Métodos de pagamento cadastrados e disponíveis:");
        for (PaymentMethod method : PaymentMethod.values()) {
            System.out.println("  - " + method.name() + ": " + method.getDescription());
        }
        System.out.println("✅ 4 pagamentos de teste cadastrados no sistema");
    }

    private void createSampleOrderStatuses() {
        // Criar pedidos com diferentes status de exemplo
        User testCustomer = userRepository.findById(1L).orElse(null);
        Restaurant pizzaria = restaurantRepository.findById(1L).orElse(null);
        Restaurant burgerHouse = restaurantRepository.findById(2L).orElse(null);
        
        if (testCustomer != null && pizzaria != null) {
            Address deliveryAddress = new Address(
                "Rua dos Exemplos", "200", "Apt 20",
                "Centro", "São Paulo", "SP", "01000-000", "Próximo ao metrô"
            );
            
            // Pedido 1: PENDING
            Order pendingOrder = new Order(testCustomer, pizzaria, deliveryAddress);
            pendingOrder.setStatus(OrderStatus.PENDING);
            orderRepository.save(pendingOrder);
            
            // Pedido 2: CONFIRMED
            Order confirmedOrder = new Order(testCustomer, pizzaria, deliveryAddress);
            confirmedOrder.confirm(); // Chama confirm() quando o status ainda é PENDING
            orderRepository.save(confirmedOrder);
            
            // Pedido 3: PREPARING (se burger house existir)
            if (burgerHouse != null) {
                Order preparingOrder = new Order(testCustomer, burgerHouse, deliveryAddress);
                preparingOrder.setStatus(OrderStatus.PREPARING);
                orderRepository.save(preparingOrder);
            }
            
            // Pedido 4: READY
            Order readyOrder = new Order(testCustomer, pizzaria, deliveryAddress);
            readyOrder.setStatus(OrderStatus.READY);
            orderRepository.save(readyOrder);
        }
        
        // Exibir status disponíveis
        System.out.println("✅ Pedidos com diferentes status criados:");
        for (OrderStatus status : OrderStatus.values()) {
            System.out.println("  - " + status.name() + ": " + status.getDescription());
        }
        System.out.println("✅ 4 pedidos de exemplo com status variados cadastrados");
    }

    private void createSamplePaymentStatuses() {
        // Criar pagamentos adicionais com diferentes status
        User testCustomer = userRepository.findById(1L).orElse(null);
        if (testCustomer != null) {
            // Pagamento 1: PENDING
            Payment pendingPayment = new Payment(PaymentMethod.CREDIT_CARD, new Money(new BigDecimal("45.50")), 1001L);
            pendingPayment.setStatus(PaymentStatus.PENDING);
            pendingPayment.setTransactionId("PENDING-001");
            paymentRepository.save(pendingPayment);
            
            // Pagamento 2: PROCESSING
            Payment processingPayment = new Payment(PaymentMethod.PIX, new Money(new BigDecimal("67.80")), 1002L);
            processingPayment.setStatus(PaymentStatus.PROCESSING);
            processingPayment.setTransactionId("PROC-002");
            paymentRepository.save(processingPayment);
            
            // Pagamento 3: REJECTED
            Payment rejectedPayment = new Payment(PaymentMethod.DEBIT_CARD, new Money(new BigDecimal("25.90")), 1003L);
            rejectedPayment.setStatus(PaymentStatus.REJECTED);
            rejectedPayment.setTransactionId("REJ-003");
            paymentRepository.save(rejectedPayment);
            
            // Pagamento 4: REFUNDED
            Payment refundedPayment = new Payment(PaymentMethod.CREDIT_CARD, new Money(new BigDecimal("89.90")), 1004L);
            refundedPayment.setStatus(PaymentStatus.REFUNDED);
            refundedPayment.setTransactionId("REF-004");
            paymentRepository.save(refundedPayment);
            
            // Pagamento 5: CANCELLED
            Payment cancelledPayment = new Payment(PaymentMethod.CASH, new Money(new BigDecimal("15.00")), 1005L);
            cancelledPayment.setStatus(PaymentStatus.CANCELLED);
            cancelledPayment.setTransactionId("CAN-005");
            paymentRepository.save(cancelledPayment);
        }
        
        // Exibir status disponíveis
        System.out.println("✅ Pagamentos com diferentes status criados:");
        for (PaymentStatus status : PaymentStatus.values()) {
            System.out.println("  - " + status.name() + ": " + status.getDescription());
        }
        System.out.println("✅ 5 pagamentos adicionais com status variados cadastrados");
    }

    private void createSampleDeliveryAreas() {
        // Cadastrar áreas de entrega para restaurantes
        Restaurant pizzaria = restaurantRepository.findById(1L).orElse(null);
        if (pizzaria != null) {
            DeliveryArea area1 = new DeliveryArea("Centro", pizzaria, new Money(new BigDecimal("5.00")));
            area1.setZipCode("01000-000");
            area1.setCity("São Paulo");
            area1.setState("SP");
            deliveryAreaRepository.save(area1);
            
            DeliveryArea area2 = new DeliveryArea("Bela Vista", pizzaria, new Money(new BigDecimal("7.00")));
            area2.setNeighborhood("Bela Vista");
            area2.setCity("São Paulo");
            area2.setState("SP");
            deliveryAreaRepository.save(area2);
            
            System.out.println("✅ Áreas de entrega cadastradas para " + pizzaria.getName());
        }
    }

    private void createSampleNotificationTypes() {
        // Criar notificações adicionais de cada tipo
        User testCustomer = userRepository.findById(1L).orElse(null);
        if (testCustomer != null) {
            // Notificação ORDER_STATUS adicional
            Notification orderStatusNotif = new Notification(
                testCustomer,
                "Pedido Saiu para Entrega",
                "Seu pedido #ORD-123 saiu para entrega e chegará em aproximadamente 15 minutos.",
                NotificationType.ORDER_STATUS,
                123L,
                "ORDER"
            );
            notificationRepository.save(orderStatusNotif);
            
            // Notificação PAYMENT_STATUS adicional
            Notification paymentStatusNotif = new Notification(
                testCustomer,
                "Pagamento Rejeitado",
                "Seu pagamento não foi aprovado. Tente novamente com outro cartão ou método.",
                NotificationType.PAYMENT_STATUS,
                456L,
                "PAYMENT"
            );
            notificationRepository.save(paymentStatusNotif);
            
            // Notificação PROMOTION adicional
            Notification promotionNotif = new Notification(
                testCustomer,
                "Oferta Imperdível!",
                "Hambúrgueres com 30% de desconto hoje! Válido até 23h59.",
                NotificationType.PROMOTION
            );
            notificationRepository.save(promotionNotif);
            
            // Notificação SYSTEM
            Notification systemNotif = new Notification(
                testCustomer,
                "Manutenção Programada",
                "O sistema ficará indisponível das 02h às 04h para manutenção programada.",
                NotificationType.SYSTEM
            );
            notificationRepository.save(systemNotif);
        }
        
        // Exibir tipos disponíveis
        System.out.println("✅ Notificações de cada tipo criadas:");
        for (NotificationType type : NotificationType.values()) {
            System.out.println("  - " + type.name() + ": " + type.getDescription());
        }
        System.out.println("✅ 4 notificações adicionais de diferentes tipos cadastradas");
    }

    private void createSampleReviewTypes() {
        // Criar avaliações adicionais de cada tipo
        User testCustomer = userRepository.findById(1L).orElse(null);
        if (testCustomer != null) {
            Restaurant pizzaria = restaurantRepository.findById(1L).orElse(null);
            Restaurant burgerHouse = restaurantRepository.findById(2L).orElse(null);
            
            if (pizzaria != null) {
                Address deliveryAddress = new Address(
                    "Rua das Avaliações", "300", "Casa",
                    "Centro", "São Paulo", "SP", "01000-000", "Próximo ao parque"
                );
                
                // Pedido para avaliação de restaurante
                Order restaurantOrder = new Order(testCustomer, pizzaria, deliveryAddress);
                restaurantOrder = orderRepository.save(restaurantOrder);
                
                // Avaliação RESTAURANT adicional
                Review restaurantReview2 = new Review(
                    restaurantOrder,
                    testCustomer,
                    ReviewType.RESTAURANT,
                    3,
                    "Pizza boa, mas chegou um pouco fria. Atendimento cordial."
                );
                reviewRepository.save(restaurantReview2);
                
                // Avaliação RESTAURANT com nota baixa
                Review restaurantReview3 = new Review(
                    restaurantOrder,
                    testCustomer,
                    ReviewType.RESTAURANT,
                    2,
                    "Demora excessiva na entrega e pizza veio errada. Decepcionante."
                );
                reviewRepository.save(restaurantReview3);
            }
            
            if (burgerHouse != null) {
                Address deliveryAddress2 = new Address(
                    "Av. das Reviews", "400", "Apto 5",
                    "Vila Madalena", "São Paulo", "SP", "05406-000", "Próximo ao shopping"
                );
                
                // Pedido para avaliação de entregador
                Order deliveryOrder = new Order(testCustomer, burgerHouse, deliveryAddress2);
                deliveryOrder = orderRepository.save(deliveryOrder);
                
                // Avaliação DELIVERY_PERSON adicional
                Review deliveryReview2 = new Review(
                    deliveryOrder,
                    testCustomer,
                    ReviewType.DELIVERY_PERSON,
                    5,
                    "Entregador muito atencioso! Chegou super rápido e educado."
                );
                reviewRepository.save(deliveryReview2);
                
                // Avaliação DELIVERY_PERSON com nota média
                Review deliveryReview3 = new Review(
                    deliveryOrder,
                    testCustomer,
                    ReviewType.DELIVERY_PERSON,
                    3,
                    "Entrega no prazo, mas entregador um pouco apressado."
                );
                reviewRepository.save(deliveryReview3);
            }
        }
        
        // Exibir tipos disponíveis
        System.out.println("✅ Avaliações de cada tipo criadas:");
        for (ReviewType type : ReviewType.values()) {
            System.out.println("  - " + type.name() + ": " + type.getDescription());
        }
        System.out.println("✅ 4 avaliações adicionais de diferentes tipos cadastradas");
    }

    private void createSampleNotifications() {
        // Criar notificações de teste
        User testCustomer = userRepository.findById(1L).orElse(null);
        if (testCustomer != null) {
            // Notificação 1: Pedido confirmado
            Notification orderNotification = new Notification(
                testCustomer,
                "Pedido Confirmado",
                "Seu pedido foi confirmado e está sendo preparado. Tempo estimado: 30 minutos.",
                NotificationType.ORDER_STATUS,
                1L,
                "ORDER"
            );
            notificationRepository.save(orderNotification);

            // Notificação 2: Pagamento aprovado
            Notification paymentNotification = new Notification(
                testCustomer,
                "Pagamento Aprovado",
                "Seu pagamento de R$ 50,00 via PIX foi aprovado com sucesso!",
                NotificationType.PAYMENT_STATUS,
                1L,
                "PAYMENT"
            );
            notificationRepository.save(paymentNotification);

            // Notificação 3: Promoção
            Notification promotionNotification = new Notification(
                testCustomer,
                "Promoção Especial",
                "Pizza Margherita com 20% de desconto! Aproveite nossa oferta especial.",
                NotificationType.PROMOTION
            );
            notificationRepository.save(promotionNotification);
        }
        System.out.println("✅ 3 notificações de teste cadastradas");
    }

    private void createSampleReviews() {
        // Criar avaliações de teste
        User testCustomer = userRepository.findById(1L).orElse(null);
        if (testCustomer != null) {
            // Criar pedido temporário para as avaliações (mockado)
            Restaurant pizzaria = restaurantRepository.findById(1L).orElse(null);
            if (pizzaria != null) {
                Address deliveryAddress = new Address(
                    "Rua dos Testes", "100", "Apt 10",
                    "Centro", "São Paulo", "SP", "01000-000", "Próximo ao shopping"
                );
                
                Order testOrder = new Order(testCustomer, pizzaria, deliveryAddress);
                testOrder = orderRepository.save(testOrder);

                // Avaliação 1: Restaurante
                Review restaurantReview = new Review(
                    testOrder,
                    testCustomer,
                    ReviewType.RESTAURANT,
                    5,
                    "Excelente pizza! Massa crocante e ingredientes frescos. Recomendo!"
                );
                reviewRepository.save(restaurantReview);

                // Avaliação 2: Entregador (simulado)
                Review deliveryReview = new Review(
                    testOrder,
                    testCustomer,
                    ReviewType.DELIVERY_PERSON,
                    4,
                    "Entregador pontual e educado. Chegou no tempo estimado."
                );
                reviewRepository.save(deliveryReview);
            }
        }
        System.out.println("✅ 2 avaliações de teste cadastradas (restaurante e entregador)");
    }

    private void createSampleOrdersAndItems() {
        // Criar pedido e itens de pedido de teste
        User testCustomer = userRepository.findById(1L).orElse(null);
        Restaurant pizzaria = restaurantRepository.findById(1L).orElse(null);
        Product pizza1 = productRepository.findById(1L).orElse(null);
        Product pizza2 = productRepository.findById(2L).orElse(null);

        if (testCustomer != null && pizzaria != null && pizza1 != null) {
            // Endereço de entrega
            Address deliveryAddress = new Address(
                "Rua dos Testes", "100", "Apt 10",
                "Centro", "São Paulo", "SP", "01000-000", "Próximo ao shopping"
            );

            // Criar pedido
            Order order = new Order(testCustomer, pizzaria, deliveryAddress);
            order.setStatus(OrderStatus.CONFIRMED);
            Order savedOrder = orderRepository.save(order);

            // Item 1: Pizza Margherita
            OrderItem item1 = new OrderItem(savedOrder, pizza1, 2, "Sem cebola, por favor");
            orderItemRepository.save(item1);

            // Item 2: Pizza Calabresa (se existir)
            if (pizza2 != null) {
                OrderItem item2 = new OrderItem(savedOrder, pizza2, 1, "Massa fina");
                orderItemRepository.save(item2);
            }

            // Atualizar totais do pedido
            savedOrder.calculateTotal();
            orderRepository.save(savedOrder);

            System.out.println("✅ Pedido de teste criado:");
            System.out.println("  - ID: " + savedOrder.getId() + " | Código: " + savedOrder.getCode());
            System.out.println("  - Status: " + savedOrder.getStatus());
            System.out.println("  - Itens: " + (pizza2 != null ? "2" : "1") + " produtos");
        } else {
            System.out.println("⚠️ Não foi possível criar pedido de teste - dados necessários não encontrados");
        }
    }

    private void createSampleBusinessHours() {
        // Cadastrar áreas de entrega adicionais para outros restaurantes
        Restaurant burgerHouse = restaurantRepository.findById(2L).orElse(null);
        Restaurant sushiMaster = restaurantRepository.findById(3L).orElse(null);
        
        // Áreas para Burger House
        if (burgerHouse != null) {
            DeliveryArea area3 = new DeliveryArea("Vila Madalena", burgerHouse, new Money(new BigDecimal("8.00")));
            area3.setNeighborhood("Vila Madalena");
            area3.setZipCode("05406-000");
            area3.setCity("São Paulo");
            area3.setState("SP");
            area3.setEstimatedTimeInMinutes(25);
            deliveryAreaRepository.save(area3);
            
            DeliveryArea area4 = new DeliveryArea("Pinheiros", burgerHouse, new Money(new BigDecimal("9.50")));
            area4.setNeighborhood("Pinheiros");
            area4.setZipCode("05422-000");
            area4.setCity("São Paulo");
            area4.setState("SP");
            area4.setEstimatedTimeInMinutes(30);
            deliveryAreaRepository.save(area4);
            
            System.out.println("✅ Áreas de entrega cadastradas para " + burgerHouse.getName());
        }
        
        // Áreas para Sushi Master (mesmo fechado, para demonstração)
        if (sushiMaster != null) {
            DeliveryArea area5 = new DeliveryArea("Liberdade", sushiMaster, new Money(new BigDecimal("6.00")));
            area5.setNeighborhood("Liberdade");
            area5.setZipCode("01503-000");
            area5.setCity("São Paulo");
            area5.setState("SP");
            area5.setEstimatedTimeInMinutes(20);
            deliveryAreaRepository.save(area5);
            
            DeliveryArea area6 = new DeliveryArea("Aclimação", sushiMaster, new Money(new BigDecimal("7.50")));
            area6.setNeighborhood("Aclimação");
            area6.setZipCode("01531-000");
            area6.setCity("São Paulo");
            area6.setState("SP");
            area6.setEstimatedTimeInMinutes(35);
            deliveryAreaRepository.save(area6);
            
            System.out.println("✅ Áreas de entrega cadastradas para " + sushiMaster.getName());
        }
        
        // Configurar horários básicos para restaurantes
        Restaurant pizzaria = restaurantRepository.findById(1L).orElse(null);
        if (pizzaria != null) {
            pizzaria.setOpen(true);
            pizzaria.setActive(true);
            restaurantRepository.save(pizzaria);
        }
        
        if (burgerHouse != null) {
            burgerHouse.setOpen(true);
            burgerHouse.setActive(true);
            restaurantRepository.save(burgerHouse);
        }
        
        // Mostrar informações detalhadas sobre horários
        System.out.println("✅ Horários de funcionamento e áreas de entrega configurados:");
        System.out.println("  - Pizzaria do João: Centro, Bela Vista (5-7 reais, 15-25 min)");
        System.out.println("  - Burger House: Vila Madalena, Pinheiros (8-9.50 reais, 25-30 min)");
        System.out.println("  - Sushi Master: Liberdade, Aclimação (6-7.50 reais, 20-35 min)");
        
        System.out.println("✅ Dias da semana para configuração de horários:");
        for (DayOfWeek day : DayOfWeek.values()) {
            System.out.println("  - " + day.name() + " (" + day.getValue() + ")");
        }
        
        System.out.println("✅ 6 áreas de entrega criadas com taxas e tempos específicos");
    }

    private void createSampleRestaurants() {
        // Buscar categorias criadas
        RestaurantCategory italiana = restaurantCategoryRepository.findByName("Italiana").orElse(null);
        RestaurantCategory hamburgueria = restaurantCategoryRepository.findByName("Hamburgueria").orElse(null);
        RestaurantCategory japonesa = restaurantCategoryRepository.findByName("Japonesa").orElse(null);
        RestaurantCategory cafeteria = restaurantCategoryRepository.findByName("Cafeteria").orElse(null);
        
        // Restaurante 1: Pizzaria do João
        Address address1 = new Address(
                "Rua das Flores", "123", "Apto 201",
                "Centro", "São Paulo", "SP", "01234-567", "Próximo ao metrô"
        );
        
        Restaurant restaurant1 = new Restaurant(
                "Pizzaria do João",
                "Pizzas artesanais com ingredientes frescos e massa tradicional italiana",
                "12.345.678/0001-90",
                "(11) 98765-4321",
                address1,
                null // owner será null por enquanto
        );
        restaurant1.setLogo("https://example.com/logo-pizzaria.png");
        restaurant1.setOpen(true);
        if (italiana != null) {
            restaurant1.addCategory(italiana);
        }

        // Restaurante 2: Burger House
        Address address2 = new Address(
                "Avenida Paulista", "1000", null,
                "Bela Vista", "São Paulo", "SP", "01310-100", "Em frente ao MASP"
        );
        
        Restaurant restaurant2 = new Restaurant(
                "Burger House",
                "Hambúrgueres gourmet com carnes selecionadas e pães artesanais",
                "98.765.432/0001-10",
                "(11) 87654-3210",
                address2,
                null // owner será null por enquanto
        );
        restaurant2.setLogo("https://example.com/logo-burger.png");
        restaurant2.setOpen(true);
        if (hamburgueria != null) {
            restaurant2.addCategory(hamburgueria);
        }

        // Restaurante 3: Sushi Master (fechado)
        Address address3 = new Address(
                "Rua da Liberdade", "500", "Loja 15",
                "Liberdade", "São Paulo", "SP", "01503-001", "Próximo à estação Liberdade"
        );
        
        Restaurant restaurant3 = new Restaurant(
                "Sushi Master",
                "Culinária japonesa autêntica com peixes frescos importados diariamente",
                "11.222.333/0001-44",
                "(11) 99988-7766",
                address3,
                null // owner será null por enquanto
        );
        restaurant3.setLogo("https://example.com/logo-sushi.png");
        restaurant3.setOpen(false); // Restaurante fechado no momento
        if (japonesa != null) {
            restaurant3.addCategory(japonesa);
        }

        // Restaurante 4: Café & Cia (inativo)
        Address address4 = new Address(
                "Rua Augusta", "200", "Térreo",
                "Consolação", "São Paulo", "SP", "01305-000", "Esquina com Rua da Consolação"
        );
        
        Restaurant restaurant4 = new Restaurant(
                "Café & Cia",
                "Cafés especiais, doces caseiros e sanduíches naturais",
                "55.666.777/0001-88",
                "(11) 77766-5544",
                address4,
                null // owner será null por enquanto
        );
        restaurant4.setLogo("https://example.com/logo-cafe.png");
        restaurant4.setActive(false); // Restaurante inativo
        if (cafeteria != null) {
            restaurant4.addCategory(cafeteria);
        }

        // Salvar no banco
        restaurantRepository.save(restaurant1);
        restaurantRepository.save(restaurant2);
        restaurantRepository.save(restaurant3);
        restaurantRepository.save(restaurant4);

        System.out.println("✅ Dados iniciais de restaurantes criados com sucesso!");
        System.out.println("- 2 restaurantes ativos e abertos");
        System.out.println("- 1 restaurante ativo mas fechado");
        System.out.println("- 1 restaurante inativo");
    }

    private void createSampleProductCategories() {
        // Buscar restaurantes criados
        Restaurant pizzaria = restaurantRepository.findById(1L).orElse(null);
        Restaurant burgerHouse = restaurantRepository.findById(2L).orElse(null);
        Restaurant sushiMaster = restaurantRepository.findById(3L).orElse(null);

        if (pizzaria != null) {
            // Categorias para Pizzaria do João
            ProductCategory pizzasCategory = new ProductCategory("Pizzas", "Deliciosas pizzas artesanais", pizzaria);
            pizzasCategory.setDisplayOrder(1);
            productCategoryRepository.save(pizzasCategory);

            ProductCategory bebidasCategory = new ProductCategory("Bebidas", "Refrigerantes e sucos", pizzaria);
            bebidasCategory.setDisplayOrder(2);
            productCategoryRepository.save(bebidasCategory);
        }

        if (burgerHouse != null) {
            // Categorias para Burger House
            ProductCategory burgersCategory = new ProductCategory("Hambúrgueres", "Hambúrgueres gourmet", burgerHouse);
            burgersCategory.setDisplayOrder(1);
            productCategoryRepository.save(burgersCategory);

            ProductCategory acompanhamentosCategory = new ProductCategory("Acompanhamentos", "Batatas e porções", burgerHouse);
            acompanhamentosCategory.setDisplayOrder(2);
            productCategoryRepository.save(acompanhamentosCategory);
        }

        if (sushiMaster != null) {
            // Categorias para Sushi Master
            ProductCategory sushiCategory = new ProductCategory("Sushi", "Peças de sushi fresco", sushiMaster);
            sushiCategory.setDisplayOrder(1);
            productCategoryRepository.save(sushiCategory);

            ProductCategory sashimiCategory = new ProductCategory("Sashimi", "Fatias de peixe fresco", sushiMaster);
            sashimiCategory.setDisplayOrder(2);
            productCategoryRepository.save(sashimiCategory);
        }

        System.out.println("✅ Categorias de produtos criadas com sucesso!");
        System.out.println("- 2 categorias para cada restaurante ativo");
    }

    private void createRestaurantCategories() {
        // Criar categorias de restaurante
        RestaurantCategory italiana = new RestaurantCategory("Italiana", "Restaurantes especializados em culinária italiana");
        italiana.setImageUrl("https://example.com/categoria-italiana.jpg");
        restaurantCategoryRepository.save(italiana);

        RestaurantCategory hamburgueria = new RestaurantCategory("Hamburgueria", "Hambúrgueres gourmet e fast food");
        hamburgueria.setImageUrl("https://example.com/categoria-hamburgueria.jpg");
        restaurantCategoryRepository.save(hamburgueria);

        RestaurantCategory japonesa = new RestaurantCategory("Japonesa", "Sushi, sashimi e culinária oriental");
        japonesa.setImageUrl("https://example.com/categoria-japonesa.jpg");
        restaurantCategoryRepository.save(japonesa);

        RestaurantCategory cafeteria = new RestaurantCategory("Cafeteria", "Cafés especiais, doces e lanches");
        cafeteria.setImageUrl("https://example.com/categoria-cafeteria.jpg");
        restaurantCategoryRepository.save(cafeteria);

        System.out.println("✅ Categorias de restaurante criadas com sucesso!");
        System.out.println("- Italiana, Hamburgueria, Japonesa, Cafeteria");
    }

    private void createSampleUsers() {
        // Criar usuário de teste padrão para os testes do Postman
        Address customerAddress = new Address(
                "Rua dos Testes", "100", "Apt 10",
                "Centro", "São Paulo", "SP", "01000-000", "Próximo ao shopping"
        );
        
        User testCustomer = new User();
        testCustomer.setName("Cliente Teste");
        testCustomer.setEmail("teste@email.com");
        testCustomer.setPassword("senha123");
        testCustomer.setPhone("(11) 99999-0000");
        testCustomer.setAddress(customerAddress);
        testCustomer.setUserType(UserType.CUSTOMER);
        userRepository.save(testCustomer);

        System.out.println("✅ Usuário de teste criado com sucesso!");
        System.out.println("- Cliente ID: " + testCustomer.getId() + " (teste@email.com)");
    }

    private void createSampleProducts() {
        System.out.println("🍕 Iniciando criação de produtos de teste...");
        
        // Verificar se há restaurantes
        long restaurantCount = restaurantRepository.count();
        System.out.println("DEBUG: Total de restaurantes no banco: " + restaurantCount);
        
        // Verificar se há categorias de produto
        long categoryCount = productCategoryRepository.count();
        System.out.println("DEBUG: Total de categorias de produto no banco: " + categoryCount);
        
        if (restaurantCount == 0) {
            System.out.println("⚠️ Nenhum restaurante encontrado. Pulando criação de produtos.");
            return;
        }
        
        if (categoryCount == 0) {
            System.out.println("⚠️ Nenhuma categoria de produto encontrada. Pulando criação de produtos.");
            return;
        }
        
        // Buscar restaurantes
        Restaurant pizzaria = restaurantRepository.findById(1L).orElse(null);
        Restaurant burgerHouse = restaurantRepository.findById(2L).orElse(null);
        
        System.out.println("DEBUG: Pizzaria encontrada: " + (pizzaria != null ? pizzaria.getName() : "null"));
        System.out.println("DEBUG: BurgerHouse encontrado: " + (burgerHouse != null ? burgerHouse.getName() : "null"));
        
        int produtosCriados = 0;
        
        if (pizzaria != null) {
            // Buscar categoria de pizzas manualmente
            ProductCategory pizzasCategory = productCategoryRepository.findAll().stream()
                .filter(cat -> cat.getRestaurant().getId().equals(pizzaria.getId()) && cat.getName().equals("Pizzas"))
                .findFirst().orElse(null);
            
            System.out.println("DEBUG: Categoria de pizzas encontrada: " + (pizzasCategory != null ? pizzasCategory.getName() : "null"));
            
            if (pizzasCategory != null) {
                try {
                    // Produto 1: Pizza Margherita
                    Product pizza1 = new Product();
                    pizza1.setName("Pizza Margherita");
                    pizza1.setDescription("Pizza clássica com molho de tomate, mozzarella e manjericão fresco");
                    pizza1.setPrice(new Money(new BigDecimal("32.90")));
                    pizza1.setImage("https://exemplo.com/pizza-margherita.jpg");
                    pizza1.setPreparationTimeInMinutes(20);
                    pizza1.setRestaurant(pizzaria);
                    pizza1.setCategory(pizzasCategory);
                    pizza1.setAvailable(true);
                    pizza1.setActive(true);
                    Product savedPizza1 = productRepository.save(pizza1);
                    produtosCriados++;
                    System.out.println("✅ Pizza Margherita criada com ID: " + savedPizza1.getId());

                    // Produto 2: Pizza Calabresa
                    Product pizza2 = new Product();
                    pizza2.setName("Pizza Calabresa");
                    pizza2.setDescription("Pizza tradicional com calabresa fatiada, cebola e mozzarella");
                    pizza2.setPrice(new Money(new BigDecimal("35.90")));
                    pizza2.setImage("https://exemplo.com/pizza-calabresa.jpg");
                    pizza2.setPreparationTimeInMinutes(25);
                    pizza2.setRestaurant(pizzaria);
                    pizza2.setCategory(pizzasCategory);
                    pizza2.setAvailable(true);
                    pizza2.setActive(true);
                    Product savedPizza2 = productRepository.save(pizza2);
                    produtosCriados++;
                    System.out.println("✅ Pizza Calabresa criada com ID: " + savedPizza2.getId());
                    
                    // Produto 3: Pizza Quatro Queijos
                    Product pizza3 = new Product();
                    pizza3.setName("Pizza Quatro Queijos");
                    pizza3.setDescription("Pizza com mozzarella, parmesão, gorgonzola e provolone");
                    pizza3.setPrice(new Money(new BigDecimal("42.90")));
                    pizza3.setImage("https://exemplo.com/pizza-quatro-queijos.jpg");
                    pizza3.setPreparationTimeInMinutes(22);
                    pizza3.setRestaurant(pizzaria);
                    pizza3.setCategory(pizzasCategory);
                    pizza3.setAvailable(true);
                    pizza3.setActive(true);
                    Product savedPizza3 = productRepository.save(pizza3);
                    produtosCriados++;
                    System.out.println("✅ Pizza Quatro Queijos criada com ID: " + savedPizza3.getId());
                } catch (Exception e) {
                    System.out.println("❌ Erro ao criar pizzas: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("⚠️ Categoria 'Pizzas' não encontrada para o restaurante: " + pizzaria.getName());
            }
            
            // Buscar categoria de bebidas para pizzaria
            ProductCategory bebidasCategory = productCategoryRepository.findAll().stream()
                .filter(cat -> cat.getRestaurant().getId().equals(pizzaria.getId()) && cat.getName().equals("Bebidas"))
                .findFirst().orElse(null);
                
            if (bebidasCategory != null) {
                try {
                    // Produto 4: Coca-Cola
                    Product cocaCola = new Product();
                    cocaCola.setName("Coca-Cola 350ml");
                    cocaCola.setDescription("Refrigerante Coca-Cola lata 350ml gelada");
                    cocaCola.setPrice(new Money(new BigDecimal("5.50")));
                    cocaCola.setImage("https://exemplo.com/coca-cola.jpg");
                    cocaCola.setPreparationTimeInMinutes(2);
                    cocaCola.setRestaurant(pizzaria);
                    cocaCola.setCategory(bebidasCategory);
                    cocaCola.setAvailable(true);
                    cocaCola.setActive(true);
                    Product savedCoca = productRepository.save(cocaCola);
                    produtosCriados++;
                    System.out.println("✅ Coca-Cola criada com ID: " + savedCoca.getId());
                } catch (Exception e) {
                    System.out.println("❌ Erro ao criar bebida: " + e.getMessage());
                }
            }
        }

        if (burgerHouse != null) {
            // Buscar categoria de hambúrgueres manualmente
            ProductCategory burgersCategory = productCategoryRepository.findAll().stream()
                .filter(cat -> cat.getRestaurant().getId().equals(burgerHouse.getId()) && cat.getName().equals("Hambúrgueres"))
                .findFirst().orElse(null);
            
            System.out.println("DEBUG: Categoria de hambúrgueres encontrada: " + (burgersCategory != null ? burgersCategory.getName() : "null"));
            
            if (burgersCategory != null) {
                try {
                    // Produto 5: X-Burguer Clássico
                    Product burger1 = new Product();
                    burger1.setName("X-Burguer Clássico");
                    burger1.setDescription("Hambúrguer com carne 180g, queijo, alface, tomate e molho especial");
                    burger1.setPrice(new Money(new BigDecimal("22.90")));
                    burger1.setImage("https://exemplo.com/burger-classico.jpg");
                    burger1.setPreparationTimeInMinutes(15);
                    burger1.setRestaurant(burgerHouse);
                    burger1.setCategory(burgersCategory);
                    burger1.setAvailable(true);
                    burger1.setActive(true);
                    Product savedBurger1 = productRepository.save(burger1);
                    produtosCriados++;
                    System.out.println("✅ X-Burguer Clássico criado com ID: " + savedBurger1.getId());

                    // Produto 6: X-Bacon
                    Product burger2 = new Product();
                    burger2.setName("X-Bacon Deluxe");
                    burger2.setDescription("Hambúrguer com carne 180g, bacon crocante, queijo cheddar e molho barbecue");
                    burger2.setPrice(new Money(new BigDecimal("28.90")));
                    burger2.setImage("https://exemplo.com/x-bacon.jpg");
                    burger2.setPreparationTimeInMinutes(18);
                    burger2.setRestaurant(burgerHouse);
                    burger2.setCategory(burgersCategory);
                    burger2.setAvailable(true);
                    burger2.setActive(true);
                    Product savedBurger2 = productRepository.save(burger2);
                    produtosCriados++;
                    System.out.println("✅ X-Bacon Deluxe criado com ID: " + savedBurger2.getId());
                } catch (Exception e) {
                    System.out.println("❌ Erro ao criar hambúrgueres: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            // Buscar categoria de acompanhamentos
            ProductCategory acompanhamentosCategory = productCategoryRepository.findAll().stream()
                .filter(cat -> cat.getRestaurant().getId().equals(burgerHouse.getId()) && cat.getName().equals("Acompanhamentos"))
                .findFirst().orElse(null);
                
            if (acompanhamentosCategory != null) {
                try {
                    // Produto 7: Batata Frita
                    Product batata = new Product();
                    batata.setName("Batata Frita Grande");
                    batata.setDescription("Porção grande de batata frita sequinha e dourada");
                    batata.setPrice(new Money(new BigDecimal("12.90")));
                    batata.setImage("https://exemplo.com/batata-frita.jpg");
                    batata.setPreparationTimeInMinutes(8);
                    batata.setRestaurant(burgerHouse);
                    batata.setCategory(acompanhamentosCategory);
                    batata.setAvailable(true);
                    batata.setActive(true);
                    Product savedBatata = productRepository.save(batata);
                    produtosCriados++;
                    System.out.println("✅ Batata Frita Grande criada com ID: " + savedBatata.getId());
                } catch (Exception e) {
                    System.out.println("❌ Erro ao criar acompanhamento: " + e.getMessage());
                }
            }
        }

        System.out.println("✅ Criação de produtos finalizada!");
        System.out.println("📊 Total de produtos criados: " + produtosCriados);
        if (produtosCriados > 0) {
            System.out.println("🍕 Pizzaria do João: 3 pizzas + 1 bebida");
            System.out.println("🍔 Burger House: 2 hambúrgueres + 1 acompanhamento");
        }
    }
}
