package com.deliverytech.delivery_api.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Testes para PedidoRequest")
class PedidoRequestTest {

  private Validator validator;

  @BeforeEach
  void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Nested
  @DisplayName("Cenários de sucesso")
  class CenariosSucesso {

    @Test
    @DisplayName("Deve criar PedidoRequest válido com todos os campos obrigatórios")
    void deveCriarPedidoRequestValido() {
      // Given
      EnderecoRequest endereco = new EnderecoRequest();
      endereco.setRua("Rua Teste");
      endereco.setNumero("123");
      endereco.setBairro("Centro");
      endereco.setCidade("São Paulo");
      endereco.setEstado("SP");
      endereco.setCep("01234-567");

      ItemPedidoRequest item = new ItemPedidoRequest();
      item.setProdutoId(1L);
      item.setQuantidade(2);

      PedidoRequest request = new PedidoRequest();
      request.setClienteId(1L);
      request.setRestauranteId(2L);
      request.setEnderecoEntrega(endereco);
      request.setItens(List.of(item));
      request.setDesconto(BigDecimal.valueOf(5.00));

      // When
      Set<ConstraintViolation<PedidoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
      assertThat(request.getClienteId()).isEqualTo(1L);
      assertThat(request.getRestauranteId()).isEqualTo(2L);
      assertThat(request.getEnderecoEntrega()).isEqualTo(endereco);
      assertThat(request.getItens()).hasSize(1);
      assertThat(request.getDesconto()).isEqualByComparingTo(BigDecimal.valueOf(5.00));
    }

    @Test
    @DisplayName("Deve criar PedidoRequest válido sem desconto")
    void deveCriarPedidoRequestValidoSemDesconto() {
      // Given
      EnderecoRequest endereco = new EnderecoRequest();
      endereco.setRua("Rua Teste");
      endereco.setNumero("123");
      endereco.setBairro("Centro");
      endereco.setCidade("São Paulo");
      endereco.setEstado("SP");
      endereco.setCep("01234-567");

      ItemPedidoRequest item = new ItemPedidoRequest();
      item.setProdutoId(1L);
      item.setQuantidade(1);

      PedidoRequest request = new PedidoRequest();
      request.setClienteId(1L);
      request.setRestauranteId(2L);
      request.setEnderecoEntrega(endereco);
      request.setItens(List.of(item));

      // When
      Set<ConstraintViolation<PedidoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
      assertThat(request.getDesconto()).isNull();
    }
  }

  @Nested
  @DisplayName("Cenários de erro - Cliente ID")
  class CenariosErroClienteId {

    @Test
    @DisplayName("Deve rejeitar PedidoRequest com clienteId nulo")
    void deveRejeitarClienteIdNulo() {
      // Given
      PedidoRequest request = criarPedidoRequestBasico();
      request.setClienteId(null);

      // When
      Set<ConstraintViolation<PedidoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("must not be null");
    }
  }

  @Nested
  @DisplayName("Cenários de erro - Restaurante ID")
  class CenariosErroRestauranteId {

    @Test
    @DisplayName("Deve rejeitar PedidoRequest com restauranteId nulo")
    void deveRejeitarRestauranteIdNulo() {
      // Given
      PedidoRequest request = criarPedidoRequestBasico();
      request.setRestauranteId(null);

      // When
      Set<ConstraintViolation<PedidoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("must not be null");
    }
  }

  @Nested
  @DisplayName("Cenários de erro - Endereço de entrega")
  class CenariosErroEndereco {

    @Test
    @DisplayName("Deve rejeitar PedidoRequest com enderecoEntrega nulo")
    void deveRejeitarEnderecoEntregaNulo() {
      // Given
      PedidoRequest request = criarPedidoRequestBasico();
      request.setEnderecoEntrega(null);

      // When
      Set<ConstraintViolation<PedidoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage()).isEqualTo("must not be null");
    }
  }

  @Nested
  @DisplayName("Cenários de erro - Itens do pedido")
  class CenariosErroItens {

    @Test
    @DisplayName("Deve rejeitar PedidoRequest com itens nulo")
    void deveRejeitarItensNulo() {
      // Given
      PedidoRequest request = criarPedidoRequestBasico();
      request.setItens(null);

      // When
      Set<ConstraintViolation<PedidoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSizeGreaterThanOrEqualTo(1);
      // Pode ter tanto @NotNull quanto @NotEmpty sendo validadas
      var messages = violations.stream().map(ConstraintViolation::getMessage).toList();
      assertThat(messages).contains("must not be null");
    }

    @Test
    @DisplayName("Deve rejeitar PedidoRequest com lista de itens vazia")
    void deveRejeitarItensVazia() {
      // Given
      PedidoRequest request = criarPedidoRequestBasico();
      request.setItens(List.of());

      // When
      Set<ConstraintViolation<PedidoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSize(1);
      assertThat(violations.iterator().next().getMessage())
          .contains("Pedido deve conter ao menos um item");
    }

    @Test
    @DisplayName("Deve validar itens do pedido quando inválidos")
    void deveValidarItensQuandoInvalidos() {
      // Given
      ItemPedidoRequest itemInvalido = new ItemPedidoRequest();
      // produtoId nulo - deve causar erro de validação

      PedidoRequest request = criarPedidoRequestBasico();
      request.setItens(List.of(itemInvalido));

      // When
      Set<ConstraintViolation<PedidoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isNotEmpty();
      // Deve ter pelo menos uma violação relacionada aos itens
    }
  }

  @Nested
  @DisplayName("Cenários de erro - Múltiplas validações")
  class CenariosErroMultiplasValidacoes {

    @Test
    @DisplayName("Deve acumular múltiplas violações de validação")
    void deveAcumularMultiplasViolacoes() {
      // Given
      PedidoRequest request = new PedidoRequest();
      // Todos os campos obrigatórios são nulos

      // When
      Set<ConstraintViolation<PedidoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).hasSizeGreaterThan(1);
      // Deve ter violações para clienteId, restauranteId, enderecoEntrega e itens
    }
  }

  @Nested
  @DisplayName("Cenários de desconto")
  class CenariosDesconto {

    @Test
    @DisplayName("Deve aceitar desconto zero")
    void deveAceitarDescontoZero() {
      // Given
      PedidoRequest request = criarPedidoRequestBasico();
      request.setDesconto(BigDecimal.ZERO);

      // When
      Set<ConstraintViolation<PedidoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
      assertThat(request.getDesconto()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Deve aceitar desconto positivo")
    void deveAceitarDescontoPositivo() {
      // Given
      PedidoRequest request = criarPedidoRequestBasico();
      request.setDesconto(BigDecimal.valueOf(15.50));

      // When
      Set<ConstraintViolation<PedidoRequest>> violations = validator.validate(request);

      // Then
      assertThat(violations).isEmpty();
      assertThat(request.getDesconto()).isEqualByComparingTo(BigDecimal.valueOf(15.50));
    }
  }

  // Método auxiliar para criar um PedidoRequest básico válido
  private PedidoRequest criarPedidoRequestBasico() {
    EnderecoRequest endereco = new EnderecoRequest();
    endereco.setRua("Rua Teste");
    endereco.setNumero("123");
    endereco.setBairro("Centro");
    endereco.setCidade("São Paulo");
    endereco.setEstado("SP");
    endereco.setCep("01234-567");

    ItemPedidoRequest item = new ItemPedidoRequest();
    item.setProdutoId(1L);
    item.setQuantidade(1);

    PedidoRequest request = new PedidoRequest();
    request.setClienteId(1L);
    request.setRestauranteId(2L);
    request.setEnderecoEntrega(endereco);
    request.setItens(List.of(item));

    return request;
  }
}
