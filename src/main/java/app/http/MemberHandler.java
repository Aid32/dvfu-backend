package app.http;

import app.domain.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tools.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;

public class MemberHandler implements HttpHandler {
    private final RentalService service;

    public MemberHandler(RentalService service) { this.service = service; }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        try {
            String[] parts = ex.getRequestURI().getPath().split("/");
            String method = ex.getRequestMethod();

            if (parts.length == 2) {
                if ("GET".equals(method)) {
                    MembershipType type = null;
                    String q = ex.getRequestURI().getQuery();
                    if (q != null) {
                        for (String p : q.split("&")) {
                            String[] kv = p.split("=", 2);
                            if (kv.length == 2 && kv[0].equals("membershipType"))
                                type = MembershipType.valueOf(kv[1].toUpperCase());
                        }
                    }
                    List<Member> list = service.listMembers(type);
                    Responses.json(ex, 200, list);
                } else if ("POST".equals(method)) {
                    JsonNode node = Responses.MAPPER.readTree(Responses.readBody(ex));
                    Member m = service.createMember(
                            node.path("name").asString(null),
                            MembershipType.valueOf(node.path("membershipType").asString().toUpperCase()));
                    Responses.json(ex, 201, m);
                } else {
                    ex.getResponseHeaders().set("Allow", "GET, POST");
                    Responses.error(ex, 405, "method not allowed");
                }
            }
            else if (parts.length == 3) {
                long id = Long.parseLong(parts[2]);
                switch (method) {
                    case "GET" -> Responses.json(ex, 200, service.getMember(id));
                    case "PUT", "PATCH" -> {
                        JsonNode node = Responses.MAPPER.readTree(Responses.readBody(ex));
                        String name = node.has("name") ? node.get("name").asString() : null;
                        MembershipType type = node.has("membershipType")
                                ? MembershipType.valueOf(node.get("membershipType").asString().toUpperCase())
                                : null;
                        Responses.json(ex, 200, service.updateMember(id, name, type));
                    }
                    case "DELETE" -> { service.deleteMember(id); Responses.noContent(ex); }
                    default -> {
                        ex.getResponseHeaders().set("Allow", "GET, PUT, PATCH, DELETE");
                        Responses.error(ex, 405, "method not allowed");
                    }
                }
            }
            else if (parts.length == 4 && parts[3].equals("rentals")) {
                if (!"POST".equals(method)) {
                    ex.getResponseHeaders().set("Allow", "POST");
                    Responses.error(ex, 405, "method not allowed");
                    return;
                }
                long id = Long.parseLong(parts[2]);
                JsonNode node = Responses.MAPPER.readTree(Responses.readBody(ex));
                long equipmentId = node.path("equipmentId").asLong(0);
                if (equipmentId <= 0) throw new DomainException(400, "equipmentId required");
                service.rent(id, equipmentId);
                Responses.noContent(ex);
            }
            else {
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