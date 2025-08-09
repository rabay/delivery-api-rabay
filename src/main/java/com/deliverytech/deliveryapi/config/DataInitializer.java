package com.deliverytech.deliveryapi.config;

import com.deliverytech.deliveryapi.domain.model.Address;
import com.deliverytech.deliveryapi.domain.model.Restaurant;
import com.deliverytech.deliveryapi.domain.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Configuração para popular o banco de dados com dados iniciais
 * Executado apenas na inicialização da aplicação
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public void run(String... args) throws Exception {
        // Só popula se não houver restaurantes cadastrados
        if (restaurantRepository.count() == 0) {
            createSampleRestaurants();
        }
    }

    private void createSampleRestaurants() {
        // Restaurante 1: Pizzaria do João
        Address address1 = new Address(
                "Rua das Flores", "123", "Apto 201",
                "Centro", "São Paulo", "SP", "01234-567", "Próximo ao metrô"
        );
        
        Restaurant restaurant1 = new Restaurant(
                "Pizzaria do João",
                "Pizzas artesanais com ingredientes frescos e massa tradicional italiana",
                "12.345.678/0001-90",
                "(11) 98765-4321",
                address1,
                null // owner será null por enquanto
        );
        restaurant1.setLogo("https://example.com/logo-pizzaria.png");
        restaurant1.setOpen(true);

        // Restaurante 2: Burger House
        Address address2 = new Address(
                "Avenida Paulista", "1000", null,
                "Bela Vista", "São Paulo", "SP", "01310-100", "Em frente ao MASP"
        );
        
        Restaurant restaurant2 = new Restaurant(
                "Burger House",
                "Hambúrgueres gourmet com carnes selecionadas e pães artesanais",
                "98.765.432/0001-10",
                "(11) 87654-3210",
                address2,
                null // owner será null por enquanto
        );
        restaurant2.setLogo("https://example.com/logo-burger.png");
        restaurant2.setOpen(true);

        // Restaurante 3: Sushi Master (fechado)
        Address address3 = new Address(
                "Rua da Liberdade", "500", "Loja 15",
                "Liberdade", "São Paulo", "SP", "01503-001", "Próximo à estação Liberdade"
        );
        
        Restaurant restaurant3 = new Restaurant(
                "Sushi Master",
                "Culinária japonesa autêntica com peixes frescos importados diariamente",
                "11.222.333/0001-44",
                "(11) 99988-7766",
                address3,
                null // owner será null por enquanto
        );
        restaurant3.setLogo("https://example.com/logo-sushi.png");
        restaurant3.setOpen(false); // Restaurante fechado no momento

        // Restaurante 4: Café & Cia (inativo)
        Address address4 = new Address(
                "Rua Augusta", "200", "Térreo",
                "Consolação", "São Paulo", "SP", "01305-000", "Esquina com Rua da Consolação"
        );
        
        Restaurant restaurant4 = new Restaurant(
                "Café & Cia",
                "Cafés especiais, doces caseiros e sanduíches naturais",
                "55.666.777/0001-88",
                "(11) 77766-5544",
                address4,
                null // owner será null por enquanto
        );
        restaurant4.setLogo("https://example.com/logo-cafe.png");
        restaurant4.setActive(false); // Restaurante inativo

        // Salvar no banco
        restaurantRepository.save(restaurant1);
        restaurantRepository.save(restaurant2);
        restaurantRepository.save(restaurant3);
        restaurantRepository.save(restaurant4);

        System.out.println("✅ Dados iniciais de restaurantes criados com sucesso!");
        System.out.println("- 2 restaurantes ativos e abertos");
        System.out.println("- 1 restaurante ativo mas fechado");
        System.out.println("- 1 restaurante inativo");
    }
}
