package app;

import app.domain.RentalService;
import app.http.EquipmentHandler;
import app.http.MemberHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        RentalService service = new RentalService();
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/ping", new PingHandler());
        server.createContext("/equipment", new EquipmentHandler(service));
        server.createContext("/members", new MemberHandler(service));
        server.start();
        System.out.println("Сервер запущен: http://localhost:" + PORT);
    }
}
