package com.deliverytech.delivery_api.mapper.converters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.deliverytech.delivery_api.dto.request.EnderecoRequest;
import com.deliverytech.delivery_api.model.Endereco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.spi.MappingContext;

class AddressConverterTest {

  private AddressConverter converter;
  private AddressReverseConverter reverseConverter;

  @BeforeEach
  void setUp() {
    converter = new AddressConverter();
    reverseConverter = new AddressReverseConverter();
  }

  @Test
  void shouldConvertEnderecoToEnderecoRequest() {
    Endereco endereco = new Endereco();
    endereco.setRua("Rua Teste");
    endereco.setNumero("123");
    endereco.setBairro("Centro");
    endereco.setCidade("São Paulo");
    endereco.setEstado("SP");
    endereco.setCep("01000-000");
    endereco.setComplemento("Apto 101");

    MappingContext<Endereco, EnderecoRequest> context = mock(MappingContext.class);
    when(context.getSource()).thenReturn(endereco);

    EnderecoRequest result = converter.convert(context);

    assertThat(result.getRua()).isEqualTo("Rua Teste");
    assertThat(result.getNumero()).isEqualTo("123");
    assertThat(result.getBairro()).isEqualTo("Centro");
    assertThat(result.getCidade()).isEqualTo("São Paulo");
    assertThat(result.getEstado()).isEqualTo("SP");
    assertThat(result.getCep()).isEqualTo("01000-000");
    assertThat(result.getComplemento()).isEqualTo("Apto 101");
  }

  @Test
  void shouldReturnNullWhenEnderecoIsNull() {
    MappingContext<Endereco, EnderecoRequest> context = mock(MappingContext.class);
    when(context.getSource()).thenReturn(null);

    EnderecoRequest result = converter.convert(context);

    assertThat(result).isNull();
  }

  @Test
  void shouldConvertEnderecoRequestToEndereco() {
    EnderecoRequest request = new EnderecoRequest();
    request.setRua("Rua Teste");
    request.setNumero("123");
    request.setBairro("Centro");
    request.setCidade("São Paulo");
    request.setEstado("SP");
    request.setCep("01000-000");
    request.setComplemento("Apto 101");

    MappingContext<EnderecoRequest, Endereco> context = mock(MappingContext.class);
    when(context.getSource()).thenReturn(request);

    Endereco result = reverseConverter.convert(context);

    assertThat(result.getRua()).isEqualTo("Rua Teste");
    assertThat(result.getNumero()).isEqualTo("123");
    assertThat(result.getBairro()).isEqualTo("Centro");
    assertThat(result.getCidade()).isEqualTo("São Paulo");
    assertThat(result.getEstado()).isEqualTo("SP");
    assertThat(result.getCep()).isEqualTo("01000-000");
    assertThat(result.getComplemento()).isEqualTo("Apto 101");
  }

  @Test
  void shouldReturnNullWhenEnderecoRequestIsNull() {
    MappingContext<EnderecoRequest, Endereco> context = mock(MappingContext.class);
    when(context.getSource()).thenReturn(null);

    Endereco result = reverseConverter.convert(context);

    assertThat(result).isNull();
  }
}
