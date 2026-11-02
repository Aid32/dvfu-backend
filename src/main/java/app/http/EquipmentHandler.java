package app.http;

import app.domain.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tools.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;

public class EquipmentHandler implements HttpHandler {
    private final RentalService service;

    public EquipmentHandler(RentalService service) { this.service = service; }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        try {
            String[] parts = ex.getRequestURI().getPath().split("/");
            String method = ex.getRequestMethod();

            if (parts.length == 2) {
                if ("GET".equals(method)) {
                    var q = ex.getRequestURI().getQuery();
                    EquipmentStatus status = null;
                    String type = null;
                    if (q != null) {
                        for (String p : q.split("&")) {
                            String[] kv = p.split("=", 2);
                            if (kv.length < 2) continue;
                            if (kv[0].equals("status"))
                                status = EquipmentStatus.valueOf(kv[1].toUpperCase());
                            if (kv[0].equals("type")) type = kv[1];
                        }
                    }
                    List<Equipment> list = service.listEquipment(status, type);
                    Responses.json(ex, 200, list);
                } else if ("POST".equals(method)) {
                    JsonNode node = Responses.MAPPER.readTree(Responses.readBody(ex));
                    Equipment e = service.createEquipment(
                            node.path("name").asString(null),
                            node.path("type").asString(null));
                    Responses.json(ex, 201, e);
                } else {
                    ex.getResponseHeaders().set("Allow", "GET, POST");
                    Responses.error(ex, 405, "method not allowed");
                }
            } else if (parts.length == 3) {
                long id = Long.parseLong(parts[2]);
                switch (method) {
                    case "GET" -> Responses.json(ex, 200, service.getEquipment(id));
                    case "PUT", "PATCH" -> {
                        JsonNode node = Responses.MAPPER.readTree(Responses.readBody(ex));
                        String name = node.has("name") ? node.get("name").asString() : null;
                        String type = node.has("type") ? node.get("type").asString() : null;
                        EquipmentStatus status = node.has("status")
                                ? EquipmentStatus.valueOf(node.get("status").asString().toUpperCase())
                                : null;
                        Responses.json(ex, 200, service.updateEquipment(id, name, type, status));
                    }
                    case "DELETE" -> { service.deleteEquipment(id); Responses.noContent(ex); }
                    default -> {
                        ex.getResponseHeaders().set("Allow", "GET, PUT, PATCH, DELETE");
                        Responses.error(ex, 405, "method not allowed");
                    }
                }
            } else {
                Responses.error(ex, 404, "not found");
            }
        } catch (DomainException e) {
            Responses.handleDomain(ex, e);
        } catch (tools.jackson.core.JacksonException e) {
            Responses.error(ex, 400, "invalid json");
        } catch (IllegalArgumentException e) {
            Responses.error(ex, 400, "invalid parameter: " + e.getMessage());
        } catch (Exception e) {
            Responses.error(ex, 500, "internal error");
        }
    }
}