package com.deliverytech.delivery_api.config;

import com.deliverytech.delivery_api.model.Role;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(100) // Run after other data loaders
@Profile({"!test", "dev", "default"}) // Don't run in test profile unless explicitly needed
public class UserDataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        loadDefaultUsers();
    }

    private void loadDefaultUsers() {
        // Check if users already exist
        if (usuarioRepository.count() > 0) {
            log.debug("Usuários já existem no banco de dados, pulando inicialização");
            return;
        }

        log.info("Criando usuários padrão para JWT");

        // Create users in batch for better performance
        List<Usuario> defaultUsers = List.of(
            Usuario.builder()
                .nome("Administrator")
                .email("admin@deliveryapi.com")
                .senha(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .ativo(true)
                .dataCriacao(LocalDateTime.now())
                .build(),
            
            Usuario.builder()
                .nome("Cliente Teste")
                .email("cliente@test.com")
                .senha(passwordEncoder.encode("cliente123"))
                .role(Role.CLIENTE)
                .ativo(true)
                .dataCriacao(LocalDateTime.now())
                .build(),
            
            Usuario.builder()
                .nome("Restaurante Teste")
                .email("restaurante@test.com")
                .senha(passwordEncoder.encode("restaurante123"))
                .role(Role.RESTAURANTE)
                .ativo(true)
                .dataCriacao(LocalDateTime.now())
                .restauranteId(1L)
                .build(),
            
            Usuario.builder()
                .nome("Entregador Teste")
                .email("entregador@test.com")
                .senha(passwordEncoder.encode("entregador123"))
                .role(Role.ENTREGADOR)
                .ativo(true)
                .dataCriacao(LocalDateTime.now())
                .build()
        );

        usuarioRepository.saveAll(defaultUsers);
        log.info("✅ {} usuários padrão criados com sucesso", defaultUsers.size());
        log.debug("JWT Authentication ready with default users");
    }
}