package com.deliverytech.deliveryapi.service;

import com.deliverytech.deliveryapi.domain.model.User;
import com.deliverytech.deliveryapi.domain.model.UserType;
import com.deliverytech.deliveryapi.domain.repository.UserRepository;
import com.deliverytech.deliveryapi.dto.CreateCustomerRequest;
import com.deliverytech.deliveryapi.dto.CustomerDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para gerenciamento de clientes
 */
@Service
@Transactional
public class CustomerService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cria um novo cliente
     */
    public CustomerDTO createCustomer(CreateCustomerRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setPhone(request.phone());
        user.setUserType(UserType.CUSTOMER);
        
        if (request.address() != null) {
            user.setAddress(new com.deliverytech.deliveryapi.domain.model.Address(
                request.address().street(),
                request.address().number(),
                request.address().complement(),
                request.address().neighborhood(),
                request.address().city(),
                request.address().state(),
                request.address().postalCode(),
                request.address().reference()
            ));
        }

        User savedUser = userRepository.save(user);
        return CustomerDTO.from(savedUser);
    }

    /**
     * Busca um cliente pelo ID
     */
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        
        if (user.getUserType() != UserType.CUSTOMER) {
            throw new IllegalArgumentException("Usuário não é um cliente");
        }
        
        return CustomerDTO.from(user);
    }

    /**
     * Lista todos os clientes
     */
    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers() {
        return userRepository.findByUserType(UserType.CUSTOMER)
            .stream()
            .map(CustomerDTO::from)
            .collect(Collectors.toList());
    }

    /**
     * Busca um cliente pelo email
     */
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        
        if (user.getUserType() != UserType.CUSTOMER) {
            throw new IllegalArgumentException("Usuário não é um cliente");
        }
        
        return CustomerDTO.from(user);
    }

    /**
     * Atualiza os dados de um cliente
     */
    @Transactional
    public CustomerDTO updateCustomer(Long id, CreateCustomerRequest request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        
        if (user.getUserType() != UserType.CUSTOMER) {
            throw new IllegalArgumentException("Usuário não é um cliente");
        }

        // Validar e-mail se estiver sendo alterado
        if (!user.getEmail().equals(request.email()) && userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        // Atualizar dados
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        
        // Atualizar endereço se fornecido
        if (request.address() != null) {
            user.setAddress(new com.deliverytech.deliveryapi.domain.model.Address(
                request.address().street(),
                request.address().number(),
                request.address().complement(),
                request.address().neighborhood(),
                request.address().city(),
                request.address().state(),
                request.address().postalCode(),
                request.address().reference()
            ));
        }

        User savedUser = userRepository.save(user);
        return CustomerDTO.from(savedUser);
    }

    /**
     * Inativa um cliente (soft delete)
     */
    @Transactional
    public void deleteCustomer(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        
        if (user.getUserType() != UserType.CUSTOMER) {
            throw new IllegalArgumentException("Usuário não é um cliente");
        }

        user.setActive(false);
        userRepository.save(user);
    }

    /**
     * Inativa um cliente (soft delete) - método alternativo
     */
    @Transactional
    public void inactivateCustomer(Long id) {
        deleteCustomer(id);
    }

    /**
     * Valida se o e-mail já está em uso
     */
    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }
}
