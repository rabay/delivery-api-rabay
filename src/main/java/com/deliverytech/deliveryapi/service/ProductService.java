package com.deliverytech.deliveryapi.service;

import com.deliverytech.deliveryapi.domain.model.Money;
import com.deliverytech.deliveryapi.domain.model.Product;
import com.deliverytech.deliveryapi.domain.model.ProductCategory;
import com.deliverytech.deliveryapi.domain.model.Restaurant;
import com.deliverytech.deliveryapi.domain.repository.ProductRepository;
import com.deliverytech.deliveryapi.domain.repository.RestaurantRepository;
import com.deliverytech.deliveryapi.domain.repository.ProductCategoryRepository;
import com.deliverytech.deliveryapi.dto.CreateProductRequest;
import com.deliverytech.deliveryapi.dto.ProductDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para gerenciamento de produtos
 * Implementa regras de negócio para produtos de delivery
 */
@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final RestaurantRepository restaurantRepository;
    private final ProductCategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                         RestaurantRepository restaurantRepository,
                         ProductCategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.restaurantRepository = restaurantRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Cadastra um novo produto para um restaurante
     */
    public ProductDTO createProduct(CreateProductRequest request) {
        // Validar se o restaurante existe e está ativo
        Restaurant restaurant = restaurantRepository.findById(request.restaurantId())
            .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));
        
        if (!restaurant.isActive()) {
            throw new IllegalArgumentException("Não é possível adicionar produtos a um restaurante inativo");
        }

        // Validar preço
        if (request.price() == null || request.price().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }

        // Buscar categoria se fornecida
        ProductCategory category = null;
        if (request.categoryId() != null) {
            category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));
        }

        // Criar produto
        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(new Money(request.price()));
        product.setImage(request.image() != null ? request.image() : "");
        product.setPreparationTimeInMinutes(request.preparationTimeInMinutes());
        product.setRestaurant(restaurant);
        product.setCategory(category);
        product.setActive(true);
        product.setAvailable(true);

        Product savedProduct = productRepository.save(product);
        return ProductDTO.from(savedProduct);
    }

    /**
     * Lista produtos de um restaurante
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsByRestaurant(Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new IllegalArgumentException("Restaurante não encontrado");
        }

        return productRepository.findActiveProductsByRestaurantId(restaurantId)
            .stream()
            .map(ProductDTO::from)
            .collect(Collectors.toList());
    }

    /**
     * Lista apenas produtos disponíveis de um restaurante
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> getAvailableProductsByRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));

        if (!restaurant.isActive() || !restaurant.isOpen()) {
            return List.of(); // Restaurante fechado ou inativo
        }

        return productRepository.findAvailableProductsByRestaurantId(restaurantId)
            .stream()
            .map(ProductDTO::from)
            .collect(Collectors.toList());
    }

    /**
     * Busca um produto por ID
     */
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        
        return ProductDTO.from(product);
    }

    /**
     * Atualiza um produto existente
     */
    public ProductDTO updateProduct(Long id, CreateProductRequest request) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        // Validar se o restaurante do produto ainda está ativo
        if (!product.getRestaurant().isActive()) {
            throw new IllegalArgumentException("Não é possível atualizar produto de restaurante inativo");
        }

        // Validar preço
        if (request.price() == null || request.price().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }

        // Buscar categoria se fornecida
        ProductCategory category = null;
        if (request.categoryId() != null) {
            category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));
        }

        // Atualizar dados
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(new Money(request.price()));
        product.setPreparationTimeInMinutes(request.preparationTimeInMinutes());
        product.setCategory(category);
        
        if (request.image() != null) {
            product.setImage(request.image());
        }

        Product savedProduct = productRepository.save(product);
        return ProductDTO.from(savedProduct);
    }

    /**
     * Altera a disponibilidade de um produto
     */
    public ProductDTO toggleAvailability(Long id, boolean available) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        product.setAvailable(available);
        
        Product savedProduct = productRepository.save(product);
        return ProductDTO.from(savedProduct);
    }

    /**
     * Ativa ou desativa um produto
     */
    public ProductDTO toggleActiveStatus(Long id, boolean active) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        product.setActive(active);
        
        // Se desativando, também tornar indisponível
        if (!active) {
            product.setAvailable(false);
        }
        
        Product savedProduct = productRepository.save(product);
        return ProductDTO.from(savedProduct);
    }

    /**
     * Remove um produto (soft delete)
     */
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        product.setActive(false);
        product.setAvailable(false);
        productRepository.save(product);
    }

    /**
     * Busca produtos por categoria
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new IllegalArgumentException("Categoria não encontrada");
        }

        ProductCategory category = categoryRepository.findById(categoryId).orElseThrow();
        return productRepository.findByCategoryAndActiveTrue(category)
            .stream()
            .filter(Product::isAvailable) // Filtrar apenas disponíveis
            .map(ProductDTO::from)
            .collect(Collectors.toList());
    }

    /**
     * Busca produtos por nome (busca parcial)
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> searchProductsByName(String query) {
        return productRepository.findAllAvailableProducts()
            .stream()
            .filter(product -> product.getName().toLowerCase().contains(query.toLowerCase()))
            .map(ProductDTO::from)
            .collect(Collectors.toList());
    }

    /**
     * Valida se um produto está disponível para pedido
     */
    @Transactional(readOnly = true)
    public boolean isProductAvailableForOrder(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        return product.isActive() 
            && product.isAvailable() 
            && product.getRestaurant().isActive() 
            && product.getRestaurant().isOpen();
    }

    /**
     * Lista todos os produtos (incluindo inativos)
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> findAllProducts() {
        return productRepository.findAll()
            .stream()
            .map(ProductDTO::from)
            .collect(Collectors.toList());
    }

    /**
     * Lista apenas produtos ativos
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> findActiveProducts() {
        return productRepository.findAll()
            .stream()
            .filter(Product::isActive)
            .map(ProductDTO::from)
            .collect(Collectors.toList());
    }

    /**
     * Lista apenas produtos disponíveis (ativos e disponíveis)
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> findAvailableProducts() {
        return productRepository.findAllAvailableProducts()
            .stream()
            .map(ProductDTO::from)
            .collect(Collectors.toList());
    }
}
