package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.service.impl.ProdutoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoServiceImplTest {
    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoServiceImpl produtoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar produto sem nome")
    void deveLancarExcecaoCadastroSemNome() {
        Produto produto = new Produto();
        produto.setCategoria("Pizza");
        produto.setRestaurante(new com.deliverytech.delivery_api.model.Restaurante());
        var ex = assertThrows(RuntimeException.class, () -> produtoService.cadastrar(produto));
        assertTrue(ex.getMessage().toLowerCase().contains("nome"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar produto sem categoria")
    void deveLancarExcecaoCadastroSemCategoria() {
        Produto produto = new Produto();
        produto.setNome("Pizza");
        produto.setRestaurante(new com.deliverytech.delivery_api.model.Restaurante());
        var ex = assertThrows(RuntimeException.class, () -> produtoService.cadastrar(produto));
        assertTrue(ex.getMessage().toLowerCase().contains("categoria"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar produto sem restaurante")
    void deveLancarExcecaoCadastroSemRestaurante() {
        Produto produto = new Produto();
        produto.setNome("Pizza");
        produto.setCategoria("Pizza");
        var ex = assertThrows(RuntimeException.class, () -> produtoService.cadastrar(produto));
        assertTrue(ex.getMessage().toLowerCase().contains("restaurante"));
    }

    @Test
    @DisplayName("Deve atualizar produto existente")
    void deveAtualizarProdutoExistente() {
        Produto existente = new Produto();
        existente.setId(1L);
        existente.setNome("Pizza");
        existente.setCategoria("Pizza");
        existente.setDisponivel(true);
        existente.setRestaurante(new com.deliverytech.delivery_api.model.Restaurante());
        Produto atualizado = new Produto();
        atualizado.setNome("Pizza Calabresa");
        atualizado.setCategoria("Pizza");
        atualizado.setDisponivel(false);
        atualizado.setRestaurante(new com.deliverytech.delivery_api.model.Restaurante());
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(produtoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        Produto result = produtoService.atualizar(1L, atualizado);
        assertEquals("Pizza Calabresa", result.getNome());
        assertFalse(result.getDisponivel());
    }

    @Test
    @DisplayName("Deve realizar soft delete do produto")
    void deveSoftDeleteProduto() {
        Produto produto = new Produto();
        produto.setId(2L);
        produto.setDisponivel(true);
        produto.setExcluido(false);
        when(produtoRepository.findById(2L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        produtoService.deletar(2L);
        assertFalse(produto.getDisponivel());
        assertTrue(produto.getExcluido());
    }

    @Test
    @DisplayName("Deve buscar produtos por categoria")
    void deveBuscarPorCategoria() {
        Produto produto = new Produto();
        produto.setId(3L);
        produto.setCategoria("Pizza");
        when(produtoRepository.findByCategoriaAndExcluidoFalse("Pizza")).thenReturn(java.util.List.of(produto));
        var lista = produtoService.buscarPorCategoria("Pizza");
        assertEquals(1, lista.size());
        assertEquals("Pizza", lista.get(0).getCategoria());
    }

    @Test
    @DisplayName("Deve lançar exceção ao validar preço inválido")
    void deveLancarExcecaoPrecoInvalido() {
        var ex = assertThrows(RuntimeException.class, () -> produtoService.validarPreco(java.math.BigDecimal.ZERO));
        assertTrue(ex.getMessage().toLowerCase().contains("preço"));
    }

    @Test
    @DisplayName("Deve cadastrar produto com dados válidos")
    void deveCadastrarProdutoQuandoDadosValidos() {
        Produto produto = new Produto();
        produto.setNome("Pizza");
        produto.setCategoria("Pizza");
        produto.setQuantidadeEstoque(10); // Add required field
        produto.setRestaurante(new Restaurante());
        when(produtoRepository.save(produto)).thenReturn(produto);
        Produto salvo = produtoService.cadastrar(produto);
        assertNotNull(salvo);
        assertEquals("Pizza", salvo.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção quando quantidade estoque for nula")
    void deveLancarExcecaoQuandoQuantidadeEstoqueForNula() {
        Produto produto = new Produto();
        produto.setNome("Pizza");
        produto.setCategoria("Pizza");
        produto.setQuantidadeEstoque(null); // Null stock quantity
        produto.setRestaurante(new Restaurante());
        
        assertThrows(RuntimeException.class, () -> produtoService.cadastrar(produto));
    }
}
