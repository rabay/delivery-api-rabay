package com.deliverytech.deliveryapi.service;

import com.deliverytech.deliveryapi.domain.model.*;
import com.deliverytech.deliveryapi.domain.repository.*;
import com.deliverytech.deliveryapi.dto.CreateOrderRequest;
import com.deliverytech.deliveryapi.dto.OrderDTO;
import com.deliverytech.deliveryapi.dto.OrderItemRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Serviço para gerenciamento de pedidos
 * Implementa regras de negócio críticas para o fluxo de delivery
 */
@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository,
                       UserRepository userRepository,
                       RestaurantRepository restaurantRepository,
                       ProductRepository productRepository,
                       OrderItemRepository orderItemRepository,
                       ProductService productService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.productService = productService;
    }

    /**
     * Cria um novo pedido com validações de negócio
     */
    public OrderDTO createOrder(CreateOrderRequest request) {
        // Validar cliente
        User customer = userRepository.findById(request.customerId())
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        
        if (customer.getUserType() != UserType.CUSTOMER || !customer.isActive()) {
            throw new IllegalArgumentException("Cliente inválido ou inativo");
        }

        // Validar restaurante
        Restaurant restaurant = restaurantRepository.findById(request.restaurantId())
            .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));
        
        if (!restaurant.isActive() || !restaurant.isOpen()) {
            throw new IllegalArgumentException("Restaurante não está disponível para pedidos");
        }

        // Validar itens do pedido
        if (request.items() == null || request.items().isEmpty()) {
            throw new IllegalArgumentException("Pedido deve ter pelo menos um item");
        }

        // Criar pedido
        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setDeliveryAddress(new Address(
            request.deliveryAddress().street(),
            request.deliveryAddress().number(),
            request.deliveryAddress().complement(),
            request.deliveryAddress().neighborhood(),
            request.deliveryAddress().city(),
            request.deliveryAddress().state(),
            request.deliveryAddress().postalCode(),
            request.deliveryAddress().reference()
        ));
        order.setDeliveryFee(restaurant.getDeliveryFee());

        // Calcular valores
        BigDecimal subtotal = BigDecimal.ZERO;
        int totalPreparationTime = 0;

        // Salvar pedido primeiro para obter ID
        Order savedOrder = orderRepository.save(order);

        // Processar itens
        for (OrderItemRequest itemRequest : request.items()) {
            // Validar produto
            Product product = productRepository.findById(itemRequest.productId())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + itemRequest.productId()));
            
            if (!productService.isProductAvailableForOrder(itemRequest.productId())) {
                throw new IllegalArgumentException("Produto não disponível: " + product.getName());
            }

            if (!product.getRestaurant().getId().equals(request.restaurantId())) {
                throw new IllegalArgumentException("Produto não pertence ao restaurante selecionado");
            }

            // Validar quantidade
            if (itemRequest.quantity() <= 0) {
                throw new IllegalArgumentException("Quantidade deve ser maior que zero");
            }

            // Criar item do pedido
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.quantity());
            orderItem.setNotes(itemRequest.observations());

            orderItemRepository.save(orderItem);

            // Calcular totais
            subtotal = subtotal.add(orderItem.getSubtotal().getAmount());
            
            // Calcular tempo de preparo (usar o maior tempo entre os produtos)
            if (product.getPreparationTimeInMinutes() != null) {
                totalPreparationTime = Math.max(totalPreparationTime, product.getPreparationTimeInMinutes());
            }
        }

        // Validar valor mínimo do pedido
        if (subtotal.compareTo(restaurant.getMinimumOrderValue().getAmount()) < 0) {
            throw new IllegalArgumentException(String.format(
                "Valor mínimo do pedido é R$ %.2f", 
                restaurant.getMinimumOrderValue().getAmount()
            ));
        }

        // Calcular valores finais
        BigDecimal deliveryFee = restaurant.getDeliveryFee().getAmount();
        BigDecimal totalValue = subtotal.add(deliveryFee);

        // Atualizar pedido com valores calculados (reutilizar calculateTotal())
        savedOrder.calculateTotal();

        Order finalOrder = orderRepository.save(savedOrder);
        return OrderDTO.from(finalOrder);
    }

    /**
     * Busca pedido por ID
     */
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
        
        return OrderDTO.from(order);
    }

    /**
     * Lista pedidos de um cliente
     */
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByCustomer(Long customerId) {
        if (!userRepository.existsById(customerId)) {
            throw new IllegalArgumentException("Cliente não encontrado");
        }

        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId, Pageable.unpaged())
            .stream()
            .map(OrderDTO::from)
            .collect(Collectors.toList());
    }

    /**
     * Lista pedidos de um restaurante
     */
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByRestaurant(Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new IllegalArgumentException("Restaurante não encontrado");
        }

        return orderRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId, Pageable.unpaged())
            .stream()
            .map(OrderDTO::from)
            .collect(Collectors.toList());
    }

    /**
     * Altera o status do pedido com validação de transições
     */
    public OrderDTO updateOrderStatus(Long id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

        // Validar transição de status
        validateStatusTransition(order.getStatus(), newStatus);

        order.setStatus(newStatus);
        
        // Definir timestamps específicos baseados no status
        LocalDateTime now = LocalDateTime.now();
        switch (newStatus) {
            case CONFIRMED -> order.confirm();
            case PREPARING -> order.startPreparation();
            case READY -> order.finishPreparation();
            case OUT_FOR_DELIVERY -> {
                // Para simplificar, vamos usar null como deliveryPerson por enquanto
                // TODO: Implementar seleção de entregador
                order.startDelivery(null);
            }
            case DELIVERED -> order.deliver();
            case CANCELLED -> order.cancel();
        }

        Order savedOrder = orderRepository.save(order);
        return OrderDTO.from(savedOrder);
    }

    /**
     * Cancela um pedido com validação de regras de negócio
     */
    public OrderDTO cancelOrder(Long id, String reason) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

        // Verificar se o pedido pode ser cancelado usando método da entidade
        if (!order.canCancel()) {
            throw new IllegalArgumentException("Pedido não pode ser cancelado no status atual: " + order.getStatus());
        }

        order.cancel();

        Order savedOrder = orderRepository.save(order);
        return OrderDTO.from(savedOrder);
    }

    /**
     * Lista pedidos por status
     */
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status)
            .stream()
            .map(OrderDTO::from)
            .collect(Collectors.toList());
    }

    /**
     * Lista pedidos ativos (não finalizados) para monitoramento em tempo real
     */
    @Transactional(readOnly = true)
    public List<OrderDTO> getActiveOrders() {
        return orderRepository.findActiveOrders()
            .stream()
            .map(OrderDTO::from)
            .collect(Collectors.toList());
    }

    /**
     * Calcula estatísticas do pedido para métricas de negócio
     */
    @Transactional(readOnly = true)
    public OrderStatisticsDTO getOrderStatistics(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

        // Calcular tempo total se entregue
        Long totalMinutes = null;
        if (order.getDeliveryEndDate() != null) {
            totalMinutes = java.time.Duration.between(order.getCreatedAt(), order.getDeliveryEndDate()).toMinutes();
        }

        // Contar itens via repository (já que getItems() não está implementado)
        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);

        return new OrderStatisticsDTO(
            order.getId(),
            order.getCode(),
            items.size(),
            order.getTotal().getAmount(),
            totalMinutes
        );
    }

    /**
     * Valida se a transição de status é permitida (critical para delivery)
     */
    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        boolean isValidTransition = switch (currentStatus) {
            case PENDING -> newStatus == OrderStatus.CONFIRMED || newStatus == OrderStatus.CANCELLED;
            case CONFIRMED -> newStatus == OrderStatus.PREPARING || newStatus == OrderStatus.CANCELLED;
            case PREPARING -> newStatus == OrderStatus.READY || newStatus == OrderStatus.CANCELLED;
            case READY -> newStatus == OrderStatus.OUT_FOR_DELIVERY || newStatus == OrderStatus.CANCELLED;
            case OUT_FOR_DELIVERY -> newStatus == OrderStatus.DELIVERED;
            case DELIVERED, CANCELLED -> false; // Estados finais
        };

        if (!isValidTransition) {
            throw new IllegalArgumentException(
                String.format("Não é possível alterar status de %s para %s", currentStatus, newStatus)
            );
        }
    }

    /**
     * DTO para estatísticas do pedido
     */
    public record OrderStatisticsDTO(
        Long orderId,
        String orderCode,
        int itemCount,
        BigDecimal totalValue,
        Long totalDeliveryTimeMinutes
    ) {}

    /**
     * Atualiza status do pedido usando String
     */
    public OrderDTO updateOrderStatus(Long id, String status) {
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            return updateOrderStatus(id, orderStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status inválido: " + status);
        }
    }

    /**
     * Cancela um pedido sem motivo específico
     */
    public OrderDTO cancelOrder(Long id) {
        return cancelOrder(id, "Cancelado pelo usuário");
    }

    /**
     * Lista todos os pedidos
     */
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
            .stream()
            .map(OrderDTO::from)
            .collect(Collectors.toList());
    }

    /**
     * Lista pedidos por status usando String
     */
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByStatus(String status) {
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            return getOrdersByStatus(orderStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status inválido: " + status);
        }
    }
}
