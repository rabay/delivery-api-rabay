package com.deliverytech.delivery_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementa a interface para validar a anotação @ValidCategoria Valida strings que representam
 * categorias de restaurantes ou produtos
 */
public class CategoriaValidator implements ConstraintValidator<ValidCategoria, String> {

  // Categorias válidas para restaurantes
  private static final Set<String> CATEGORIAS_RESTAURANTE =
      new HashSet<>(
          Arrays.asList(
              "Brasileira",
              "Italiana",
              "Japonesa",
              "Chinesa",
              "Americana",
              "Mexicana",
              "Indiana",
              "Árabe",
              "Francesa",
              "Tailandesa",
              "Coreana",
              "Grega",
              "Peruana",
              "Argentina",
              "Alemã",
              "Portuguesa",
              "Espanhola",
              "Vegetariana",
              "Vegana",
              "Orgânica",
              "Fast Food",
              "Contemporânea",
              "Fusion",
              "Mediterrânea",
              "Churrascaria",
              "Pizzaria",
              "Hamburgueria",
              "Sorveteria",
              "Padaria",
              "Lanchonete",
              "Cafeteria",
              "Bar",
              "Confeitaria",
              "Açaíteria"));

  // Categorias válidas para produtos
  private static final Set<String> CATEGORIAS_PRODUTO =
      new HashSet<>(
          Arrays.asList(
              "Pizza",
              "Hambúrguer",
              "Sushi",
              "Bebida",
              "Sobremesa",
              "Lanche",
              "Prato Principal",
              "Entrada",
              "Salada",
              "Sanduíche",
              "Pastel",
              "Coxinha",
              "Esfiha",
              "Tapioca",
              "Açaí",
              "Vitamina",
              "Suco",
              "Refrigerante",
              "Água",
              "Cerveja",
              "Vinho",
              "Drink",
              "Café",
              "Chá",
              "Chocolate Quente",
              "Milkshake",
              "Sorvete",
              "Bolo",
              "Torta",
              "Pudim",
              "Mousse",
              "Brigadeiro",
              "Bombom",
              "Doce",
              "Salgado",
              "Petisco",
              "Porção",
              "Combo",
              "Promoção"));

  // Todas as categorias válidas (união das duas listas)
  private static final Set<String> TODAS_CATEGORIAS = new HashSet<>();

  static {
    TODAS_CATEGORIAS.addAll(CATEGORIAS_RESTAURANTE);
    TODAS_CATEGORIAS.addAll(CATEGORIAS_PRODUTO);
  }

  private ValidCategoria.Type type;

  /** Inicializa o validador com os parâmetros da anotação */
  @Override
  public void initialize(ValidCategoria constraintAnnotation) {
    this.type = constraintAnnotation.type();
  }

  /**
   * Método que contém a regra de validação 'value' é o valor do campo anotado com @ValidCategoria
   */
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    // Se o valor for nulo ou vazio, considera válido
    // Porque a anotação @NotNull deve ser usada para validar presença
    if (value == null || value.isEmpty()) {
      return true;
    }

    // Remove espaços em branco extras e capitaliza primeira letra de cada palavra
    String cleanValue = formatCategoria(value.trim());

    // Seleciona o conjunto de categorias válidas baseado no tipo
    Set<String> categoriasValidas;
    switch (type) {
      case RESTAURANTE:
        categoriasValidas = CATEGORIAS_RESTAURANTE;
        break;
      case PRODUTO:
        categoriasValidas = CATEGORIAS_PRODUTO;
        break;
      case GERAL:
      default:
        categoriasValidas = TODAS_CATEGORIAS;
        break;
    }

    // Verifica se a categoria é válida
    return categoriasValidas.contains(cleanValue);
  }

  /**
   * Formata a categoria para padronizar a comparação Capitaliza a primeira letra de cada palavra
   */
  private String formatCategoria(String categoria) {
    if (categoria == null || categoria.isEmpty()) {
      return categoria;
    }

    String[] words = categoria.toLowerCase().split("\\s+");
    StringBuilder formatted = new StringBuilder();

    for (int i = 0; i < words.length; i++) {
      if (i > 0) {
        formatted.append(" ");
      }

      String word = words[i];
      if (!word.isEmpty()) {
        // Capitaliza a primeira letra e mantém o resto em minúsculo
        formatted.append(Character.toUpperCase(word.charAt(0)));
        if (word.length() > 1) {
          formatted.append(word.substring(1));
        }
      }
    }

    return formatted.toString();
  }
}
