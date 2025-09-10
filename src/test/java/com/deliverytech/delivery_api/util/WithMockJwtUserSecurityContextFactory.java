package com.deliverytech.delivery_api.util;

import com.deliverytech.delivery_api.model.Usuario;
import java.time.LocalDateTime;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

/** Security context factory for creating authenticated test users */
public class WithMockJwtUserSecurityContextFactory
    implements WithSecurityContextFactory<WithMockJwtUser> {

  @Override
  public SecurityContext createSecurityContext(WithMockJwtUser annotation) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();

    Usuario usuario =
        Usuario.builder()
            .id(annotation.id())
            .nome(annotation.name())
            .email(annotation.email())
            .senha("$2a$10$encrypted.password.hash")
            .role(annotation.role())
            .ativo(annotation.ativo())
            .dataCriacao(LocalDateTime.now())
            .build();

    Authentication authentication =
        new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

    context.setAuthentication(authentication);
    return context;
  }
}
