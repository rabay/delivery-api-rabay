package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
  UserDetails findByEmail(String email);

  Optional<Usuario> findUsuarioByEmail(String email);

  long countByAtivo(boolean ativo);
}
