package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.dto.request.RestauranteRequest;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import com.deliverytech.delivery_api.service.impl.RestauranteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestauranteServiceImplTest {
    @Mock
    private RestauranteRepository restauranteRepository;
    
    @InjectMocks
    private RestauranteServiceImpl restauranteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve cadastrar restaurante com sucesso")
    void deveCadastrarRestauranteComSucesso() {
        // Arrange
        RestauranteRequest request = new RestauranteRequest();
        request.setNome("Restaurante Teste");
        request.setCategoria("Italiana");
        request.setEndereco("Rua Teste, 123");
        request.setTaxaEntrega(BigDecimal.valueOf(5.0));
        request.setTempoEntregaMinutos(30);
        request.setTelefone("11999999999");
        request.setEmail("teste@restaurante.com");
        request.setAvaliacao(BigDecimal.valueOf(4.5));
        
        Restaurante restauranteSalvo = new Restaurante();
        restauranteSalvo.setId(1L);
        restauranteSalvo.setNome(request.getNome());
        restauranteSalvo.setAtivo(true);
        
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restauranteSalvo);
        
        // Act
        Restaurante resultado = restauranteService.cadastrar(request);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Restaurante Teste", resultado.getNome());
        assertTrue(resultado.isAtivo());
        verify(restauranteRepository).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve buscar restaurante por ID")
    void deveBuscarRestaurantePorId() {
        // Arrange
        Long id = 1L;
        Restaurante restaurante = new Restaurante();
        restaurante.setId(id);
        restaurante.setNome("Restaurante Teste");
        
        when(restauranteRepository.findById(id)).thenReturn(Optional.of(restaurante));
        
        // Act
        Optional<Restaurante> resultado = restauranteService.buscarPorId(id);
        
        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getId());
        assertEquals("Restaurante Teste", resultado.get().getNome());
    }

    @Test
    @DisplayName("Deve listar restaurantes ativos")
    void deveListarRestaurantesAtivos() {
        // Arrange
        Restaurante restaurante1 = new Restaurante();
        restaurante1.setId(1L);
        restaurante1.setNome("Restaurante 1");
        restaurante1.setAtivo(true);
        restaurante1.setExcluido(false);
        
        Restaurante restaurante2 = new Restaurante();
        restaurante2.setId(2L);
        restaurante2.setNome("Restaurante 2");
        restaurante2.setAtivo(true);
        restaurante2.setExcluido(false);
        
        when(restauranteRepository.findByAtivoTrueAndExcluidoFalse())
            .thenReturn(Arrays.asList(restaurante1, restaurante2));
        
        // Act
        List<Restaurante> resultado = restauranteService.listarAtivos();
        
        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(Restaurante::isAtivo));
        assertTrue(resultado.stream().noneMatch(Restaurante::getExcluido));
    }
}
