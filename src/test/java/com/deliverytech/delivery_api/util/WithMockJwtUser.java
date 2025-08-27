package com.deliverytech.delivery_api.util;

import com.deliverytech.delivery_api.model.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Custom annotation for testing with mock authenticated users
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockJwtUserSecurityContextFactory.class)
public @interface WithMockJwtUser {
    String email() default "test@example.com";
    Role role() default Role.CLIENTE;
    String name() default "Test User";
    boolean ativo() default true;
    long id() default 1L;
}