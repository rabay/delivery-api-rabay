package com.deliverytech.deliveryapi.service;

import com.deliverytech.deliveryapi.domain.model.User;
import com.deliverytech.deliveryapi.domain.model.UserType;
import com.deliverytech.deliveryapi.domain.repository.UserRepository;
import com.deliverytech.deliveryapi.dto.CreateCustomerRequest;
import com.deliverytech.deliveryapi.dto.CustomerDTO;
import com.deliverytech.deliveryapi.exception.EntityNotFoundException;
import com.deliverytech.deliveryapi.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerService customerService;

    private CreateCustomerRequest createCustomerRequest;
    private User user;

    @BeforeEach
    void setUp() {
        createCustomerRequest = new CreateCustomerRequest(
            "João Silva",
            "joao.silva@email.com",
            "password123",
            "11999999999",
            null
        );

        user = new User();
        user.setId(1L);
        user.setName("João Silva");
        user.setEmail("joao.silva@email.com");
        user.setPassword("encodedPassword");
        user.setPhone("11999999999");
        user.setUserType(UserType.CUSTOMER);
        user.setActive(true);
        user.setEmailVerified(false);
    }

    @Test
    void createCustomer_ShouldCreateCustomer_WhenEmailIsNotUsed() {
        // Arrange
        when(userRepository.existsByEmail(createCustomerRequest.email())).thenReturn(false);
        when(passwordEncoder.encode(createCustomerRequest.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        CustomerDTO result = customerService.createCustomer(createCustomerRequest);

        // Assert
        assertNotNull(result);
        assertEquals(user.getId(), result.id());
        assertEquals(user.getName(), result.name());
        assertEquals(user.getEmail(), result.email());
        assertEquals(user.getPhone(), result.phone());
        assertEquals(user.isActive(), result.active());
        assertEquals(user.isEmailVerified(), result.emailVerified());

        verify(userRepository).existsByEmail(createCustomerRequest.email());
        verify(passwordEncoder).encode(createCustomerRequest.password());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createCustomer_ShouldThrowException_WhenEmailIsAlreadyUsed() {
        // Arrange
        when(userRepository.existsByEmail(createCustomerRequest.email())).thenReturn(true);

        // Act & Assert
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> customerService.createCustomer(createCustomerRequest)
        );

        assertEquals("Email já cadastrado", exception.getMessage());
        verify(userRepository).existsByEmail(createCustomerRequest.email());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getCustomerById_ShouldReturnCustomer_WhenCustomerExists() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        CustomerDTO result = customerService.getCustomerById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(user.getId(), result.id());
        assertEquals(user.getName(), result.name());
        assertEquals(user.getEmail(), result.email());

        verify(userRepository).findById(1L);
    }

    @Test
    void getCustomerById_ShouldThrowException_WhenCustomerDoesNotExist() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
            EntityNotFoundException.class,
            () -> customerService.getCustomerById(1L)
        );

        assertEquals("Cliente não encontrado", exception.getMessage());
        verify(userRepository).findById(1L);
    }

    @Test
    void getCustomerById_ShouldThrowException_WhenUserIsNotCustomer() {
        // Arrange
        user.setUserType(UserType.RESTAURANT);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act & Assert
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> customerService.getCustomerById(1L)
        );

        assertEquals("Usuário não é um cliente", exception.getMessage());
        verify(userRepository).findById(1L);
    }

    @Test
    void getAllCustomers_ShouldReturnAllCustomers() {
        // Arrange
        User customer2 = new User();
        customer2.setId(2L);
        customer2.setName("Maria Santos");
        customer2.setEmail("maria.santos@email.com");
        customer2.setPassword("encodedPassword");
        customer2.setPhone("11888888888");
        customer2.setUserType(UserType.CUSTOMER);
        customer2.setActive(true);
        customer2.setEmailVerified(false);

        List<User> customers = List.of(user, customer2);
        when(userRepository.findByUserType(UserType.CUSTOMER)).thenReturn(customers);

        // Act
        List<CustomerDTO> result = customerService.getAllCustomers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(user.getId(), result.get(0).id());
        assertEquals(customer2.getId(), result.get(1).id());

        verify(userRepository).findByUserType(UserType.CUSTOMER);
    }

    @Test
    void getCustomerByEmail_ShouldReturnCustomer_WhenCustomerExists() {
        // Arrange
        when(userRepository.findByEmail("joao.silva@email.com")).thenReturn(Optional.of(user));

        // Act
        CustomerDTO result = customerService.getCustomerByEmail("joao.silva@email.com");

        // Assert
        assertNotNull(result);
        assertEquals(user.getId(), result.id());
        assertEquals(user.getName(), result.name());
        assertEquals(user.getEmail(), result.email());

        verify(userRepository).findByEmail("joao.silva@email.com");
    }

    @Test
    void getCustomerByEmail_ShouldThrowException_WhenCustomerDoesNotExist() {
        // Arrange
        when(userRepository.findByEmail("nonexistent@email.com")).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
            EntityNotFoundException.class,
            () -> customerService.getCustomerByEmail("nonexistent@email.com")
        );

        assertEquals("Cliente não encontrado", exception.getMessage());
        verify(userRepository).findByEmail("nonexistent@email.com");
    }

    @Test
    void getCustomerByEmail_ShouldThrowException_WhenUserIsNotCustomer() {
        // Arrange
        user.setUserType(UserType.DELIVERY_PERSON);
        when(userRepository.findByEmail("joao.silva@email.com")).thenReturn(Optional.of(user));

        // Act & Assert
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> customerService.getCustomerByEmail("joao.silva@email.com")
        );

        assertEquals("Usuário não é um cliente", exception.getMessage());
        verify(userRepository).findByEmail("joao.silva@email.com");
    }
}
