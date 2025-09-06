package com.deliverytech.delivery_api.mapper.typemaps;

import com.deliverytech.delivery_api.dto.request.ProdutoRequest;
import com.deliverytech.delivery_api.dto.response.ProdutoResponse;
import com.deliverytech.delivery_api.mapper.converters.RestauranteToResumoConverter;
import com.deliverytech.delivery_api.model.Produto;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
public class ProdutoTypeMap {

  private final ModelMapper modelMapper;
  private final RestauranteToResumoConverter restauranteConverter;

  public ProdutoTypeMap(
      ModelMapper modelMapper, RestauranteToResumoConverter restauranteConverter) {
    this.modelMapper = modelMapper;
    this.restauranteConverter = restauranteConverter;
  }

  @PostConstruct
  public void configure() {
    // Register the converter
    modelMapper.addConverter(restauranteConverter);

    // Produto to ProdutoResponse mapping
    TypeMap<Produto, ProdutoResponse> produtoToResponse =
        modelMapper.createTypeMap(Produto.class, ProdutoResponse.class);

    // Add all our explicit mappings
    produtoToResponse.addMapping(Produto::getId, ProdutoResponse::setId);
    produtoToResponse.addMapping(Produto::getNome, ProdutoResponse::setNome);
    produtoToResponse.addMapping(Produto::getCategoria, ProdutoResponse::setCategoria);
    produtoToResponse.addMapping(Produto::getDescricao, ProdutoResponse::setDescricao);
    produtoToResponse.addMapping(Produto::getPreco, ProdutoResponse::setPreco);
    produtoToResponse.addMapping(Produto::getDisponivel, ProdutoResponse::setDisponivel);
    produtoToResponse.addMapping(
        Produto::getQuantidadeEstoque, ProdutoResponse::setQuantidadeEstoque);

    // Add our custom mapping for restaurante using the converter
    produtoToResponse.addMapping(src -> src.getRestaurante(), ProdutoResponse::setRestaurante);

    // ProdutoRequest to Produto mapping
    TypeMap<ProdutoRequest, Produto> requestToProduto =
        modelMapper.createTypeMap(ProdutoRequest.class, Produto.class);
    requestToProduto.addMapping(ProdutoRequest::getNome, Produto::setNome);
    requestToProduto.addMapping(ProdutoRequest::getCategoria, Produto::setCategoria);
    requestToProduto.addMapping(ProdutoRequest::getDescricao, Produto::setDescricao);
    requestToProduto.addMapping(ProdutoRequest::getPreco, Produto::setPreco);
    requestToProduto.addMapping(ProdutoRequest::getDisponivel, Produto::setDisponivel);
    requestToProduto.addMapping(
        ProdutoRequest::getQuantidadeEstoque, Produto::setQuantidadeEstoque);
  }
}
