package com.deliverytech.delivery_api.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes para Cliente Model")
class ClienteTest {
  @Test
  @DisplayName("Deve criar Cliente com valores padrão corretos")
  void deveCriarClienteComValoresPadrao() {
    Cliente cliente = new Cliente();
    assertThat(cliente.getExcluido()).isFalse();
    assertThat(cliente.isAtivo()).isFalse();
  }

  @Test
  @DisplayName("Deve permitir set/get de todos os campos")
  void devePermitirSetGetCampos() {
    Cliente cliente = new Cliente();
    cliente.setId(10L);
    cliente.setNome("Teste");
    cliente.setEmail("teste@email.com");
    cliente.setTelefone("11999999999");
    cliente.setEndereco("Rua Teste, 123");
    cliente.setAtivo(true);
    cliente.setExcluido(true);
    assertThat(cliente.getId()).isEqualTo(10L);
    assertThat(cliente.getNome()).isEqualTo("Teste");
    assertThat(cliente.getEmail()).isEqualTo("teste@email.com");
    assertThat(cliente.getTelefone()).isEqualTo("11999999999");
    assertThat(cliente.getEndereco()).isEqualTo("Rua Teste, 123");
    assertThat(cliente.isAtivo()).isTrue();
    assertThat(cliente.getExcluido()).isTrue();
  }

  @Test
  @DisplayName("Deve criar cliente usando builder pattern")
  void deveCriarClienteUsandoBuilder() {
    // Given
    String nome = "João Silva";
    String email = "joao.silva@email.com";
    String telefone = "11999999999";
    String endereco = "Rua das Flores, 123";

    // When
    Cliente cliente =
        Cliente.builder()
            .nome(nome)
            .email(email)
            .telefone(telefone)
            .endereco(endereco)
            .ativo(true)
            .excluido(false)
            .build();

    // Then
    assertThat(cliente).isNotNull();
    assertThat(cliente.getNome()).isEqualTo(nome);
    assertThat(cliente.getEmail()).isEqualTo(email);
    assertThat(cliente.getTelefone()).isEqualTo(telefone);
    assertThat(cliente.getEndereco()).isEqualTo(endereco);
    assertThat(cliente.isAtivo()).isTrue();
    assertThat(cliente.getExcluido()).isFalse();
  }

  @Test
  @DisplayName("Deve criar cliente com valores padrão usando builder")
  void deveCriarClienteComValoresPadraoBuilder() {
    // When
    Cliente cliente = Cliente.builder().nome("Maria Santos").email("maria@email.com").build();

    // Then
    assertThat(cliente).isNotNull();
    assertThat(cliente.getNome()).isEqualTo("Maria Santos");
    assertThat(cliente.getEmail()).isEqualTo("maria@email.com");
    assertThat(cliente.getTelefone()).isNull();
    assertThat(cliente.getEndereco()).isNull();
    assertThat(cliente.isAtivo()).isFalse(); // valor padrão de boolean
    assertThat(cliente.getExcluido()).isFalse(); // valor padrão definido no builder
  }

  @Test
  @DisplayName("Deve permitir modificar cliente após criação via builder")
  void devePermitirModificarClienteAposCriacaoViaBuilder() {
    // Given
    Cliente cliente =
        Cliente.builder().nome("Pedro Costa").email("pedro@email.com").ativo(false).build();

    // When
    cliente.setNome("Pedro Costa Silva");
    cliente.setAtivo(true);

    // Then
    assertThat(cliente.getNome()).isEqualTo("Pedro Costa Silva");
    assertThat(cliente.isAtivo()).isTrue();
  }
}
