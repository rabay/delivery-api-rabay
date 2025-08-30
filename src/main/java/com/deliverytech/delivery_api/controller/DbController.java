package com.deliverytech.delivery_api.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/db")
public class DbController {

    private final JdbcTemplate jdbcTemplate;

    public DbController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/schema")
    public ResponseEntity<Map<String, Object>> schema(
            @RequestParam(value = "table", required = false) String tableName) {
        Map<String, Object> result = new HashMap<>();

        // listar tabelas da base de dados atual (MySQL)
        List<Map<String, Object>> tables =
                jdbcTemplate.queryForList(
                        "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA ="
                            + " DATABASE() ORDER BY TABLE_NAME");
        result.put("tables", tables);

        // listar colunas de uma tabela específica (se solicitada) usando consulta dinâmica
        if (tableName != null && !tableName.isBlank()) {
            try {
                List<Map<String, Object>> cols =
                        jdbcTemplate.queryForList(
                                "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE"
                                    + " TABLE_SCHEMA=DATABASE() AND TABLE_NAME=? ORDER BY"
                                    + " ORDINAL_POSITION",
                                tableName.toUpperCase());
                result.put("columns", cols);
            } catch (DataAccessException ex) {
                result.put("columns_error", ex.getMessage());
            }
        }

        // constraints
        List<Map<String, Object>> constraints =
                jdbcTemplate.queryForList(
                        "SELECT TABLE_NAME, CONSTRAINT_NAME, CONSTRAINT_TYPE FROM"
                            + " INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE TABLE_SCHEMA=DATABASE()"
                            + " ORDER BY TABLE_NAME, CONSTRAINT_NAME");
        result.put("constraints", constraints);

        // key column usage (FK columns) - MySQL INFORMATION_SCHEMA
        List<Map<String, Object>> keys =
                jdbcTemplate.queryForList(
                        "SELECT TABLE_NAME, COLUMN_NAME, CONSTRAINT_NAME FROM"
                            + " INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_SCHEMA=DATABASE()"
                            + " ORDER BY TABLE_NAME, CONSTRAINT_NAME");
        result.put("keyColumnUsage", keys);

        // contagens rápidas por tabela
        Map<String, Object> counts = new HashMap<>();
        for (Map<String, Object> row : tables) {
            String table = (String) row.get("TABLE_NAME");
            try {
                Integer c =
                        jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + table, Integer.class);
                counts.put(table, c);
            } catch (DataAccessException e) {
                counts.put(table, "error: " + e.getMessage());
            }
        }
        result.put("counts", counts);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/integrity")
    @Transactional
    public ResponseEntity<Map<String, Object>> integrity() {
        Map<String, Object> resp = new HashMap<>();

        String insertSql =
                "INSERT INTO ITEM_PEDIDO (id, pedido_id, produto_id, quantidade, preco_unitario,"
                    + " subtotal) VALUES (?, ?, ?, ?, ?, ?)";
        Object[] params = new Object[] {999999, 999999, 999999, 1, 1.00, 1.00};

        try {
            jdbcTemplate.update(insertSql, params);
            // se chegou aqui, a inserção não violou FK (surpresa); remover o registro de teste
            jdbcTemplate.update("DELETE FROM ITEM_PEDIDO WHERE id = ?", 999999);
            resp.put("status", "insert_succeeded_no_fk_violation");
        } catch (DataAccessException ex) {
            resp.put("status", "insert_failed_as_expected");
            resp.put("error", ex.getMessage());
        }

        return ResponseEntity.ok(resp);
    }

    @PostMapping("/query")
    public ResponseEntity<Map<String, Object>> query(@RequestBody Map<String, String> body) {
        Map<String, Object> resp = new HashMap<>();
        String sql = body.get("sql");
        if (sql == null || sql.isBlank()) {
            resp.put("error", "missing sql in request body");
            return ResponseEntity.badRequest().body(resp);
        }
        String trimmed = sql.trim();
        String upper = trimmed.toUpperCase();

        // Validações de segurança simples
        if (upper.contains(";")
                || upper.contains("UPDATE")
                || upper.contains("DELETE")
                || upper.contains("INSERT")
                || upper.contains("ALTER")
                || upper.contains("DROP")
                || upper.contains("TRUNCATE")) {
            resp.put("error", "only single SELECT queries are allowed");
            return ResponseEntity.badRequest().body(resp);
        }
        if (!upper.startsWith("SELECT") && !upper.startsWith("WITH")) {
            resp.put("error", "only SELECT queries are allowed");
            return ResponseEntity.badRequest().body(resp);
        }
        if (trimmed.length() > 2000) {
            resp.put("error", "query too long");
            return ResponseEntity.badRequest().body(resp);
        }

        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(trimmed);
            resp.put("rows", rows);
            resp.put("count", rows.size());
            return ResponseEntity.ok(resp);
        } catch (DataAccessException ex) {
            resp.put("error", ex.getMessage());
            return ResponseEntity.status(500).body(resp);
        }
    }
}
