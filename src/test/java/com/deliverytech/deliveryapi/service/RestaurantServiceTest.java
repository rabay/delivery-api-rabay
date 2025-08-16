package com.deliverytech.deliveryapi.service;

import com.deliverytech.deliveryapi.domain.model.Address;
import com.deliverytech.deliveryapi.domain.model.Money;
import com.deliverytech.deliveryapi.domain.model.Restaurant;
import com.deliverytech.deliveryapi.domain.model.RestaurantCategory;
import com.deliverytech.deliveryapi.domain.repository.RestaurantRepository;
import com.deliverytech.deliveryapi.domain.repository.RestaurantCategoryRepository;
import com.deliverytech.deliveryapi.dto.AddressDTO;
import com.deliverytech.deliveryapi.dto.CreateRestaurantRequest;
import com.deliverytech.deliveryapi.dto.RestaurantDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantCategoryRepository restaurantCategoryRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateRestaurantSuccessfully() {
        // Given
        AddressDTO addressDTO = new AddressDTO(
                "Rua Teste",
                "123",
                "Apto 456",
                "Bairro Teste",
                "Cidade Teste",
                "SP",
                "01234-567",
                "Perto da escola"
        );

        CreateRestaurantRequest request = new CreateRestaurantRequest(
                "Restaurante Teste",
                "Descrição do Restaurante Teste",
                "12.345.678/0001-90",
                "(11) 99999-9999",
                addressDTO,
                "https://logo.teste.com",
                new BigDecimal("5.00"),
                new BigDecimal("15.00"),
                30,
                List.of(1L)
        );

        // Mock category validation
        when(restaurantCategoryRepository.existsById(1L)).thenReturn(true);
        
        RestaurantCategory mockCategory = new RestaurantCategory("Italiana", "Culinária italiana");
        mockCategory.setId(1L);
        when(restaurantCategoryRepository.findById(1L)).thenReturn(Optional.of(mockCategory));

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Restaurante Teste");
        restaurant.setDescription("Descrição do Restaurante Teste");
        restaurant.setCnpj("12.345.678/0001-90");
        restaurant.setPhone("(11) 99999-9999");
        restaurant.setAddress(new Address(
                "Rua Teste",
                "123",
                "Apto 456",
                "Bairro Teste",
                "Cidade Teste",
                "SP",
                "01234-567",
                "Perto da escola"
        ));
        restaurant.setLogo("https://logo.teste.com");
        restaurant.setDeliveryFee(new Money(new BigDecimal("5.00")));
        restaurant.setMinimumOrderValue(new Money(new BigDecimal("15.00")));
        restaurant.setAverageDeliveryTimeInMinutes(30);
        restaurant.setActive(true);
        restaurant.setOpen(false);
        // createdAt and updatedAt are managed by Hibernate

        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        // When
        RestaurantDTO result = restaurantService.createRestaurant(request);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Restaurante Teste", result.name());
        assertEquals("12.345.678/0001-90", result.cnpj());
        assertEquals("(11) 99999-9999", result.phone());
        assertEquals("https://logo.teste.com", result.logo());
        assertEquals(true, result.active());
        assertEquals(false, result.open());

        // Verify that the repository save method was called
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    void shouldCreateRestaurantWithMinimalData() {
        // Given
        AddressDTO addressDTO = new AddressDTO(
                "Rua Teste",
                "123",
                null,
                "Bairro Teste",
                "Cidade Teste",
                "SP",
                "01234-567",
                null
        );

        CreateRestaurantRequest request = new CreateRestaurantRequest(
                "Restaurante Teste",
                "Descrição do Restaurante Teste",
                "12.345.678/0001-90",
                "(11) 99999-9999",
                addressDTO,
                null,
                new BigDecimal("0.00"),
                null,
                null,
                List.of(1L)
        );

        // Mock category validation
        when(restaurantCategoryRepository.existsById(1L)).thenReturn(true);
        
        RestaurantCategory mockCategory = new RestaurantCategory("Italiana", "Culinária italiana");
        mockCategory.setId(1L);
        when(restaurantCategoryRepository.findById(1L)).thenReturn(Optional.of(mockCategory));

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Restaurante Teste");
        restaurant.setDescription("Descrição do Restaurante Teste");
        restaurant.setCnpj("12.345.678/0001-90");
        restaurant.setPhone("(11) 99999-9999");
        restaurant.setAddress(new Address(
                "Rua Teste",
                "123",
                null,
                "Bairro Teste",
                "Cidade Teste",
                "SP",
                "01234-567",
                null
        ));
        restaurant.setLogo("");
        restaurant.setDeliveryFee(new Money(new BigDecimal("0.00")));
        restaurant.setMinimumOrderValue(new Money(new BigDecimal("0.00")));
        restaurant.setAverageDeliveryTimeInMinutes(null);
        restaurant.setActive(true);
        restaurant.setOpen(false);
        // createdAt and updatedAt are managed by Hibernate

        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        // When
        RestaurantDTO result = restaurantService.createRestaurant(request);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("", result.logo());
        assertNull(result.address().complement());
        assertNull(result.address().reference());

        // Verify that the repository save method was called
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }
}
