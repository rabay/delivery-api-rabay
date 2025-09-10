package com.deliverytech.delivery_api.mapper.typemaps;

import static org.assertj.core.api.Assertions.assertThat;

import com.deliverytech.delivery_api.dto.response.ProdutoResponse;
import com.deliverytech.delivery_api.mapper.converters.RestauranteToResumoConverter;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

class ProdutoTypeMapTest {

  private ModelMapper modelMapper;

  @BeforeEach
  void setUp() {
    modelMapper = new ModelMapper();
    new ProdutoTypeMap(modelMapper, new RestauranteToResumoConverter()).configure();
  }

  @Test
  void shouldMapProdutoToProdutoResponse() {
    Restaurante restaurante = new Restaurante();
    restaurante.setId(1L);
    restaurante.setNome("Restaurante Teste");
    restaurante.setCategoria("Fast Food");
    restaurante.setTaxaEntrega(new BigDecimal("5.00"));
    restaurante.setTempoEntregaMinutos(30);
    restaurante.setAvaliacao(new BigDecimal("4.5"));
    restaurante.setAtivo(true);

    Produto produto = new Produto();
    produto.setId(1L);
    produto.setNome("Produto Teste");
    produto.setCategoria("Lanche");
    produto.setDescricao("Descrição do produto teste");
    produto.setPreco(new BigDecimal("19.90"));
    produto.setDisponivel(true);
    produto.setQuantidadeEstoque(10);
    produto.setRestaurante(restaurante);

    ProdutoResponse response = modelMapper.map(produto, ProdutoResponse.class);

    assertThat(response.getId()).isEqualTo(produto.getId());
    assertThat(response.getNome()).isEqualTo(produto.getNome());
    assertThat(response.getCategoria()).isEqualTo(produto.getCategoria());
    assertThat(response.getDescricao()).isEqualTo(produto.getDescricao());
    assertThat(response.getPreco()).isEqualTo(produto.getPreco());
    assertThat(response.getDisponivel()).isEqualTo(produto.getDisponivel());
    assertThat(response.getQuantidadeEstoque()).isEqualTo(produto.getQuantidadeEstoque());

    // Check restaurante mapping
    assertThat(response.getRestaurante()).isNotNull();
    assertThat(response.getRestaurante().getId()).isEqualTo(restaurante.getId());
    assertThat(response.getRestaurante().getNome()).isEqualTo(restaurante.getNome());
    assertThat(response.getRestaurante().getCategoria()).isEqualTo(restaurante.getCategoria());
    assertThat(response.getRestaurante().getTaxaEntrega()).isEqualTo(restaurante.getTaxaEntrega());
    assertThat(response.getRestaurante().getTempoEntregaMinutos())
        .isEqualTo(restaurante.getTempoEntregaMinutos());
    assertThat(response.getRestaurante().getAvaliacao()).isEqualTo(restaurante.getAvaliacao());
    assertThat(response.getRestaurante().getAtivo()).isEqualTo(restaurante.isAtivo());
  }
}
