package com.deliverytech.delivery_api.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.util.Set;

class PageableUtilTest {

  @Test
  void buildPageable_withSimpleParams_returnsPageable() {
    Pageable p = PageableUtil.buildPageable(0, 5, "nome,asc", Set.of("nome"));
    assertEquals(0, p.getPageNumber());
    assertEquals(5, p.getPageSize());
    assertTrue(p.getSort().isSorted());
  }

  @Test
  void buildPageable_withMultipleSortEntries_parsesAll() {
    Pageable p = PageableUtil.buildPageable(1, 10, "nome,asc;preco,desc", Set.of("nome", "preco"));
    assertEquals(1, p.getPageNumber());
    assertEquals(10, p.getPageSize());
    assertTrue(p.getSort().isSorted());
    assertEquals(2, p.getSort().stream().count());
  }

  @Test
  void buildPageable_strictThrowsOnUnknownProperty() {
    assertThrows(IllegalArgumentException.class, () ->
        PageableUtil.buildPageable(0, 10, "invalid,asc", Set.of("nome")));
  }

  @Test
  void buildPageable_nonStrictIgnoresUnknownProperty() {
    Pageable p = PageableUtil.buildPageable(0, 10, new String[] {"invalid,asc", "nome,desc"}, Set.of("nome"), false, 20, 100);
    assertTrue(p.getSort().isSorted());
    assertEquals(1, p.getSort().stream().count());
  }

  @Test
  void buildPageable_enforcesSizeBounds() {
  Pageable p1 = PageableUtil.buildPageable(0, 0, (String) null, true, 5, 50);
  // size == 0 is normalized to minimum 1 by implementation
  assertEquals(1, p1.getPageSize());

  Pageable p2 = PageableUtil.buildPageable(0, 500, (String) null, true, 5, 50);
    assertEquals(50, p2.getPageSize());
  }
}
