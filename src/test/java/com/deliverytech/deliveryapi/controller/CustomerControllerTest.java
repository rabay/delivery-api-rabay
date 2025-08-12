package com.deliverytech.deliveryapi.controller;

import com.deliverytech.deliveryapi.dto.CreateCustomerRequest;
import com.deliverytech.deliveryapi.dto.CustomerDTO;
import com.deliverytech.deliveryapi.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private CreateCustomerRequest createCustomerRequest;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
        objectMapper = new ObjectMapper();

        createCustomerRequest = new CreateCustomerRequest(
            "João Silva",
            "joao.silva@email.com",
            "password123",
            "11999999999",
            null
        );

        customerDTO = new CustomerDTO(
            1L,
            "João Silva",
            "joao.silva@email.com",
            "11999999999",
            null,
            null,
            false,
            true,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    @Test
    void createCustomer_ShouldReturnCreatedCustomer_WhenRequestIsValid() throws Exception {
        // Arrange
        when(customerService.createCustomer(any(CreateCustomerRequest.class))).thenReturn(customerDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCustomerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao.silva@email.com"))
                .andExpect(jsonPath("$.phone").value("11999999999"));
    }

    @Test
    void createCustomer_ShouldReturnBadRequest_WhenRequestIsInvalid() throws Exception {
        // Arrange
        CreateCustomerRequest invalidRequest = new CreateCustomerRequest(
            "", // Nome vazio
            "invalid-email", // Email inválido
            "123", // Senha muito curta
            "", // Telefone vazio
            null
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCustomerById_ShouldReturnCustomer_WhenCustomerExists() throws Exception {
        // Arrange
        when(customerService.getCustomerById(1L)).thenReturn(customerDTO);

        // Act & Assert
        mockMvc.perform(get("/api/v1/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao.silva@email.com"));
    }

    @Test
    void getCustomerById_ShouldReturnNotFound_WhenCustomerDoesNotExist() throws Exception {
        // Arrange
        when(customerService.getCustomerById(999L))
                .thenThrow(new IllegalArgumentException("Cliente não encontrado"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/customers/999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllCustomers_ShouldReturnAllCustomers() throws Exception {
        // Arrange
        CustomerDTO customer2 = new CustomerDTO(
            2L,
            "Maria Santos",
            "maria.santos@email.com",
            "11888888888",
            null,
            null,
            false,
            true,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        List<CustomerDTO> customers = List.of(customerDTO, customer2);
        when(customerService.getAllCustomers()).thenReturn(customers);

        // Act & Assert
        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void getCustomerByEmail_ShouldReturnCustomer_WhenCustomerExists() throws Exception {
        // Arrange
        when(customerService.getCustomerByEmail("joao.silva@email.com")).thenReturn(customerDTO);

        // Act & Assert
        mockMvc.perform(get("/api/v1/customers/email/joao.silva@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao.silva@email.com"));
    }

    @Test
    void getCustomerByEmail_ShouldReturnNotFound_WhenCustomerDoesNotExist() throws Exception {
        // Arrange
        when(customerService.getCustomerByEmail("nonexistent@email.com"))
                .thenThrow(new IllegalArgumentException("Cliente não encontrado"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/customers/email/nonexistent@email.com"))
                .andExpect(status().isBadRequest());
    }
}
