package com.deliverytech.delivery_api.service.impl;

import com.deliverytech.delivery_api.dto.request.ProdutoRequest;
import com.deliverytech.delivery_api.dto.response.ProdutoResponse;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.exception.EstoqueInsuficienteException;
import com.deliverytech.delivery_api.exception.ProdutoIndisponivelException;
import com.deliverytech.delivery_api.mapper.ProdutoMapper;
import com.deliverytech.delivery_api.model.ItemPedido;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.service.ProdutoService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProdutoServiceImpl implements ProdutoService {
  private final ProdutoRepository produtoRepository;
  private final ProdutoMapper produtoMapper;

  public ProdutoServiceImpl(ProdutoRepository produtoRepository, ProdutoMapper produtoMapper) {
    this.produtoRepository = produtoRepository;
    this.produtoMapper = produtoMapper;
  }

  @Override
  public Produto cadastrar(Produto produto) {
    if (produto.getCategoria() == null || produto.getCategoria().isEmpty()) {
      throw new BusinessException("Categoria obrigatória");
    }
    if (produto.getRestaurante() == null) {
      throw new BusinessException("Restaurante obrigatório");
    }
    if (produto.getNome() == null || produto.getNome().isEmpty()) {
      throw new BusinessException("Nome obrigatório");
    }
    // Validate stock quantity is provided
    if (produto.getQuantidadeEstoque() == null) {
      throw new BusinessException("Quantidade em estoque é obrigatória");
    }
    return produtoRepository.save(produto);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Produto> buscarPorRestaurante(Restaurante restaurante) {
    return produtoRepository.findByRestauranteAndExcluidoFalse(restaurante);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Produto> buscarDisponiveisEntities() {
    return produtoRepository.findByDisponivelTrueAndExcluidoFalse();
  }

  @Transactional(readOnly = true)
  @Deprecated
  public Optional<Produto> buscarPorId(Long id) {
    return produtoRepository.findById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Produto> listarTodos() {
    return produtoRepository.findAll();
  }

  @Override
  public Produto atualizar(Long id, Produto produtoAtualizado) {
    Produto existente =
        produtoRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Produto", id));
    existente.setNome(produtoAtualizado.getNome());
    existente.setCategoria(produtoAtualizado.getCategoria());
    existente.setDisponivel(produtoAtualizado.getDisponivel());
    existente.setRestaurante(produtoAtualizado.getRestaurante());
    // Update stock quantity if provided
    if (produtoAtualizado.getQuantidadeEstoque() != null) {
      existente.setQuantidadeEstoque(produtoAtualizado.getQuantidadeEstoque());
    }
    // Adicione outros campos conforme necessário
    return produtoRepository.save(existente);
  }

  @Override
  public void inativar(Long id) {
    Produto produto =
        produtoRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Produto", id));
    produto.setDisponivel(false);
    produtoRepository.save(produto);
  }

  @Override
  public void deletar(Long id) {
    Produto produto =
        produtoRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Produto", id));
    produto.setDisponivel(false);
    produto.setExcluido(true);
    produtoRepository.save(produto);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Produto> buscarPorCategoria(String categoria) {
    return produtoRepository.findByCategoriaAndExcluidoFalse(categoria);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Produto> buscarPorNome(String nome) {
    // Se existir método customizado, usar:
    // return produtoRepository.findByNomeContainingIgnoreCaseAndExcluidoFalse(nome);
    List<Produto> todos = produtoRepository.findByDisponivelTrueAndExcluidoFalse();
    return todos.stream()
        .filter(p -> p.getNome() != null && p.getNome().toLowerCase().contains(nome.toLowerCase()))
        .toList();
  }

  @Override
  public void alterarDisponibilidade(Long id, boolean disponivel) {
    Produto produto =
        produtoRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Produto", id));
    produto.setDisponivel(disponivel);
    produtoRepository.save(produto);
  }

  @Override
  public void validarPreco(java.math.BigDecimal preco) {
    if (preco == null || preco.compareTo(java.math.BigDecimal.ZERO) <= 0) {
      throw new BusinessException("Preço inválido");
    }
  }

  // ===== NOVOS MÉTODOS COM DTOs =====

  @Override
  public ProdutoResponse cadastrar(ProdutoRequest produtoRequest) {
    Produto produto = produtoMapper.toEntity(produtoRequest);
    Produto salvo = produtoRepository.save(produto);
    return produtoMapper.toResponse(salvo);
  }

  @Override
  @Transactional(readOnly = true)
  public ProdutoResponse buscarProdutoPorId(Long id) {
    Produto produto =
        produtoRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Produto", id));

    // Validate not soft deleted
    if (Boolean.TRUE.equals(produto.getExcluido())) {
      throw new BusinessException("Produto foi excluído do sistema");
    }

    return produtoMapper.toResponse(produto);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProdutoResponse> buscarProdutosPorRestaurante(Long restauranteId) {
    List<Produto> produtos = produtoRepository.findByRestauranteIdAndExcluidoFalse(restauranteId);
    return produtos.stream().map(produtoMapper::toResponse).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public org.springframework.data.domain.Page<ProdutoResponse> buscarProdutosPorRestaurante(
      Long restauranteId, org.springframework.data.domain.Pageable pageable) {
    var page = produtoRepository.findByRestauranteIdAndExcluidoFalse(restauranteId, pageable);
    return page.map(produtoMapper::toResponse);
  }

  @Override
  public ProdutoResponse atualizar(Long id, ProdutoRequest produtoRequest) {
    Produto existente =
        produtoRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Produto", id));

    // Validate not soft deleted
    if (Boolean.TRUE.equals(existente.getExcluido())) {
      throw new BusinessException("Não é possível atualizar produto excluído");
    }

    // Update fields
    existente.setNome(produtoRequest.getNome());
    existente.setCategoria(produtoRequest.getCategoria());
    existente.setDescricao(produtoRequest.getDescricao());
    existente.setPreco(produtoRequest.getPreco());
    existente.setDisponivel(produtoRequest.getDisponivel());
    // Update stock quantity
    existente.setQuantidadeEstoque(produtoRequest.getQuantidadeEstoque());

    // Update restaurant if changed
    if (!existente.getRestaurante().getId().equals(produtoRequest.getRestauranteId())) {
      Produto temp = produtoMapper.toEntity(produtoRequest);
      existente.setRestaurante(temp.getRestaurante());
    }

    Produto atualizado = produtoRepository.save(existente);
    return produtoMapper.toResponse(atualizado);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProdutoResponse> buscarProdutosPorCategoria(String categoria) {
    List<Produto> produtos = produtoRepository.findByCategoriaAndExcluidoFalse(categoria);
    return produtos.stream().map(produtoMapper::toResponse).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProdutoResponse> buscarDisponiveis() {
    List<Produto> produtos = produtoRepository.findByDisponivelTrueAndExcluidoFalse();
    return produtos.stream().map(produtoMapper::toResponse).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public org.springframework.data.domain.Page<ProdutoResponse> buscarDisponiveis(
      org.springframework.data.domain.Pageable pageable) {
    var page = produtoRepository.findByDisponivelTrueAndExcluidoFalse(pageable);
    return page.map(produtoMapper::toResponse);
  }

  @Override
  @Transactional(readOnly = true)
  public org.springframework.data.domain.Page<ProdutoResponse> buscarProdutosPorCategoria(
      String categoria, org.springframework.data.domain.Pageable pageable) {
    var page = produtoRepository.findByCategoriaAndExcluidoFalse(categoria, pageable);
    return page.map(produtoMapper::toResponse);
  }

  @Override
  @Transactional(readOnly = true)
  public org.springframework.data.domain.Page<ProdutoResponse> buscarProdutosPorNome(
      String nome, org.springframework.data.domain.Pageable pageable) {
    var page = produtoRepository.findByNomeContainingIgnoreCaseAndExcluidoFalse(nome, pageable);
    return page.map(produtoMapper::toResponse);
  }

  // ===== MÉTODOS PARA CONTROLE DE ESTOQUE =====

  @Override
  public void validarEstoque(Produto produto, Integer quantidadeSolicitada) {
    // If product is not available, throw exception
    if (!produto.isDisponivel()) {
      throw new ProdutoIndisponivelException("Produto não está disponível: " + produto.getNome());
    }

    // If product has infinite stock, no validation needed
    if (produto.isInfiniteStock()) {
      return;
    }

    // If product has zero stock, throw exception
    if (produto.getQuantidadeEstoque() == 0) {
      throw new ProdutoIndisponivelException("Produto fora de estoque: " + produto.getNome());
    }

    // If requested quantity exceeds available stock, throw exception
    if (quantidadeSolicitada > produto.getQuantidadeEstoque()) {
      throw new EstoqueInsuficienteException(
          String.format(
              "Estoque insuficiente para o produto %s. Disponível: %d, Solicitado: %d",
              produto.getNome(), produto.getQuantidadeEstoque(), quantidadeSolicitada));
    }
  }

  @Override
  @Transactional
  public void atualizarEstoque(Long produtoId, Integer novaQuantidade) {
    Produto produto =
        produtoRepository
            .findById(produtoId)
            .orElseThrow(() -> new EntityNotFoundException("Produto", produtoId));

    produto.setQuantidadeEstoque(novaQuantidade);
    produtoRepository.save(produto);
  }

  @Override
  @Transactional
  public void ajustarEstoque(Long produtoId, Integer quantidade) {
    Produto produto =
        produtoRepository
            .findById(produtoId)
            .orElseThrow(() -> new EntityNotFoundException("Produto", produtoId));

    // Only adjust stock for non-infinite stock products
    if (!produto.isInfiniteStock()) {
      produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + quantidade);
      produtoRepository.save(produto);
    }
  }

  @Override
  @Transactional
  public void reservarEstoque(com.deliverytech.delivery_api.model.Pedido pedido) {
    // Reserve stock for all items in the order
    for (ItemPedido item : pedido.getItens()) {
      Produto produto = item.getProduto();

      // Validate stock availability
      validarEstoque(produto, item.getQuantidade());

      // Reserve stock (reduce available quantity)
      // Only for non-infinite stock products
      if (!produto.isInfiniteStock()) {
        produto.reduzirEstoque(item.getQuantidade());
        produtoRepository.save(produto);
      }
    }
  }

  @Override
  @Transactional
  public void confirmarEstoque(com.deliverytech.delivery_api.model.Pedido pedido) {
    // Confirm stock reduction for all items in the order
    // In this implementation, stock is already reduced during reservation
    // This method is kept for future extensions or different stock management patterns
  }

  @Override
  @Transactional
  public void cancelarReservaEstoque(com.deliverytech.delivery_api.model.Pedido pedido) {
    // Release reserved stock back to available stock
    for (ItemPedido item : pedido.getItens()) {
      Produto produto = item.getProduto();

      // Only restore stock for non-infinite stock products
      if (!produto.isInfiniteStock()) {
        produto.aumentarEstoque(item.getQuantidade());
        produtoRepository.save(produto);
      }
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProdutoResponse> buscarProdutosPorNome(String nome) {
    List<Produto> produtos = produtoRepository.findByNomeContainingIgnoreCaseAndExcluidoFalse(nome);
    return produtos.stream()
        .map(produtoMapper::toResponse)
        .collect(java.util.stream.Collectors.toList());
  }
}
