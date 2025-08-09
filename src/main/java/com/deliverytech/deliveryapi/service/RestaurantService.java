package com.deliverytech.deliveryapi.service;

import com.deliverytech.deliveryapi.domain.model.Address;
import com.deliverytech.deliveryapi.domain.model.Money;
import com.deliverytech.deliveryapi.domain.model.Restaurant;
import com.deliverytech.deliveryapi.domain.repository.RestaurantRepository;
import com.deliverytech.deliveryapi.dto.CreateRestaurantRequest;
import com.deliverytech.deliveryapi.dto.RestaurantDTO;
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

    /**
     * Cria um novo restaurante
     * @param request Dados do restaurante a ser criado
     * @return DTO do restaurante criado
     */
    @Transactional
    public RestaurantDTO createRestaurant(CreateRestaurantRequest request) {
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
}
