package example._03_web;

import java.nio.file.Files;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WebApp {
    public static void main(String[] args) throws Exception {
        var spring = new AnnotationConfigWebApplicationContext();
        spring.register(WebConfig.class);

        var tomcat = new Tomcat();
        tomcat.setPort(8081);
        tomcat.setBaseDir(Files.createTempDirectory("tomcat").toString());
        tomcat.getConnector();

        Context context = tomcat.addContext("", null);
        Tomcat.addServlet(context, "dispatcher", new DispatcherServlet(spring))
                .setLoadOnStartup(1);
        context.addServletMappingDecoded("/", "dispatcher");

        tomcat.start();
        System.out.println("Сервер запущен: http://localhost:8081/tasks");
        tomcat.getServer().await();
    }
}
