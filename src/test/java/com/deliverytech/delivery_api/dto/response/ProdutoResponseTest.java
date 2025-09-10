package com.deliverytech.delivery_api.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ProdutoResponse")
class ProdutoResponseTest {

    @Nested
    @DisplayName("Getters e Setters")
    class GettersSettersTests {

        @Test
        @DisplayName("Deve permitir definir e obter ID")
        void devePermitirDefinirEObterId() {
            // Given
            ProdutoResponse produto = new ProdutoResponse();
            Long expectedId = 1L;

            // When
            produto.setId(expectedId);
            Long actualId = produto.getId();

            // Then
            assertThat(actualId).isEqualTo(expectedId);
        }

        @Test
        @DisplayName("Deve permitir definir e obter nome")
        void devePermitirDefinirEObterNome() {
            // Given
            ProdutoResponse produto = new ProdutoResponse();
            String expectedNome = "Pizza Margherita";

            // When
            produto.setNome(expectedNome);
            String actualNome = produto.getNome();

            // Then
            assertThat(actualNome).isEqualTo(expectedNome);
        }

        @Test
        @DisplayName("Deve permitir definir e obter categoria")
        void devePermitirDefinirEObterCategoria() {
            // Given
            ProdutoResponse produto = new ProdutoResponse();
            String expectedCategoria = "Pizzas";

            // When
            produto.setCategoria(expectedCategoria);
            String actualCategoria = produto.getCategoria();

            // Then
            assertThat(actualCategoria).isEqualTo(expectedCategoria);
        }

        @Test
        @DisplayName("Deve permitir definir e obter descricao")
        void devePermitirDefinirEObterDescricao() {
            // Given
            ProdutoResponse produto = new ProdutoResponse();
            String expectedDescricao = "Pizza tradicional com molho de tomate, mussarela e manjericão";

            // When
            produto.setDescricao(expectedDescricao);
            String actualDescricao = produto.getDescricao();

            // Then
            assertThat(actualDescricao).isEqualTo(expectedDescricao);
        }

        @Test
        @DisplayName("Deve permitir definir e obter preco")
        void devePermitirDefinirEObterPreco() {
            // Given
            ProdutoResponse produto = new ProdutoResponse();
            BigDecimal expectedPreco = new BigDecimal("29.90");

            // When
            produto.setPreco(expectedPreco);
            BigDecimal actualPreco = produto.getPreco();

            // Then
            assertThat(actualPreco).isEqualTo(expectedPreco);
        }

        @Test
        @DisplayName("Deve permitir definir e obter restaurante")
        void devePermitirDefinirEObterRestaurante() {
            // Given
            ProdutoResponse produto = new ProdutoResponse();
            RestauranteResumoResponse expectedRestaurante = RestauranteResumoResponse.builder()
                    .id(1L)
                    .nome("Pizzaria Italiana")
                    .build();

            // When
            produto.setRestaurante(expectedRestaurante);
            RestauranteResumoResponse actualRestaurante = produto.getRestaurante();

            // Then
            assertThat(actualRestaurante).isEqualTo(expectedRestaurante);
        }

        @Test
        @DisplayName("Deve permitir definir e obter disponivel")
        void devePermitirDefinirEObterDisponivel() {
            // Given
            ProdutoResponse produto = new ProdutoResponse();
            Boolean expectedDisponivel = true;

            // When
            produto.setDisponivel(expectedDisponivel);
            Boolean actualDisponivel = produto.getDisponivel();

            // Then
            assertThat(actualDisponivel).isEqualTo(expectedDisponivel);
        }

        @Test
        @DisplayName("Deve permitir definir e obter quantidadeEstoque")
        void devePermitirDefinirEObterQuantidadeEstoque() {
            // Given
            ProdutoResponse produto = new ProdutoResponse();
            Integer expectedQuantidadeEstoque = 10;

            // When
            produto.setQuantidadeEstoque(expectedQuantidadeEstoque);
            Integer actualQuantidadeEstoque = produto.getQuantidadeEstoque();

            // Then
            assertThat(actualQuantidadeEstoque).isEqualTo(expectedQuantidadeEstoque);
        }
    }

    @Nested
    @DisplayName("Builder Pattern")
    class BuilderPatternTests {

        @Test
        @DisplayName("Deve criar produto usando builder pattern")
        void deveCriarProdutoUsandoBuilderPattern() {
            // Given
            RestauranteResumoResponse restaurante = RestauranteResumoResponse.builder()
                    .id(1L)
                    .nome("Pizzaria Italiana")
                    .build();

            // When
            ProdutoResponse produto = ProdutoResponse.builder()
                    .id(1L)
                    .nome("Pizza Margherita")
                    .categoria("Pizzas")
                    .descricao("Pizza tradicional com molho de tomate, mussarela e manjericão")
                    .preco(new BigDecimal("29.90"))
                    .restaurante(restaurante)
                    .disponivel(true)
                    .quantidadeEstoque(10)
                    .build();

            // Then
            assertThat(produto.getId()).isEqualTo(1L);
            assertThat(produto.getNome()).isEqualTo("Pizza Margherita");
            assertThat(produto.getCategoria()).isEqualTo("Pizzas");
            assertThat(produto.getDescricao()).isEqualTo("Pizza tradicional com molho de tomate, mussarela e manjericão");
            assertThat(produto.getPreco()).isEqualTo(new BigDecimal("29.90"));
            assertThat(produto.getRestaurante()).isEqualTo(restaurante);
            assertThat(produto.getDisponivel()).isTrue();
            assertThat(produto.getQuantidadeEstoque()).isEqualTo(10);
        }

        @Test
        @DisplayName("Deve permitir builder com campos opcionais vazios")
        void devePermitirBuilderComCamposOpcionaisVazios() {
            // When
            ProdutoResponse produto = ProdutoResponse.builder()
                    .id(2L)
                    .nome("Refrigerante")
                    .categoria("Bebidas")
                    .preco(new BigDecimal("5.00"))
                    .disponivel(false)
                    .build();

            // Then
            assertThat(produto.getId()).isEqualTo(2L);
            assertThat(produto.getNome()).isEqualTo("Refrigerante");
            assertThat(produto.getCategoria()).isEqualTo("Bebidas");
            assertThat(produto.getPreco()).isEqualTo(new BigDecimal("5.00"));
            assertThat(produto.getDisponivel()).isFalse();
            assertThat(produto.getDescricao()).isNull();
            assertThat(produto.getRestaurante()).isNull();
            assertThat(produto.getQuantidadeEstoque()).isNull();
        }

        @Test
        @DisplayName("Deve criar produto com restaurante associado")
        void deveCriarProdutoComRestauranteAssociado() {
            // Given
            RestauranteResumoResponse restaurante = RestauranteResumoResponse.builder()
                    .id(1L)
                    .nome("Pizzaria do Zé")
                    .categoria("Pizzas")
                    .build();

            // When
            ProdutoResponse produto = ProdutoResponse.builder()
                    .id(10L)
                    .nome("Pizza Calabresa")
                    .categoria("Pizzas")
                    .preco(new BigDecimal("35.00"))
                    .restaurante(restaurante)
                    .disponivel(true)
                    .quantidadeEstoque(5)
                    .build();

            // Then
            assertThat(produto.getId()).isEqualTo(10L);
            assertThat(produto.getNome()).isEqualTo("Pizza Calabresa");
            assertThat(produto.getCategoria()).isEqualTo("Pizzas");
            assertThat(produto.getPreco()).isEqualTo(new BigDecimal("35.00"));
            assertThat(produto.getRestaurante()).isNotNull();
            assertThat(produto.getRestaurante().getId()).isEqualTo(1L);
            assertThat(produto.getRestaurante().getNome()).isEqualTo("Pizzaria do Zé");
            assertThat(produto.getRestaurante().getCategoria()).isEqualTo("Pizzas");
            assertThat(produto.getDisponivel()).isTrue();
            assertThat(produto.getQuantidadeEstoque()).isEqualTo(5);
        }
    }

    @Nested
    @DisplayName("Cenários de Sucesso")
    class SuccessScenariosTests {

        @Test
        @DisplayName("Deve criar produto com todos os campos preenchidos")
        void deveCriarProdutoComTodosOsCamposPreenchidos() {
            // Given
            ProdutoResponse produto = new ProdutoResponse();
            RestauranteResumoResponse restaurante = RestauranteResumoResponse.builder()
                    .id(1L)
                    .nome("Pizzaria Italiana")
                    .build();

            // When
            produto.setId(1L);
            produto.setNome("Pizza Margherita");
            produto.setCategoria("Pizzas");
            produto.setDescricao("Pizza tradicional com molho de tomate, mussarela e manjericão");
            produto.setPreco(new BigDecimal("29.90"));
            produto.setRestaurante(restaurante);
            produto.setDisponivel(true);
            produto.setQuantidadeEstoque(10);

            // Then
            assertThat(produto.getId()).isEqualTo(1L);
            assertThat(produto.getNome()).isEqualTo("Pizza Margherita");
            assertThat(produto.getCategoria()).isEqualTo("Pizzas");
            assertThat(produto.getDescricao()).isEqualTo("Pizza tradicional com molho de tomate, mussarela e manjericão");
            assertThat(produto.getPreco()).isEqualTo(new BigDecimal("29.90"));
            assertThat(produto.getRestaurante()).isEqualTo(restaurante);
            assertThat(produto.getDisponivel()).isTrue();
            assertThat(produto.getQuantidadeEstoque()).isEqualTo(10);
        }

        @Test
        @DisplayName("Deve criar produto indisponivel")
        void deveCriarProdutoIndisponivel() {
            // Given
            ProdutoResponse produto = new ProdutoResponse();
            RestauranteResumoResponse restaurante = RestauranteResumoResponse.builder()
                    .id(2L)
                    .nome("Restaurante Mexicano")
                    .build();

            // When
            produto.setId(2L);
            produto.setNome("Taco Especial");
            produto.setCategoria("Mexicana");
            produto.setDescricao("Taco com carne, queijo e vegetais frescos");
            produto.setPreco(new BigDecimal("15.50"));
            produto.setRestaurante(restaurante);
            produto.setDisponivel(false);
            produto.setQuantidadeEstoque(0);

            // Then
            assertThat(produto.getId()).isEqualTo(2L);
            assertThat(produto.getNome()).isEqualTo("Taco Especial");
            assertThat(produto.getCategoria()).isEqualTo("Mexicana");
            assertThat(produto.getDescricao()).isEqualTo("Taco com carne, queijo e vegetais frescos");
            assertThat(produto.getPreco()).isEqualTo(new BigDecimal("15.50"));
            assertThat(produto.getRestaurante()).isEqualTo(restaurante);
            assertThat(produto.getDisponivel()).isFalse();
            assertThat(produto.getQuantidadeEstoque()).isEqualTo(0);
        }

        @Test
        @DisplayName("Deve criar produto sem estoque")
        void deveCriarProdutoSemEstoque() {
            // Given
            ProdutoResponse produto = new ProdutoResponse();
            RestauranteResumoResponse restaurante = RestauranteResumoResponse.builder()
                    .id(3L)
                    .nome("Cafeteria Central")
                    .build();

            // When
            produto.setId(3L);
            produto.setNome("Café Expresso");
            produto.setCategoria("Bebidas");
            produto.setDescricao("Café expresso tradicional");
            produto.setPreco(new BigDecimal("4.50"));
            produto.setRestaurante(restaurante);
            produto.setDisponivel(true);
            produto.setQuantidadeEstoque(0);

            // Then
            assertThat(produto.getId()).isEqualTo(3L);
            assertThat(produto.getNome()).isEqualTo("Café Expresso");
            assertThat(produto.getCategoria()).isEqualTo("Bebidas");
            assertThat(produto.getDescricao()).isEqualTo("Café expresso tradicional");
            assertThat(produto.getPreco()).isEqualTo(new BigDecimal("4.50"));
            assertThat(produto.getRestaurante()).isEqualTo(restaurante);
            assertThat(produto.getDisponivel()).isTrue();
            assertThat(produto.getQuantidadeEstoque()).isEqualTo(0);
        }

        @Test
        @DisplayName("Deve criar produto com preco zero")
        void deveCriarProdutoComPrecoZero() {
            // Given
            ProdutoResponse produto = new ProdutoResponse();
            RestauranteResumoResponse restaurante = RestauranteResumoResponse.builder()
                    .id(4L)
                    .nome("Restaurante Popular")
                    .build();

            // When
            produto.setId(4L);
            produto.setNome("Água Mineral");
            produto.setCategoria("Bebidas");
            produto.setDescricao("Água mineral sem gás");
            produto.setPreco(BigDecimal.ZERO);
            produto.setRestaurante(restaurante);
            produto.setDisponivel(true);
            produto.setQuantidadeEstoque(100);

            // Then
            assertThat(produto.getId()).isEqualTo(4L);
            assertThat(produto.getNome()).isEqualTo("Água Mineral");
            assertThat(produto.getCategoria()).isEqualTo("Bebidas");
            assertThat(produto.getDescricao()).isEqualTo("Água mineral sem gás");
            assertThat(produto.getPreco()).isEqualTo(BigDecimal.ZERO);
            assertThat(produto.getRestaurante()).isEqualTo(restaurante);
            assertThat(produto.getDisponivel()).isTrue();
            assertThat(produto.getQuantidadeEstoque()).isEqualTo(100);
        }

        @Test
        @DisplayName("Deve criar produto com valores BigDecimal para preco")
        void deveCriarProdutoComValoresBigDecimal() {
            // Given
            BigDecimal preco = new BigDecimal("99.99");

            // When
            ProdutoResponse produto = ProdutoResponse.builder()
                    .id(5L)
                    .nome("Prato Executivo")
                    .categoria("Pratos Executivos")
                    .descricao("Prato completo com entrada, prato principal e sobremesa")
                    .preco(preco)
                    .restaurante(RestauranteResumoResponse.builder().id(5L).nome("Restaurante Executivo").build())
                    .disponivel(true)
                    .quantidadeEstoque(5)
                    .build();

            // Then
            assertThat(produto.getId()).isEqualTo(5L);
            assertThat(produto.getNome()).isEqualTo("Prato Executivo");
            assertThat(produto.getCategoria()).isEqualTo("Pratos Executivos");
            assertThat(produto.getDescricao()).isEqualTo("Prato completo com entrada, prato principal e sobremesa");
            assertThat(produto.getPreco()).isEqualTo(new BigDecimal("99.99"));
            assertThat(produto.getRestaurante().getId()).isEqualTo(5L);
            assertThat(produto.getRestaurante().getNome()).isEqualTo("Restaurante Executivo");
            assertThat(produto.getDisponivel()).isTrue();
            assertThat(produto.getQuantidadeEstoque()).isEqualTo(5);
        }

        @Test
        @DisplayName("Deve criar produto indisponivel com estoque zero")
        void deveCriarProdutoIndisponivelComEstoqueZero() {
            // When
            ProdutoResponse produto = ProdutoResponse.builder()
                    .id(6L)
                    .nome("Produto Esgotado")
                    .categoria("Esgotado")
                    .preco(new BigDecimal("10.00"))
                    .disponivel(false)
                    .quantidadeEstoque(0)
                    .build();

            // Then
            assertThat(produto.getId()).isEqualTo(6L);
            assertThat(produto.getNome()).isEqualTo("Produto Esgotado");
            assertThat(produto.getCategoria()).isEqualTo("Esgotado");
            assertThat(produto.getPreco()).isEqualTo(new BigDecimal("10.00"));
            assertThat(produto.getDisponivel()).isFalse();
            assertThat(produto.getQuantidadeEstoque()).isEqualTo(0);
        }

        @Test
        @DisplayName("Deve criar produto sem restaurante associado")
        void deveCriarProdutoSemRestauranteAssociado() {
            // When
            ProdutoResponse produto = ProdutoResponse.builder()
                    .id(7L)
                    .nome("Produto Genérico")
                    .categoria("Genérico")
                    .preco(new BigDecimal("15.00"))
                    .disponivel(true)
                    .quantidadeEstoque(100)
                    .build();

            // Then
            assertThat(produto.getId()).isEqualTo(7L);
            assertThat(produto.getNome()).isEqualTo("Produto Genérico");
            assertThat(produto.getCategoria()).isEqualTo("Genérico");
            assertThat(produto.getPreco()).isEqualTo(new BigDecimal("15.00"));
            assertThat(produto.getDisponivel()).isTrue();
            assertThat(produto.getQuantidadeEstoque()).isEqualTo(100);
            assertThat(produto.getRestaurante()).isNull();
        }
    }
}