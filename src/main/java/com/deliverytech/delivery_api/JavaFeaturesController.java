package main.java.com.deliverytech.delivery_api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/java-features")
public class JavaFeaturesController {

    // ===============================
    // PATTERN MATCHING (Java 17+)
    // ===============================
    @GetMapping("/pattern-matching/{input}")
    public Map<String, String> patternMatchingExample(@PathVariable String input) {
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
        
        return Map.of(
            "input", input,
            "parsedType", value != null ? value.getClass().getSimpleName() : "null",
            "result", result,
            "feature", "Pattern Matching (Java 17+)"
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
    @GetMapping("/virtual-threads")
    public CompletableFuture<Map<String, Object>> virtualThreadsExample() {
        // Criando executor com Virtual Threads (Java 21)
        var executor = Executors.newVirtualThreadPerTaskExecutor();
        
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            
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
                )).join();
            
            long endTime = System.currentTimeMillis();
            
            return Map.of(
                "feature", "Virtual Threads (Java 21)",
                "executionTime", (endTime - startTime) + "ms",
                "tasks", results,
                "threadType", Thread.currentThread().isVirtual() ? "Virtual" : "Platform",
                "description", "3 tarefas executadas em paralelo com Virtual Threads"
            );
        }, executor);
    }
    
    private void simulateWork(String taskName, long millis) {
        try {
            Thread.sleep(millis);
            System.out.println(taskName + " executado em: " + Thread.currentThread());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
