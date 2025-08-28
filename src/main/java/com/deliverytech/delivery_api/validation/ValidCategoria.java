package com.deliverytech.delivery_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação personalizada para validar categorias de restaurantes e produtos.
 * 
 * Aceita categorias predefinidas válidas:
 * - Restaurantes: Brasileira, Italiana, Japonesa, Chinesa, Americana, Mexicana, Indiana, Árabe, Francesa, etc.
 * - Produtos: Pizza, Hambúrguer, Sushi, Bebida, Sobremesa, Lanche, Prato Principal, Entrada, etc.
 * 
 * Exemplo de uso:
 * @ValidCategoria(type = ValidCategoria.Type.RESTAURANTE)
 * private String categoria;
 */
@Documented
@Constraint(validatedBy = CategoriaValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCategoria {
    
    /**
     * Mensagem de erro padrão exibida quando a validação falha
     */
    String message() default "Categoria deve ser uma categoria válida";
    
    /**
     * Tipo de categoria a ser validada
     */
    Type type() default Type.GERAL;
    
    /**
     * Grupos de validação - permite agrupar validações
     */
    Class<?>[] groups() default {};
    
    /**
     * Payload - permite anexar metadados à validação
     */
    Class<? extends Payload>[] payload() default {};
    
    /**
     * Enum que define os tipos de categoria
     */
    enum Type {
        RESTAURANTE,
        PRODUTO,
        GERAL
    }
}