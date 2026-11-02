package app.http;

import app.domain.DomainException;
import com.sun.net.httpserver.HttpExchange;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public final class Responses {
    public static final JsonMapper MAPPER = JsonMapper.builder().build();

    private Responses() {}

    public static void json(HttpExchange ex, int code, Object body) throws IOException {
        byte[] bytes = MAPPER.writeValueAsBytes(body);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        ex.sendResponseHeaders(code, bytes.length);
        try (OutputStream out = ex.getResponseBody()) { out.write(bytes); }
    }

    public static void error(HttpExchange ex, int code, String message) throws IOException {
        json(ex, code, Map.of("error", message));
    }

    public static void noContent(HttpExchange ex) throws IOException {
        ex.sendResponseHeaders(204, -1);
        ex.close();
    }

    public static String readBody(HttpExchange ex) throws IOException {
        return new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    public static void handleDomain(HttpExchange ex, DomainException e) throws IOException {
        error(ex, e.getCode(), e.getMessage());
    }
}