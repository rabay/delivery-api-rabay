package com.deliverytech.delivery_api.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Produto Entity Tests")
class ProdutoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("Constructor and Builder Tests")
    class ConstructorAndBuilderTests {

        @Test
        @DisplayName("Should create Produto with no-args constructor")
        void shouldCreateProdutoWithNoArgsConstructor() {
            // When
            Produto produto = new Produto();

            // Then
            assertNotNull(produto);
            assertNull(produto.getId());
            assertNull(produto.getNome());
            assertNull(produto.getCategoria());
            assertNull(produto.getDescricao());
            assertNull(produto.getPreco());
            assertTrue(produto.getDisponivel()); // Default value from @Builder.Default
            assertFalse(produto.getExcluido()); // Default value from @Builder.Default
            assertNull(produto.getRestaurante());
            assertNull(produto.getQuantidadeEstoque());
        }

        @Test
        @DisplayName("Should create Produto with all-args constructor")
        void shouldCreateProdutoWithAllArgsConstructor() {
            // Given
            Restaurante restaurante = Restaurante.builder().id(1L).build();

            // When
            Produto produto = new Produto(1L, "Pizza Margherita", "Pizza", "Pizza tradicional",
                    new BigDecimal("25.90"), true, false, restaurante, 10);

            // Then
            assertEquals(1L, produto.getId());
            assertEquals("Pizza Margherita", produto.getNome());
            assertEquals("Pizza", produto.getCategoria());
            assertEquals("Pizza tradicional", produto.getDescricao());
            assertEquals(new BigDecimal("25.90"), produto.getPreco());
            assertTrue(produto.getDisponivel());
            assertFalse(produto.getExcluido());
            assertEquals(restaurante, produto.getRestaurante());
            assertEquals(10, produto.getQuantidadeEstoque());
        }

        @Test
        @DisplayName("Should create Produto using builder pattern")
        void shouldCreateProdutoUsingBuilderPattern() {
            // Given
            Restaurante restaurante = Restaurante.builder().id(2L).build();

            // When
            Produto produto = Produto.builder()
                    .id(1L)
                    .nome("Hambúrguer")
                    .categoria("Lanches")
                    .descricao("Hambúrguer artesanal")
                    .preco(new BigDecimal("15.50"))
                    .disponivel(true)
                    .excluido(false)
                    .restaurante(restaurante)
                    .quantidadeEstoque(5)
                    .build();

            // Then
            assertEquals(1L, produto.getId());
            assertEquals("Hambúrguer", produto.getNome());
            assertEquals("Lanches", produto.getCategoria());
            assertEquals("Hambúrguer artesanal", produto.getDescricao());
            assertEquals(new BigDecimal("15.50"), produto.getPreco());
            assertTrue(produto.getDisponivel());
            assertFalse(produto.getExcluido());
            assertEquals(restaurante, produto.getRestaurante());
            assertEquals(5, produto.getQuantidadeEstoque());
        }

        @Test
        @DisplayName("Should set default values for disponivel and excluido")
        void shouldSetDefaultValuesForDisponivelAndExcluido() {
            // Given
            Restaurante restaurante = Restaurante.builder().id(1L).build();

            // When
            Produto produto = Produto.builder()
                    .nome("Produto Teste")
                    .preco(new BigDecimal("10.00"))
                    .restaurante(restaurante)
                    .quantidadeEstoque(1)
                    .build();

            // Then
            assertTrue(produto.getDisponivel()); // Default true
            assertFalse(produto.getExcluido()); // Default false
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should validate Produto with all required fields")
        void shouldValidateProdutoWithAllRequiredFields() {
            // Given
            Restaurante restaurante = Restaurante.builder().id(1L).build();
            Produto produto = Produto.builder()
                    .nome("Produto Válido")
                    .preco(new BigDecimal("10.00"))
                    .restaurante(restaurante)
                    .quantidadeEstoque(1)
                    .build();

            // When
            Set<ConstraintViolation<Produto>> violations = validator.validate(produto);

            // Then
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("Should fail validation when nome is blank")
        void shouldFailValidationWhenNomeIsBlank() {
            // Given
            Restaurante restaurante = Restaurante.builder().id(1L).build();
            Produto produto = Produto.builder()
                    .nome("")
                    .preco(new BigDecimal("10.00"))
                    .restaurante(restaurante)
                    .quantidadeEstoque(1)
                    .build();

            // When
            Set<ConstraintViolation<Produto>> violations = validator.validate(produto);

            // Then
            assertFalse(violations.isEmpty());
            assertEquals(1, violations.size());
            assertEquals("Nome do produto é obrigatório", violations.iterator().next().getMessage());
        }

        @Test
        @DisplayName("Should fail validation when nome is null")
        void shouldFailValidationWhenNomeIsNull() {
            // Given
            Restaurante restaurante = Restaurante.builder().id(1L).build();
            Produto produto = Produto.builder()
                    .nome(null)
                    .preco(new BigDecimal("10.00"))
                    .restaurante(restaurante)
                    .quantidadeEstoque(1)
                    .build();

            // When
            Set<ConstraintViolation<Produto>> violations = validator.validate(produto);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Nome do produto é obrigatório")));
        }

        @Test
        @DisplayName("Should fail validation when nome is too long")
        void shouldFailValidationWhenNomeIsTooLong() {
            // Given
            Restaurante restaurante = Restaurante.builder().id(1L).build();
            String nomeLongo = "a".repeat(101); // 101 caracteres
            Produto produto = Produto.builder()
                    .nome(nomeLongo)
                    .preco(new BigDecimal("10.00"))
                    .restaurante(restaurante)
                    .quantidadeEstoque(1)
                    .build();

            // When
            Set<ConstraintViolation<Produto>> violations = validator.validate(produto);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Nome não pode ter mais de 100 caracteres")));
        }

        @Test
        @DisplayName("Should fail validation when preco is null")
        void shouldFailValidationWhenPrecoIsNull() {
            // Given
            Restaurante restaurante = Restaurante.builder().id(1L).build();
            Produto produto = Produto.builder()
                    .nome("Produto Teste")
                    .preco(null)
                    .restaurante(restaurante)
                    .quantidadeEstoque(1)
                    .build();

            // When
            Set<ConstraintViolation<Produto>> violations = validator.validate(produto);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Preço é obrigatório")));
        }

        @Test
        @DisplayName("Should fail validation when preco is zero")
        void shouldFailValidationWhenPrecoIsZero() {
            // Given
            Restaurante restaurante = Restaurante.builder().id(1L).build();
            Produto produto = Produto.builder()
                    .nome("Produto Teste")
                    .preco(BigDecimal.ZERO)
                    .restaurante(restaurante)
                    .quantidadeEstoque(1)
                    .build();

            // When
            Set<ConstraintViolation<Produto>> violations = validator.validate(produto);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Preço deve ser maior que zero")));
        }

        @Test
        @DisplayName("Should fail validation when restaurante is null")
        void shouldFailValidationWhenRestauranteIsNull() {
            // Given
            Produto produto = Produto.builder()
                    .nome("Produto Teste")
                    .preco(new BigDecimal("10.00"))
                    .restaurante(null)
                    .quantidadeEstoque(1)
                    .build();

            // When
            Set<ConstraintViolation<Produto>> violations = validator.validate(produto);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Restaurante é obrigatório")));
        }

        @Test
        @DisplayName("Should validate categoria length")
        void shouldValidateCategoriaLength() {
            // Given
            Restaurante restaurante = Restaurante.builder().id(1L).build();
            String categoriaLonga = "a".repeat(51); // 51 caracteres
            Produto produto = Produto.builder()
                    .nome("Produto Teste")
                    .categoria(categoriaLonga)
                    .preco(new BigDecimal("10.00"))
                    .restaurante(restaurante)
                    .quantidadeEstoque(1)
                    .build();

            // When
            Set<ConstraintViolation<Produto>> violations = validator.validate(produto);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Categoria não pode ter mais de 50 caracteres")));
        }

        @Test
        @DisplayName("Should validate descricao length")
        void shouldValidateDescricaoLength() {
            // Given
            Restaurante restaurante = Restaurante.builder().id(1L).build();
            String descricaoLonga = "a".repeat(501); // 501 caracteres
            Produto produto = Produto.builder()
                    .nome("Produto Teste")
                    .descricao(descricaoLonga)
                    .preco(new BigDecimal("10.00"))
                    .restaurante(restaurante)
                    .quantidadeEstoque(1)
                    .build();

            // When
            Set<ConstraintViolation<Produto>> violations = validator.validate(produto);

            // Then
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Descrição não pode ter mais de 500 caracteres")));
        }
    }

    @Nested
    @DisplayName("Business Logic Tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("Should identify infinite stock when quantidadeEstoque is negative")
        void shouldIdentifyInfiniteStockWhenQuantidadeEstoqueIsNegative() {
            // Given
            Produto produto = Produto.builder()
                    .quantidadeEstoque(-1)
                    .build();

            // When & Then
            assertTrue(produto.isInfiniteStock());
        }

        @Test
        @DisplayName("Should not identify infinite stock when quantidadeEstoque is positive")
        void shouldNotIdentifyInfiniteStockWhenQuantidadeEstoqueIsPositive() {
            // Given
            Produto produto = Produto.builder()
                    .quantidadeEstoque(10)
                    .build();

            // When & Then
            assertFalse(produto.isInfiniteStock());
        }

        @Test
        @DisplayName("Should not identify infinite stock when quantidadeEstoque is zero")
        void shouldNotIdentifyInfiniteStockWhenQuantidadeEstoqueIsZero() {
            // Given
            Produto produto = Produto.builder()
                    .quantidadeEstoque(0)
                    .build();

            // When & Then
            assertFalse(produto.isInfiniteStock());
        }

        @Test
        @DisplayName("Should be disponivel when disponivel is true and has stock")
        void shouldBeDisponivelWhenDisponivelIsTrueAndHasStock() {
            // Given
            Produto produto = Produto.builder()
                    .disponivel(true)
                    .quantidadeEstoque(5)
                    .build();

            // When & Then
            assertTrue(produto.isDisponivel());
        }

        @Test
        @DisplayName("Should be disponivel when disponivel is true and has infinite stock")
        void shouldBeDisponivelWhenDisponivelIsTrueAndHasInfiniteStock() {
            // Given
            Produto produto = Produto.builder()
                    .disponivel(true)
                    .quantidadeEstoque(-1)
                    .build();

            // When & Then
            assertTrue(produto.isDisponivel());
        }

        @Test
        @DisplayName("Should not be disponivel when disponivel is false")
        void shouldNotBeDisponivelWhenDisponivelIsFalse() {
            // Given
            Produto produto = Produto.builder()
                    .disponivel(false)
                    .quantidadeEstoque(10)
                    .build();

            // When & Then
            assertFalse(produto.isDisponivel());
        }

        @Test
        @DisplayName("Should not be disponivel when has no stock")
        void shouldNotBeDisponivelWhenHasNoStock() {
            // Given
            Produto produto = Produto.builder()
                    .disponivel(true)
                    .quantidadeEstoque(0)
                    .build();

            // When & Then
            assertFalse(produto.isDisponivel());
        }

        @Test
        @DisplayName("Should reduce stock correctly")
        void shouldReduceStockCorrectly() {
            // Given
            Produto produto = Produto.builder()
                    .quantidadeEstoque(10)
                    .build();

            // When
            produto.reduzirEstoque(3);

            // Then
            assertEquals(7, produto.getQuantidadeEstoque());
        }

        @Test
        @DisplayName("Should not reduce stock when infinite stock")
        void shouldNotReduceStockWhenInfiniteStock() {
            // Given
            Produto produto = Produto.builder()
                    .quantidadeEstoque(-1)
                    .build();

            // When
            produto.reduzirEstoque(5);

            // Then
            assertEquals(-1, produto.getQuantidadeEstoque());
        }

        @Test
        @DisplayName("Should not reduce stock when quantidade is null")
        void shouldNotReduceStockWhenQuantidadeIsNull() {
            // Given
            Produto produto = Produto.builder()
                    .quantidadeEstoque(10)
                    .build();

            // When
            produto.reduzirEstoque(null);

            // Then
            assertEquals(10, produto.getQuantidadeEstoque());
        }

        @Test
        @DisplayName("Should increase stock correctly")
        void shouldIncreaseStockCorrectly() {
            // Given
            Produto produto = Produto.builder()
                    .quantidadeEstoque(5)
                    .build();

            // When
            produto.aumentarEstoque(3);

            // Then
            assertEquals(8, produto.getQuantidadeEstoque());
        }

        @Test
        @DisplayName("Should not increase stock when infinite stock")
        void shouldNotIncreaseStockWhenInfiniteStock() {
            // Given
            Produto produto = Produto.builder()
                    .quantidadeEstoque(-1)
                    .build();

            // When
            produto.aumentarEstoque(5);

            // Then
            assertEquals(-1, produto.getQuantidadeEstoque());
        }

        @Test
        @DisplayName("Should not increase stock when quantidade is null")
        void shouldNotIncreaseStockWhenQuantidadeIsNull() {
            // Given
            Produto produto = Produto.builder()
                    .quantidadeEstoque(5)
                    .build();

            // When
            produto.aumentarEstoque(null);

            // Then
            assertEquals(5, produto.getQuantidadeEstoque());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Should be equal when same id")
        void shouldBeEqualWhenSameId() {
            // Given
            Produto produto1 = Produto.builder().id(1L).build();
            Produto produto2 = Produto.builder().id(1L).build();

            // When & Then
            assertEquals(produto1, produto2);
            assertEquals(produto1.hashCode(), produto2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when different id")
        void shouldNotBeEqualWhenDifferentId() {
            // Given
            Produto produto1 = Produto.builder().id(1L).build();
            Produto produto2 = Produto.builder().id(2L).build();

            // When & Then
            assertNotEquals(produto1, produto2);
        }

        @Test
        @DisplayName("Should not be equal when one has null id")
        void shouldNotBeEqualWhenOneHasNullId() {
            // Given
            Produto produto1 = Produto.builder().id(1L).build();
            Produto produto2 = Produto.builder().id(null).build();

            // When & Then
            assertNotEquals(produto1, produto2);
        }
    }
}