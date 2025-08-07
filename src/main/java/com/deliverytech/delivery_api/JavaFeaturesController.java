package com.deliverytech.delivery_api;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@RestController
@RequestMapping("/java-features")
public class JavaFeaturesController {

    private static final Logger log = LoggerFactory.getLogger(JavaFeaturesController.class);
    private final ExecutorService executor;
    private final Counter patternCounter;
    private final Timer virtualTimer;

    public JavaFeaturesController(@Qualifier("virtualThreadExecutor") ExecutorService executor, MeterRegistry registry) {
        this.executor = executor;
        this.patternCounter = registry.counter("delivery.java.features.pattern.calls");
        this.virtualTimer = Timer.builder("delivery.java.features.virtual.threads.timer")
                .description("Tempo de execução do endpoint /virtual-threads")
                .publishPercentiles(0.5, 0.9, 0.99)
                .register(registry);
    }

    // ===============================
    // PATTERN MATCHING (Java 17+)
    // ===============================
    @GetMapping("/pattern-matching/{input}")
    public PatternMatchingResponse patternMatchingExample(@PathVariable String input) {
        patternCounter.increment();
        Object value = parseInput(input);

        // Pattern Matching com instanceof (Java 17+)
        String result = switch (value) {
            case Integer i when i > 100 -> "Número grande: " + i;
            case Integer i when i > 0 -> "Número pequeno: " + i;
            case Integer i -> "Número zero ou negativo: " + i;
            case String s when s.length() > 10 -> "String longa: " + s;
            case String s -> "String curta: " + s;
            case null -> "Valor nulo";
            default -> "Tipo desconhecido: " + value.getClass().getSimpleName();
        };

        return new PatternMatchingResponse(
            input,
            value != null ? value.getClass().getSimpleName() : "null",
            result,
            "Pattern Matching (Java 17+)"
        );
    }

    private Object parseInput(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return input;
        }
    }

    // ===============================
    // VIRTUAL THREADS (Java 21)
    // ===============================
    @GetMapping(value = "/virtual-threads", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletableFuture<VirtualThreadsResponse> virtualThreadsExample() {
        long start = System.nanoTime();
        return CompletableFuture.supplyAsync(() -> {
            // Simulando 3 tarefas que rodam em paralelo usando Virtual Threads
            var task1 = CompletableFuture.supplyAsync(() -> {
                simulateWork("Database Query", 500);
                return "Database: OK";
            }, executor);

            var task2 = CompletableFuture.supplyAsync(() -> {
                simulateWork("API Call", 300);
                return "API: OK";
            }, executor);

            var task3 = CompletableFuture.supplyAsync(() -> {
                simulateWork("File Processing", 200);
                return "File: OK";
            }, executor);

            // Aguarda todas as tarefas
            var results = CompletableFuture.allOf(task1, task2, task3)
                .thenApply(v -> Map.of(
                    "task1", task1.join(),
                    "task2", task2.join(),
                    "task3", task3.join()
                ))
                .join();

            long end = System.nanoTime();
            virtualTimer.record(Duration.ofNanos(end - start));

            return new VirtualThreadsResponse(
                "Virtual Threads (Java 21)",
                ((end - start) / 1_000_000) + "ms",
                results,
                Thread.currentThread().isVirtual() ? "Virtual" : "Platform",
                "3 tarefas executadas em paralelo com Virtual Threads"
            );
        }, executor);
    }

    private void simulateWork(String taskName, long millis) {
        try {
            Thread.sleep(millis);
            log.info("{} executado em: {}", taskName, Thread.currentThread());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Tarefa interrompida: {}", taskName);
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleError(Exception ex) {
        log.error("Erro no JavaFeaturesController", ex);
        return Map.of(
            "error", "internal_error",
            "message", ex.getMessage() != null ? ex.getMessage() : "Ocorreu um erro inesperado"
        );
    }

    public record PatternMatchingResponse(
        String input,
        String parsedType,
        String result,
        String feature
    ) {}

    public record VirtualThreadsResponse(
        String feature,
        String executionTime,
        Map<String, String> tasks,
        String threadType,
        String description
    ) {}
}
