package com.deliverytech.deliveryapi.service;

import com.deliverytech.deliveryapi.domain.model.Address;
import com.deliverytech.deliveryapi.domain.model.Money;
import com.deliverytech.deliveryapi.domain.model.Restaurant;
import com.deliverytech.deliveryapi.domain.model.RestaurantCategory;
import com.deliverytech.deliveryapi.domain.repository.RestaurantRepository;
import com.deliverytech.deliveryapi.domain.repository.RestaurantCategoryRepository;
import com.deliverytech.deliveryapi.dto.CreateRestaurantRequest;
import com.deliverytech.deliveryapi.dto.RestaurantCategoryDTO;
import com.deliverytech.deliveryapi.dto.RestaurantDTO;
import com.deliverytech.deliveryapi.exception.EntityNotFoundException;
import com.deliverytech.deliveryapi.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service para gerenciamento de restaurantes
 * Implementa regras de negócio específicas para o domínio de delivery
 */
@Service
@Transactional(readOnly = true)
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantCategoryRepository restaurantCategoryRepository;

    /**
     * Cria um novo restaurante
     * @param request Dados do restaurante a ser criado
     * @return DTO do restaurante criado
     */
    @Transactional
    public RestaurantDTO createRestaurant(CreateRestaurantRequest request) {
        // Validar CNPJ
        if (!isValidCNPJ(request.cnpj())) {
            throw new ValidationException("CNPJ inválido");
        }
        
        // Validar e buscar categorias
        if (request.categoryIds() == null || request.categoryIds().isEmpty()) {
            throw new ValidationException("Pelo menos uma categoria é obrigatória");
        }
        
        // Verificar se todas as categorias existem
        for (Long categoryId : request.categoryIds()) {
            if (!restaurantCategoryRepository.existsById(categoryId)) {
                throw new ValidationException("Categoria com ID " + categoryId + " não encontrada");
            }
        }
        
        // Converter AddressDTO para Address entity
        Address address = new Address(
                request.address().street(),
                request.address().number(),
                request.address().complement(),
                request.address().neighborhood(),
                request.address().city(),
                request.address().state(),
                request.address().postalCode(),
                request.address().reference()
        );
        
        // Criar nova instância de Restaurant
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.name());
        restaurant.setDescription(request.description());
        restaurant.setCnpj(request.cnpj());
        restaurant.setPhone(request.phone());
        restaurant.setAddress(address);
        restaurant.setLogo(request.logo() != null ? request.logo() : "");
        restaurant.setDeliveryFee(new Money(request.deliveryFee()));
        restaurant.setMinimumOrderValue(new Money(request.minimumOrderValue()));
        restaurant.setAverageDeliveryTimeInMinutes(request.averageDeliveryTimeInMinutes());
        
        // Adicionar categorias ao restaurante
        for (Long categoryId : request.categoryIds()) {
            RestaurantCategory category = restaurantCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ValidationException("Categoria com ID " + categoryId + " não encontrada"));
            restaurant.addCategory(category);
        }
        
        // Salvar o restaurante
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        
        // Retornar o DTO
        return RestaurantDTO.from(savedRestaurant);
    }

    /**
     * Lista todos os restaurantes ativos
     * @return Lista de DTOs dos restaurantes ativos
     */
    public List<RestaurantDTO> findAllActiveRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findByActiveTrue();
        return restaurants.stream()
                .map(RestaurantDTO::from)
                .toList();
    }

    /**
     * Lista todos os restaurantes (ativos e inativos)
     * @return Lista de DTOs de todos os restaurantes
     */
    public List<RestaurantDTO> findAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurants.stream()
                .map(RestaurantDTO::from)
                .toList();
    }

    /**
     * Lista apenas restaurantes ativos e abertos
     * @return Lista de DTOs dos restaurantes disponíveis para pedidos
     */
    public List<RestaurantDTO> findOpenRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findByActiveTrueAndOpenTrue();
        return restaurants.stream()
                .map(RestaurantDTO::from)
                .toList();
    }

    /**
     * Busca restaurante por ID
     * @param id ID do restaurante
     * @return Optional com o DTO do restaurante, se encontrado
     */
    public Optional<RestaurantDTO> findRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .map(RestaurantDTO::from);
    }

    /**
     * Busca restaurantes por nome (busca parcial)
     * @param query Termo de busca
     * @return Lista de DTOs dos restaurantes que correspondem à busca
     */
    public List<RestaurantDTO> searchRestaurantsByName(String query) {
        List<Restaurant> restaurants = restaurantRepository.searchByName(query);
        return restaurants.stream()
                .map(RestaurantDTO::from)
                .toList();
    }

    /**
     * Atualiza um restaurante existente
     * @param id ID do restaurante a ser atualizado
     * @param request Dados atualizados do restaurante
     * @return DTO do restaurante atualizado
     * @throws IllegalArgumentException se o restaurante não for encontrado
     */
    @Transactional
    public RestaurantDTO updateRestaurant(Long id, CreateRestaurantRequest request) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));

        // Atualizar dados básicos
        restaurant.setName(request.name());
        restaurant.setDescription(request.description());
        restaurant.setCnpj(request.cnpj());
        restaurant.setPhone(request.phone());
        
        // Atualizar endereço
        if (request.address() != null) {
            Address address = new Address(
                    request.address().street(),
                    request.address().number(),
                    request.address().complement(),
                    request.address().neighborhood(),
                    request.address().city(),
                    request.address().state(),
                    request.address().postalCode(),
                    request.address().reference()
            );
            restaurant.setAddress(address);
        }
        
        // Atualizar outros campos
        if (request.logo() != null) {
            restaurant.setLogo(request.logo());
        }
        if (request.deliveryFee() != null) {
            restaurant.setDeliveryFee(new Money(request.deliveryFee()));
        }
        if (request.minimumOrderValue() != null) {
            restaurant.setMinimumOrderValue(new Money(request.minimumOrderValue()));
        }
        if (request.averageDeliveryTimeInMinutes() != null) {
            restaurant.setAverageDeliveryTimeInMinutes(request.averageDeliveryTimeInMinutes());
        }

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return RestaurantDTO.from(savedRestaurant);
    }

    /**
     * Ativa ou desativa um restaurante
     * @param id ID do restaurante
     * @param active Status ativo do restaurante
     * @return DTO do restaurante atualizado
     * @throws IllegalArgumentException se o restaurante não for encontrado
     */
    @Transactional
    public RestaurantDTO toggleActiveStatus(Long id, boolean active) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));
        
        restaurant.setActive(active);
        
        // Se desativando o restaurante, também fechá-lo
        if (!active) {
            restaurant.setOpen(false);
        }
        
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return RestaurantDTO.from(savedRestaurant);
    }

    /**
     * Abre ou fecha um restaurante para pedidos
     * @param id ID do restaurante
     * @param open Status aberto do restaurante
     * @return DTO do restaurante atualizado
     * @throws IllegalArgumentException se o restaurante não for encontrado ou inativo
     */
    @Transactional
    public RestaurantDTO toggleOpenStatus(Long id, boolean open) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));
        
        if (!restaurant.isActive() && open) {
            throw new IllegalArgumentException("Não é possível abrir um restaurante inativo");
        }
        
        restaurant.setOpen(open);
        
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return RestaurantDTO.from(savedRestaurant);
    }

    /**
     * Remove um restaurante (soft delete - marca como inativo)
     * @param id ID do restaurante
     * @throws IllegalArgumentException se o restaurante não for encontrado
     */
    @Transactional
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));
        
        restaurant.setActive(false);
        restaurant.setOpen(false);
        restaurantRepository.save(restaurant);
    }

    /**
     * Busca restaurantes por categoria (ID da categoria)
     * @param categoryId ID da categoria
     * @return Lista de restaurantes da categoria
     */
    public List<RestaurantDTO> getRestaurantsByCategory(Long categoryId) {
        List<Restaurant> restaurants = restaurantRepository.findByCategoryId(categoryId);
        return restaurants.stream()
                .map(RestaurantDTO::from)
                .toList();
    }

    /**
     * Busca restaurantes por categoria (nome da categoria)
     * @param categoryName Nome da categoria
     * @return Lista de restaurantes da categoria
     */
    public List<RestaurantDTO> getRestaurantsByCategoryName(String categoryName) {
        List<Restaurant> restaurants = restaurantRepository.findByCategoryName(categoryName);
        return restaurants.stream()
                .map(RestaurantDTO::from)
                .toList();
    }

    /**
     * Busca todas as categorias que possuem restaurantes
     * @return Lista de categorias em uso
     */
    public List<RestaurantCategoryDTO> getCategoriesInUse() {
        List<RestaurantCategory> categories = restaurantRepository.findAllCategoriesInUse();
        return categories.stream()
                .map(RestaurantCategoryDTO::from)
                .toList();
    }

    /**
     * Busca restaurantes por nome ou categoria
     * @param query Termo de busca
     * @return Lista de restaurantes que correspondem à busca
     */
    public List<RestaurantDTO> searchRestaurantsByNameOrCategory(String query) {
        List<Restaurant> restaurants = restaurantRepository.searchByNameOrCategory(query);
        return restaurants.stream()
                .map(RestaurantDTO::from)
                .toList();
    }
    
    /**
     * Altera o status ativo/inativo de um restaurante
     * @param id ID do restaurante
     * @param active Novo status (true=ativo, false=inativo)
     * @return DTO do restaurante atualizado
     */
    @Transactional
    public RestaurantDTO updateActiveStatus(Long id, boolean active) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado"));
        
        restaurant.setActive(active);
        Restaurant saved = restaurantRepository.save(restaurant);
        return RestaurantDTO.from(saved);
    }
    
    /**
     * Altera o status de funcionamento (aberto/fechado) de um restaurante
     * @param id ID do restaurante
     * @param open Novo status (true=aberto, false=fechado)
     * @return DTO do restaurante atualizado
     */
    @Transactional
    public RestaurantDTO updateOpenStatus(Long id, boolean open) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado"));
        
        restaurant.setOpen(open);
        Restaurant saved = restaurantRepository.save(restaurant);
        return RestaurantDTO.from(saved);
    }
    
    /**
     * Valida se o CNPJ é válido
     * @param cnpj CNPJ a ser validado
     * @return true se válido, false caso contrário
     */
    private boolean isValidCNPJ(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            return false;
        }
        
        // Remover caracteres não numéricos
        String numbers = cnpj.replaceAll("[^0-9]", "");
        
        // CNPJ deve ter exatamente 14 dígitos
        if (numbers.length() != 14) {
            return false;
        }
        
        // Verificar se não são todos números iguais (ex: 11.111.111/0001-11)
        if (numbers.matches("(\\d)\\1{13}")) {
            return false;
        }
        
        // Para simplificar, consideramos válido se passou nas validações básicas
        // Em produção, implementaria o algoritmo completo dos dígitos verificadores
        return true;
    }
}
