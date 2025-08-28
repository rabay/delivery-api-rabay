package com.deliverytech.delivery_api.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Categoria Validator Tests")
class CategoriaValidatorTest {

    private CategoriaValidator validator;

    @Mock
    private ValidCategoria constraintAnnotation;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new CategoriaValidator();
    }

    @Test
    @DisplayName("Deve aceitar categorias de restaurante válidas")
    void deveAceitarCategoriasDeRestauranteValidas() {
        when(constraintAnnotation.type()).thenReturn(ValidCategoria.Type.RESTAURANTE);
        validator.initialize(constraintAnnotation);

        assertTrue(validator.isValid("Brasileira", context));
        assertTrue(validator.isValid("Italiana", context));
        assertTrue(validator.isValid("Japonesa", context));
        assertTrue(validator.isValid("Chinesa", context));
        assertTrue(validator.isValid("Americana", context));
        assertTrue(validator.isValid("Mexicana", context));
        assertTrue(validator.isValid("Indiana", context));
        assertTrue(validator.isValid("Árabe", context));
        assertTrue(validator.isValid("Francesa", context));
        assertTrue(validator.isValid("Fast Food", context));
        assertTrue(validator.isValid("Vegetariana", context));
        assertTrue(validator.isValid("Vegana", context));
        assertTrue(validator.isValid("Pizzaria", context));
        assertTrue(validator.isValid("Hamburgueria", context));
        assertTrue(validator.isValid("Churrascaria", context));
    }

    @Test
    @DisplayName("Deve aceitar categorias de produto válidas")
    void deveAceitarCategoriasDeProdutoValidas() {
        when(constraintAnnotation.type()).thenReturn(ValidCategoria.Type.PRODUTO);
        validator.initialize(constraintAnnotation);

        assertTrue(validator.isValid("Pizza", context));
        assertTrue(validator.isValid("Hambúrguer", context));
        assertTrue(validator.isValid("Sushi", context));
        assertTrue(validator.isValid("Bebida", context));
        assertTrue(validator.isValid("Sobremesa", context));
        assertTrue(validator.isValid("Lanche", context));
        assertTrue(validator.isValid("Prato Principal", context));
        assertTrue(validator.isValid("Entrada", context));
        assertTrue(validator.isValid("Salada", context));
        assertTrue(validator.isValid("Sanduíche", context));
        assertTrue(validator.isValid("Refrigerante", context));
        assertTrue(validator.isValid("Suco", context));
        assertTrue(validator.isValid("Açaí", context));
    }

    @Test
    @DisplayName("Deve aceitar todas as categorias quando tipo é GERAL")
    void deveAceitarTodasCategoriasQuandoTipoEGeral() {
        when(constraintAnnotation.type()).thenReturn(ValidCategoria.Type.GERAL);
        validator.initialize(constraintAnnotation);

        // Categorias de restaurante
        assertTrue(validator.isValid("Brasileira", context));
        assertTrue(validator.isValid("Italiana", context));
        assertTrue(validator.isValid("Pizzaria", context));
        
        // Categorias de produto
        assertTrue(validator.isValid("Pizza", context));
        assertTrue(validator.isValid("Hambúrguer", context));
        assertTrue(validator.isValid("Bebida", context));
    }

    @Test
    @DisplayName("Deve aceitar valores nulos")
    void deveAceitarValoresNulos() {
        when(constraintAnnotation.type()).thenReturn(ValidCategoria.Type.GERAL);
        validator.initialize(constraintAnnotation);
        
        assertTrue(validator.isValid(null, context));
    }

    @Test
    @DisplayName("Deve aceitar strings vazias")
    void deveAceitarStringsVazias() {
        when(constraintAnnotation.type()).thenReturn(ValidCategoria.Type.GERAL);
        validator.initialize(constraintAnnotation);
        
        assertTrue(validator.isValid("", context));
    }

    @Test
    @DisplayName("Deve formatar categorias corretamente (capitalizar)")
    void deveFormatarCategoriasCorretamente() {
        when(constraintAnnotation.type()).thenReturn(ValidCategoria.Type.RESTAURANTE);
        validator.initialize(constraintAnnotation);

        assertTrue(validator.isValid("brasileira", context));     // minúsculo
        assertTrue(validator.isValid("ITALIANA", context));       // maiúsculo
        assertTrue(validator.isValid("japonesa", context));       // minúsculo
        assertTrue(validator.isValid("Chinesa", context));        // já formatado
        assertTrue(validator.isValid("fast food", context));      // minúsculo com espaço
        assertTrue(validator.isValid("FAST FOOD", context));      // maiúsculo com espaço
    }

    @Test
    @DisplayName("Deve rejeitar categorias de restaurante quando tipo é PRODUTO")
    void deveRejeitarCategoriasDeRestauranteQuandoTipoEProduto() {
        when(constraintAnnotation.type()).thenReturn(ValidCategoria.Type.PRODUTO);
        validator.initialize(constraintAnnotation);

        assertFalse(validator.isValid("Brasileira", context));
        assertFalse(validator.isValid("Italiana", context));
        assertFalse(validator.isValid("Pizzaria", context));
        assertFalse(validator.isValid("Hamburgueria", context));
        assertFalse(validator.isValid("Churrascaria", context));
    }

    @Test
    @DisplayName("Deve rejeitar categorias de produto quando tipo é RESTAURANTE")
    void deveRejeitarCategoriasDeProdutoQuandoTipoERestaurante() {
        when(constraintAnnotation.type()).thenReturn(ValidCategoria.Type.RESTAURANTE);
        validator.initialize(constraintAnnotation);

        assertFalse(validator.isValid("Pizza", context));
        assertFalse(validator.isValid("Hambúrguer", context));
        assertFalse(validator.isValid("Sushi", context));
        assertFalse(validator.isValid("Bebida", context));
        assertFalse(validator.isValid("Sobremesa", context));
    }

    @Test
    @DisplayName("Deve rejeitar categorias inválidas")
    void deveRejeitarCategoriasInvalidas() {
        when(constraintAnnotation.type()).thenReturn(ValidCategoria.Type.GERAL);
        validator.initialize(constraintAnnotation);

        assertFalse(validator.isValid("Categoria Inexistente", context));
        assertFalse(validator.isValid("InvalidCategory", context));
        assertFalse(validator.isValid("123", context));
        assertFalse(validator.isValid("@#$", context));
        assertFalse(validator.isValid("Comida Aleatória", context));
    }

    @Test
    @DisplayName("Deve validar categorias brasileiras específicas")
    void deveValidarCategoriasBrasileirasEspecificas() {
        when(constraintAnnotation.type()).thenReturn(ValidCategoria.Type.RESTAURANTE);
        validator.initialize(constraintAnnotation);

        assertTrue(validator.isValid("Brasileira", context));
        assertTrue(validator.isValid("Churrascaria", context));
        assertTrue(validator.isValid("Açaíteria", context));
        assertTrue(validator.isValid("Padaria", context));
        assertTrue(validator.isValid("Lanchonete", context));
    }

    @Test
    @DisplayName("Deve validar produtos típicos brasileiros")
    void deveValidarProdutosTipicosBrasileiros() {
        when(constraintAnnotation.type()).thenReturn(ValidCategoria.Type.PRODUTO);
        validator.initialize(constraintAnnotation);

        assertTrue(validator.isValid("Açaí", context));
        assertTrue(validator.isValid("Coxinha", context));
        assertTrue(validator.isValid("Pastel", context));
        assertTrue(validator.isValid("Esfiha", context));
        assertTrue(validator.isValid("Tapioca", context));
        assertTrue(validator.isValid("Vitamina", context));
    }

    @Test
    @DisplayName("Deve lidar com espaços extras")
    void deveLidarComEspacosExtras() {
        when(constraintAnnotation.type()).thenReturn(ValidCategoria.Type.RESTAURANTE);
        validator.initialize(constraintAnnotation);

        assertTrue(validator.isValid(" Brasileira ", context));
        assertTrue(validator.isValid("  Italiana  ", context));
        assertTrue(validator.isValid("\tJaponesa\t", context));
        assertTrue(validator.isValid("\nChinesa\n", context));
    }

    @Test
    @DisplayName("Deve validar categorias com acentos")
    void deveValidarCategoriasComAcentos() {
        when(constraintAnnotation.type()).thenReturn(ValidCategoria.Type.RESTAURANTE);
        validator.initialize(constraintAnnotation);

        assertTrue(validator.isValid("Árabe", context));
        assertTrue(validator.isValid("Contemporânea", context));
        assertTrue(validator.isValid("Mediterrânea", context));
        
        when(constraintAnnotation.type()).thenReturn(ValidCategoria.Type.PRODUTO);
        validator.initialize(constraintAnnotation);
        
        assertTrue(validator.isValid("Sanduíche", context));
        assertTrue(validator.isValid("Hambúrguer", context));
    }

    @Test
    @DisplayName("Deve usar configuração padrão quando não especificado")
    void deveUsarConfiguracaoPadraoQuandoNaoEspecificado() {
        // Não especifica tipo, deve usar GERAL como padrão
        when(constraintAnnotation.type()).thenReturn(ValidCategoria.Type.GERAL);
        validator.initialize(constraintAnnotation);

        // Deve aceitar ambos os tipos de categoria
        assertTrue(validator.isValid("Brasileira", context));  // Restaurante
        assertTrue(validator.isValid("Pizza", context));       // Produto
        assertTrue(validator.isValid("Italiana", context));    // Restaurante
        assertTrue(validator.isValid("Bebida", context));      // Produto
    }
}