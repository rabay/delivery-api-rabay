package com.deliverytech.delivery_api.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ClienteResponse")
class ClienteResponseTest {

  @Nested
  @DisplayName("Getters e Setters")
  class GettersSettersTests {

    @Test
    @DisplayName("Deve permitir definir e obter ID")
    void devePermitirDefinirEObterId() {
      // Given
      ClienteResponse cliente = new ClienteResponse();
      Long expectedId = 1L;

      // When
      cliente.setId(expectedId);
      Long actualId = cliente.getId();

      // Then
      assertThat(actualId).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("Deve permitir definir e obter nome")
    void devePermitirDefinirEObterNome() {
      // Given
      ClienteResponse cliente = new ClienteResponse();
      String expectedNome = "João Silva";

      // When
      cliente.setNome(expectedNome);
      String actualNome = cliente.getNome();

      // Then
      assertThat(actualNome).isEqualTo(expectedNome);
    }

    @Test
    @DisplayName("Deve permitir definir e obter email")
    void devePermitirDefinirEObterEmail() {
      // Given
      ClienteResponse cliente = new ClienteResponse();
      String expectedEmail = "joao.silva@example.com";

      // When
      cliente.setEmail(expectedEmail);
      String actualEmail = cliente.getEmail();

      // Then
      assertThat(actualEmail).isEqualTo(expectedEmail);
    }

    @Test
    @DisplayName("Deve permitir definir e obter telefone")
    void devePermitirDefinirEObterTelefone() {
      // Given
      ClienteResponse cliente = new ClienteResponse();
      String expectedTelefone = "11987654321";

      // When
      cliente.setTelefone(expectedTelefone);
      String actualTelefone = cliente.getTelefone();

      // Then
      assertThat(actualTelefone).isEqualTo(expectedTelefone);
    }

    @Test
    @DisplayName("Deve permitir definir e obter endereco")
    void devePermitirDefinirEObterEndereco() {
      // Given
      ClienteResponse cliente = new ClienteResponse();
      String expectedEndereco = "Rua das Flores, 123 - São Paulo/SP";

      // When
      cliente.setEndereco(expectedEndereco);
      String actualEndereco = cliente.getEndereco();

      // Then
      assertThat(actualEndereco).isEqualTo(expectedEndereco);
    }

    @Test
    @DisplayName("Deve permitir definir e obter status ativo")
    void devePermitirDefinirEObterStatusAtivo() {
      // Given
      ClienteResponse cliente = new ClienteResponse();
      boolean expectedAtivo = true;

      // When
      cliente.setAtivo(expectedAtivo);
      boolean actualAtivo = cliente.isAtivo();

      // Then
      assertThat(actualAtivo).isEqualTo(expectedAtivo);
    }
  }

  @Nested
  @DisplayName("Cenários de Sucesso")
  class SuccessScenariosTests {

    @Test
    @DisplayName("Deve criar cliente com todos os campos preenchidos")
    void deveCriarClienteComTodosOsCamposPreenchidos() {
      // Given
      ClienteResponse cliente = new ClienteResponse();

      // When
      cliente.setId(1L);
      cliente.setNome("João Silva");
      cliente.setEmail("joao.silva@example.com");
      cliente.setTelefone("11987654321");
      cliente.setEndereco("Rua das Flores, 123 - São Paulo/SP");
      cliente.setAtivo(true);

      // Then
      assertThat(cliente.getId()).isEqualTo(1L);
      assertThat(cliente.getNome()).isEqualTo("João Silva");
      assertThat(cliente.getEmail()).isEqualTo("joao.silva@example.com");
      assertThat(cliente.getTelefone()).isEqualTo("11987654321");
      assertThat(cliente.getEndereco()).isEqualTo("Rua das Flores, 123 - São Paulo/SP");
      assertThat(cliente.isAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deve criar cliente inativo")
    void deveCriarClienteInativo() {
      // Given
      ClienteResponse cliente = new ClienteResponse();

      // When
      cliente.setId(2L);
      cliente.setNome("Maria Santos");
      cliente.setEmail("maria.santos@example.com");
      cliente.setTelefone("11876543210");
      cliente.setEndereco("Av. Paulista, 456 - São Paulo/SP");
      cliente.setAtivo(false);

      // Then
      assertThat(cliente.getId()).isEqualTo(2L);
      assertThat(cliente.getNome()).isEqualTo("Maria Santos");
      assertThat(cliente.getEmail()).isEqualTo("maria.santos@example.com");
      assertThat(cliente.getTelefone()).isEqualTo("11876543210");
      assertThat(cliente.getEndereco()).isEqualTo("Av. Paulista, 456 - São Paulo/SP");
      assertThat(cliente.isAtivo()).isFalse();
    }

    @Test
    @DisplayName("Deve permitir cliente sem endereco definido")
    void devePermitirClienteSemEnderecoDefinido() {
      // Given
      ClienteResponse cliente = new ClienteResponse();

      // When
      cliente.setId(3L);
      cliente.setNome("Pedro Costa");
      cliente.setEmail("pedro.costa@example.com");
      cliente.setTelefone("11765432109");
      cliente.setAtivo(true);

      // Then
      assertThat(cliente.getId()).isEqualTo(3L);
      assertThat(cliente.getNome()).isEqualTo("Pedro Costa");
      assertThat(cliente.getEmail()).isEqualTo("pedro.costa@example.com");
      assertThat(cliente.getTelefone()).isEqualTo("11765432109");
      assertThat(cliente.getEndereco()).isNull();
      assertThat(cliente.isAtivo()).isTrue();
    }
  }
}
