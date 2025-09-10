# Caching Strategy Report

This document outlines the caching strategy implemented in the Delivery API to improve performance.

## 1. Caching Technology

We have chosen **Redis** as our caching provider. Redis is a powerful in-memory data store that provides high performance and scalability. It is integrated into the application using Spring's caching abstraction.

The Redis instance is managed by Testcontainers for the test environment, ensuring that our tests are reliable and independent of any external Redis installation.

## 2. Implemented Caches

We have implemented caching for the following entities and methods:

### 2.1. Produto (Product)

-   **`produtos` cache:** Caches individual `Produto` objects.
    -   **`@Cacheable(value = "produtos", key = "#id")`**: Applied to `ProdutoService.buscarPorId(Long id)`. When a product is requested by its ID, it is retrieved from the database and stored in the cache. Subsequent requests for the same product ID will be served from the cache, avoiding a database hit.
-   **`produtos:list` cache:** Caches the list of all products.
    -   **`@Cacheable(value = "produtos:list")`**: Applied to `ProdutoService.listarTodos(Pageable pageable)`. The first request for the list of products will be retrieved from the database and cached. Subsequent requests will be served from the cache.

### 2.2. Cache Eviction

To ensure data consistency, we have implemented cache eviction strategies:

-   **`@CacheEvict(value = "produtos", key = "#id")`**: Applied to `ProdutoService.updateProduto(Long id, ...)` and `ProdutoService.deletar(Long id)`. When a product is updated or deleted, its corresponding entry is removed from the `produtos` cache.
-   **`@CacheEvict(value = "produtos:list", allEntries = true)`**: Also applied to `ProdutoService.updateProduto(...)` and `ProdutoService.deletar(...)` to invalidate the cache for the list of all products.
-   **Manual Eviction in `PedidoService`**: When a new order is created (`PedidoService.criar(...)`), the caches for the products included in the order are manually evicted. This is because creating an order can affect the stock of the products, and we need to ensure that the cached product information is up-to-date.

## 3. Integration Tests

We have created a new integration test class, `CacheInvalidationTest.java`, to verify the correctness of our caching implementation. These tests cover:

-   **Caching of individual products:** Verifies that a product is cached after the first retrieval.
-   **Cache eviction on product update:** Ensures that updating a product invalidates its cache entry.
-   **Cache eviction on product deletion:** Ensures that deleting a product invalidates its cache entry.
-   **Cache eviction on order creation:** Verifies that creating a new order invalidates the cache for the products in that order.

### How to Run the Tests

To run all the tests, including the cache invalidation tests, execute the following command from the root of the project:

```bash
./mvnw clean install
```

This will run the full test suite, including the integration tests that use Testcontainers to spin up a real Redis instance.
