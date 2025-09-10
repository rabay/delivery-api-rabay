package com.deliverytech.delivery_api.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("RestauranteResponse")
class RestauranteResponseTest {

    @Nested
    @DisplayName("Getters e Setters")
    class GettersSettersTests {

        @Test
        @DisplayName("Deve permitir definir e obter ID")
        void devePermitirDefinirEObterId() {
            // Given
            RestauranteResponse restaurante = new RestauranteResponse();
            Long expectedId = 1L;

            // When
            restaurante.setId(expectedId);
            Long actualId = restaurante.getId();

            // Then
            assertThat(actualId).isEqualTo(expectedId);
        }

        @Test
        @DisplayName("Deve permitir definir e obter nome")
        void devePermitirDefinirEObterNome() {
            // Given
            RestauranteResponse restaurante = new RestauranteResponse();
            String expectedNome = "Restaurante do Zé";

            // When
            restaurante.setNome(expectedNome);
            String actualNome = restaurante.getNome();

            // Then
            assertThat(actualNome).isEqualTo(expectedNome);
        }

        @Test
        @DisplayName("Deve permitir definir e obter categoria")
        void devePermitirDefinirEObterCategoria() {
            // Given
            RestauranteResponse restaurante = new RestauranteResponse();
            String expectedCategoria = "Comida Brasileira";

            // When
            restaurante.setCategoria(expectedCategoria);
            String actualCategoria = restaurante.getCategoria();

            // Then
            assertThat(actualCategoria).isEqualTo(expectedCategoria);
        }

        @Test
        @DisplayName("Deve permitir definir e obter endereco")
        void devePermitirDefinirEObterEndereco() {
            // Given
            RestauranteResponse restaurante = new RestauranteResponse();
            String expectedEndereco = "Rua das Flores, 123 - São Paulo/SP";

            // When
            restaurante.setEndereco(expectedEndereco);
            String actualEndereco = restaurante.getEndereco();

            // Then
            assertThat(actualEndereco).isEqualTo(expectedEndereco);
        }

        @Test
        @DisplayName("Deve permitir definir e obter taxaEntrega")
        void devePermitirDefinirEObterTaxaEntrega() {
            // Given
            RestauranteResponse restaurante = new RestauranteResponse();
            BigDecimal expectedTaxaEntrega = new BigDecimal("5.00");

            // When
            restaurante.setTaxaEntrega(expectedTaxaEntrega);
            BigDecimal actualTaxaEntrega = restaurante.getTaxaEntrega();

            // Then
            assertThat(actualTaxaEntrega).isEqualTo(expectedTaxaEntrega);
        }

        @Test
        @DisplayName("Deve permitir definir e obter telefone")
        void devePermitirDefinirEObterTelefone() {
            // Given
            RestauranteResponse restaurante = new RestauranteResponse();
            String expectedTelefone = "11987654321";

            // When
            restaurante.setTelefone(expectedTelefone);
            String actualTelefone = restaurante.getTelefone();

            // Then
            assertThat(actualTelefone).isEqualTo(expectedTelefone);
        }

        @Test
        @DisplayName("Deve permitir definir e obter email")
        void devePermitirDefinirEObterEmail() {
            // Given
            RestauranteResponse restaurante = new RestauranteResponse();
            String expectedEmail = "contato@restaurantedoze.com";

            // When
            restaurante.setEmail(expectedEmail);
            String actualEmail = restaurante.getEmail();

            // Then
            assertThat(actualEmail).isEqualTo(expectedEmail);
        }

        @Test
        @DisplayName("Deve permitir definir e obter tempoEntregaMinutos")
        void devePermitirDefinirEObterTempoEntregaMinutos() {
            // Given
            RestauranteResponse restaurante = new RestauranteResponse();
            Integer expectedTempoEntregaMinutos = 30;

            // When
            restaurante.setTempoEntregaMinutos(expectedTempoEntregaMinutos);
            Integer actualTempoEntregaMinutos = restaurante.getTempoEntregaMinutos();

            // Then
            assertThat(actualTempoEntregaMinutos).isEqualTo(expectedTempoEntregaMinutos);
        }

        @Test
        @DisplayName("Deve permitir definir e obter avaliacao")
        void devePermitirDefinirEObterAvaliacao() {
            // Given
            RestauranteResponse restaurante = new RestauranteResponse();
            BigDecimal expectedAvaliacao = new BigDecimal("4.5");

            // When
            restaurante.setAvaliacao(expectedAvaliacao);
            BigDecimal actualAvaliacao = restaurante.getAvaliacao();

            // Then
            assertThat(actualAvaliacao).isEqualTo(expectedAvaliacao);
        }

        @Test
        @DisplayName("Deve permitir definir e obter status ativo")
        void devePermitirDefinirEObterStatusAtivo() {
            // Given
            RestauranteResponse restaurante = new RestauranteResponse();
            Boolean expectedAtivo = true;

            // When
            restaurante.setAtivo(expectedAtivo);
            Boolean actualAtivo = restaurante.getAtivo();

            // Then
            assertThat(actualAtivo).isEqualTo(expectedAtivo);
        }
    }

    @Nested
    @DisplayName("Builder Pattern")
    class BuilderPatternTests {

        @Test
        @DisplayName("Deve criar restaurante usando builder pattern")
        void deveCriarRestauranteUsandoBuilderPattern() {
            // When
            RestauranteResponse restaurante = RestauranteResponse.builder()
                    .id(1L)
                    .nome("Restaurante do Zé")
                    .categoria("Comida Brasileira")
                    .endereco("Rua das Flores, 123 - São Paulo/SP")
                    .taxaEntrega(new BigDecimal("5.00"))
                    .telefone("11987654321")
                    .email("contato@restaurantedoze.com")
                    .tempoEntregaMinutos(30)
                    .avaliacao(new BigDecimal("4.5"))
                    .ativo(true)
                    .build();

            // Then
            assertThat(restaurante.getId()).isEqualTo(1L);
            assertThat(restaurante.getNome()).isEqualTo("Restaurante do Zé");
            assertThat(restaurante.getCategoria()).isEqualTo("Comida Brasileira");
            assertThat(restaurante.getEndereco()).isEqualTo("Rua das Flores, 123 - São Paulo/SP");
            assertThat(restaurante.getTaxaEntrega()).isEqualTo(new BigDecimal("5.00"));
            assertThat(restaurante.getTelefone()).isEqualTo("11987654321");
            assertThat(restaurante.getEmail()).isEqualTo("contato@restaurantedoze.com");
            assertThat(restaurante.getTempoEntregaMinutos()).isEqualTo(30);
            assertThat(restaurante.getAvaliacao()).isEqualTo(new BigDecimal("4.5"));
            assertThat(restaurante.getAtivo()).isTrue();
        }

        @Test
        @DisplayName("Deve permitir builder com campos opcionais vazios")
        void devePermitirBuilderComCamposOpcionaisVazios() {
            // When
            RestauranteResponse restaurante = RestauranteResponse.builder()
                    .id(2L)
                    .nome("Restaurante Novo")
                    .categoria("Italiana")
                    .ativo(false)
                    .build();

            // Then
            assertThat(restaurante.getId()).isEqualTo(2L);
            assertThat(restaurante.getNome()).isEqualTo("Restaurante Novo");
            assertThat(restaurante.getCategoria()).isEqualTo("Italiana");
            assertThat(restaurante.getAtivo()).isFalse();
            assertThat(restaurante.getEndereco()).isNull();
            assertThat(restaurante.getTaxaEntrega()).isNull();
            assertThat(restaurante.getTelefone()).isNull();
            assertThat(restaurante.getEmail()).isNull();
            assertThat(restaurante.getTempoEntregaMinutos()).isNull();
            assertThat(restaurante.getAvaliacao()).isNull();
        }

        @Test
        @DisplayName("Deve criar restaurante com builder fluente")
        void deveCriarRestauranteComBuilderFluente() {
            // When
            RestauranteResponse restaurante = RestauranteResponse.builder()
                    .id(6L)
                    .nome("Cafeteria Central")
                    .categoria("Cafeteria")
                    .endereco("Centro da Cidade")
                    .taxaEntrega(new BigDecimal("3.00"))
                    .telefone("1199999999")
                    .email("cafe@central.com")
                    .tempoEntregaMinutos(15)
                    .avaliacao(new BigDecimal("4.2"))
                    .ativo(true)
                    .build();

            // Then
            assertThat(restaurante.getId()).isEqualTo(6L);
            assertThat(restaurante.getNome()).isEqualTo("Cafeteria Central");
            assertThat(restaurante.getCategoria()).isEqualTo("Cafeteria");
            assertThat(restaurante.getEndereco()).isEqualTo("Centro da Cidade");
            assertThat(restaurante.getTaxaEntrega()).isEqualTo(new BigDecimal("3.00"));
            assertThat(restaurante.getTelefone()).isEqualTo("1199999999");
            assertThat(restaurante.getEmail()).isEqualTo("cafe@central.com");
            assertThat(restaurante.getTempoEntregaMinutos()).isEqualTo(15);
            assertThat(restaurante.getAvaliacao()).isEqualTo(new BigDecimal("4.2"));
            assertThat(restaurante.getAtivo()).isTrue();
        }
    }

    @Nested
    @DisplayName("Cenários de Sucesso")
    class SuccessScenariosTests {

        @Test
        @DisplayName("Deve criar restaurante com todos os campos preenchidos")
        void deveCriarRestauranteComTodosOsCamposPreenchidos() {
            // Given
            RestauranteResponse restaurante = new RestauranteResponse();

            // When
            restaurante.setId(1L);
            restaurante.setNome("Restaurante do Zé");
            restaurante.setCategoria("Comida Brasileira");
            restaurante.setEndereco("Rua das Flores, 123 - São Paulo/SP");
            restaurante.setTaxaEntrega(new BigDecimal("5.00"));
            restaurante.setTelefone("11987654321");
            restaurante.setEmail("contato@restaurantedoze.com");
            restaurante.setTempoEntregaMinutos(30);
            restaurante.setAvaliacao(new BigDecimal("4.5"));
            restaurante.setAtivo(true);

            // Then
            assertThat(restaurante.getId()).isEqualTo(1L);
            assertThat(restaurante.getNome()).isEqualTo("Restaurante do Zé");
            assertThat(restaurante.getCategoria()).isEqualTo("Comida Brasileira");
            assertThat(restaurante.getEndereco()).isEqualTo("Rua das Flores, 123 - São Paulo/SP");
            assertThat(restaurante.getTaxaEntrega()).isEqualTo(new BigDecimal("5.00"));
            assertThat(restaurante.getTelefone()).isEqualTo("11987654321");
            assertThat(restaurante.getEmail()).isEqualTo("contato@restaurantedoze.com");
            assertThat(restaurante.getTempoEntregaMinutos()).isEqualTo(30);
            assertThat(restaurante.getAvaliacao()).isEqualTo(new BigDecimal("4.5"));
            assertThat(restaurante.getAtivo()).isTrue();
        }

        @Test
        @DisplayName("Deve criar restaurante inativo")
        void deveCriarRestauranteInativo() {
            // Given
            RestauranteResponse restaurante = new RestauranteResponse();

            // When
            restaurante.setId(2L);
            restaurante.setNome("Restaurante Fechado");
            restaurante.setCategoria("Japonesa");
            restaurante.setEndereco("Av. Paulista, 456 - São Paulo/SP");
            restaurante.setTaxaEntrega(new BigDecimal("8.00"));
            restaurante.setTelefone("11876543210");
            restaurante.setEmail("contato@restaurantefechado.com");
            restaurante.setTempoEntregaMinutos(45);
            restaurante.setAvaliacao(new BigDecimal("3.8"));
            restaurante.setAtivo(false);

            // Then
            assertThat(restaurante.getId()).isEqualTo(2L);
            assertThat(restaurante.getNome()).isEqualTo("Restaurante Fechado");
            assertThat(restaurante.getCategoria()).isEqualTo("Japonesa");
            assertThat(restaurante.getEndereco()).isEqualTo("Av. Paulista, 456 - São Paulo/SP");
            assertThat(restaurante.getTaxaEntrega()).isEqualTo(new BigDecimal("8.00"));
            assertThat(restaurante.getTelefone()).isEqualTo("11876543210");
            assertThat(restaurante.getEmail()).isEqualTo("contato@restaurantefechado.com");
            assertThat(restaurante.getTempoEntregaMinutos()).isEqualTo(45);
            assertThat(restaurante.getAvaliacao()).isEqualTo(new BigDecimal("3.8"));
            assertThat(restaurante.getAtivo()).isFalse();
        }

        @Test
        @DisplayName("Deve permitir restaurante sem avaliacao")
        void devePermitirRestauranteSemAvaliacao() {
            // Given
            RestauranteResponse restaurante = new RestauranteResponse();

            // When
            restaurante.setId(3L);
            restaurante.setNome("Restaurante Novo");
            restaurante.setCategoria("Mexicana");
            restaurante.setEndereco("Rua México, 789 - São Paulo/SP");
            restaurante.setTaxaEntrega(new BigDecimal("6.50"));
            restaurante.setTelefone("11765432109");
            restaurante.setEmail("contato@restaurantenovo.com");
            restaurante.setTempoEntregaMinutos(25);
            restaurante.setAtivo(true);

            // Then
            assertThat(restaurante.getId()).isEqualTo(3L);
            assertThat(restaurante.getNome()).isEqualTo("Restaurante Novo");
            assertThat(restaurante.getCategoria()).isEqualTo("Mexicana");
            assertThat(restaurante.getEndereco()).isEqualTo("Rua México, 789 - São Paulo/SP");
            assertThat(restaurante.getTaxaEntrega()).isEqualTo(new BigDecimal("6.50"));
            assertThat(restaurante.getTelefone()).isEqualTo("11765432109");
            assertThat(restaurante.getEmail()).isEqualTo("contato@restaurantenovo.com");
            assertThat(restaurante.getTempoEntregaMinutos()).isEqualTo(25);
            assertThat(restaurante.getAvaliacao()).isNull();
            assertThat(restaurante.getAtivo()).isTrue();
        }

        @Test
        @DisplayName("Deve criar restaurante com taxa de entrega zero")
        void deveCriarRestauranteComTaxaEntregaZero() {
            // When
            RestauranteResponse restaurante = RestauranteResponse.builder()
                    .id(4L)
                    .nome("Restaurante Fast Food")
                    .categoria("Fast Food")
                    .endereco("Shopping Center")
                    .taxaEntrega(BigDecimal.ZERO)
                    .telefone("1187654321")
                    .email("contato@restaurante.com")
                    .tempoEntregaMinutos(45)
                    .ativo(true)
                    .build();

            // Then
            assertThat(restaurante.getId()).isEqualTo(4L);
            assertThat(restaurante.getNome()).isEqualTo("Restaurante Fast Food");
            assertThat(restaurante.getCategoria()).isEqualTo("Fast Food");
            assertThat(restaurante.getEndereco()).isEqualTo("Shopping Center");
            assertThat(restaurante.getTaxaEntrega()).isEqualTo(BigDecimal.ZERO);
            assertThat(restaurante.getTelefone()).isEqualTo("1187654321");
            assertThat(restaurante.getEmail()).isEqualTo("contato@restaurante.com");
            assertThat(restaurante.getTempoEntregaMinutos()).isEqualTo(45);
            assertThat(restaurante.getAtivo()).isTrue();
        }

        @Test
        @DisplayName("Deve criar restaurante com valores BigDecimal para taxa e avaliação")
        void deveCriarRestauranteComValoresBigDecimal() {
            // Given
            BigDecimal taxaEntrega = new BigDecimal("12.50");
            BigDecimal avaliacao = new BigDecimal("4.8");

            // When
            RestauranteResponse restaurante = RestauranteResponse.builder()
                    .id(5L)
                    .nome("Restaurante Premium")
                    .categoria("Gastronomia")
                    .endereco("Rua Premium, 500")
                    .taxaEntrega(taxaEntrega)
                    .avaliacao(avaliacao)
                    .ativo(true)
                    .build();

            // Then
            assertThat(restaurante.getId()).isEqualTo(5L);
            assertThat(restaurante.getNome()).isEqualTo("Restaurante Premium");
            assertThat(restaurante.getCategoria()).isEqualTo("Gastronomia");
            assertThat(restaurante.getEndereco()).isEqualTo("Rua Premium, 500");
            assertThat(restaurante.getTaxaEntrega()).isEqualTo(new BigDecimal("12.50"));
            assertThat(restaurante.getAvaliacao()).isEqualTo(new BigDecimal("4.8"));
            assertThat(restaurante.getAtivo()).isTrue();
        }
    }
}