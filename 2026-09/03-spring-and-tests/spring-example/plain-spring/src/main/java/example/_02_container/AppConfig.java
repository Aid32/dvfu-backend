package example._02_container;

import example.tasks.TaskPrinter;
import example.tasks.TaskRepository;
import example.tasks.TaskService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    TaskPrinter printer(TaskService service) {
        return new TaskPrinter(service);
    }

    @Bean
    TaskService service(TaskRepository repository) {
        return new TaskService(repository);
    }














    @Bean
    TaskRepository repository() {
        return new TaskRepository();
    }
}
