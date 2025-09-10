package com.deliverytech.delivery_api.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Utilitário para construção segura e padronizada de {@link Pageable}.
 *
 * <p>Recursos: - Normaliza page/size (page >= 0, 1 <= size <= maxSize) - Parsing flexível de
 * parâmetros de sort (aceita múltiplos valores ou uma string única com vírgulas) - Validação
 * estrita contra uma allow-list de propriedades (opcional via parâmetro strict) - Overloads
 * convenientes para diferentes formatos de entrada
 */
public final class PageableUtil {

  public static final int DEFAULT_PAGE = 0;
  public static final int DEFAULT_SIZE = 20;
  public static final int DEFAULT_MAX_SIZE = 100;

  private PageableUtil() {}

  /**
   * Constrói um {@link Pageable} com validação estrita de sort (lança IllegalArgumentException se
   * uma propriedade não estiver na allow-list).
   *
   * @param page 0-based; se null ou negativo, usa 0
   * @param size se null usa DEFAULT_SIZE; forçado entre 1 e maxSize
   * @param sortParam exemplo: "nome,asc" ou "nome,asc;preco,desc" (suporta ';' para múltiplos)
   * @param allowedSortProps conjunto de propriedades permitidas (não nulo)
   */
  public static Pageable buildPageable(
      Integer page, Integer size, String sortParam, Set<String> allowedSortProps) {
    return buildPageable(
        page,
        size,
        parseSortParam(sortParam),
        allowedSortProps,
        true,
        DEFAULT_SIZE,
        DEFAULT_MAX_SIZE);
  }

  /**
   * Constrói um {@link Pageable} com controle completo e opção strict para validação da allow-list.
   *
   * @param page
   * @param size
   * @param sortParams array onde cada entrada pode ser "propriedade" ou "propriedade,dir"
   * @param allowedSortProps conjunto de propriedades permitidas (não nulo)
   * @param strict se true lança IllegalArgumentException ao encontrar propriedade não permitida; se
   *     false ignora entradas inválidas
   * @param defaultSize valor default quando size == null
   * @param maxSize limite superior para size
   */
  public static Pageable buildPageable(
      Integer page,
      Integer size,
      String[] sortParams,
      Set<String> allowedSortProps,
      boolean strict,
      int defaultSize,
      int maxSize) {
    Objects.requireNonNull(allowedSortProps, "allowedSortProps não pode ser nulo");

    int p = (page == null || page < 0) ? DEFAULT_PAGE : page;
    int s = (size == null) ? defaultSize : Math.max(1, Math.min(size, maxSize));

    Sort sort = Sort.unsorted();
    if (sortParams != null) {
      // preservar ordem e evitar duplicatas
      Set<Sort.Order> orders = new LinkedHashSet<>();
      for (String raw : sortParams) {
        if (raw == null || raw.isBlank()) {
          continue;
        }
        String entry = raw.trim();
        // aceitar múltiplos 'ordenadores' separados por ';' (ex.: "nome,asc;preco,desc")
        String[] pieces = entry.split(";", -1);
        for (String piece : pieces) {
          String ptrim = piece.trim();
          if (ptrim.isEmpty()) {
            continue;
          }
          String[] parts = ptrim.split(",");
          String prop = parts[0].trim();
          if (!allowedSortProps.contains(prop)) {
            if (strict) {
              throw new IllegalArgumentException("Propriedade de ordenação não permitida: " + prop);
            } else {
              // ignorar e continuar
              continue;
            }
          }
          Sort.Direction dir = Sort.Direction.ASC;
          if (parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim())) {
            dir = Sort.Direction.DESC;
          }
          orders.add(new Sort.Order(dir, prop));
        }
      }
      if (!orders.isEmpty()) {
        sort = Sort.by(orders.stream().collect(Collectors.toList()));
      }
    }

    return sort.isUnsorted() ? PageRequest.of(p, s) : PageRequest.of(p, s, sort);
  }

  /** Conveniência: aceita varargs para allowedSortProps. */
  public static Pageable buildPageable(
      Integer page,
      Integer size,
      String sortParam,
      boolean strict,
      int defaultSize,
      int maxSize,
      String... allowedSortProps) {
    Set<String> allowed =
        (allowedSortProps == null)
            ? Collections.emptySet()
            : Arrays.stream(allowedSortProps).collect(Collectors.toSet());
    return buildPageable(
        page, size, parseSortParam(sortParam), allowed, strict, defaultSize, maxSize);
  }

  private static String[] parseSortParam(String sortParam) {
    if (sortParam == null || sortParam.isBlank()) {
      return null;
    }
    // suportar separadores ';' para múltiplos sort entries
    String normalized = sortParam.trim();
    String[] parts = normalized.split(";", -1);
    return Arrays.stream(parts).map(String::trim).filter(s -> !s.isEmpty()).toArray(String[]::new);
  }
}
