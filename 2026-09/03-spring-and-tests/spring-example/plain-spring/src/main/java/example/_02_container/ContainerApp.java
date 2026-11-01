package example._02_container;

import example.tasks.TaskPrinter;
import example.tasks.TaskService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ContainerApp {
    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            context.getBean(TaskPrinter.class).print();

            var first = context.getBean(TaskService.class);
            var second = context.getBean(TaskService.class);
            System.out.println("Один и тот же TaskService: " + (first == second));
        }
    }
}
