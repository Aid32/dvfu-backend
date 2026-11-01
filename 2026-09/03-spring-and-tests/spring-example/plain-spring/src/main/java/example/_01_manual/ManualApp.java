package example._01_manual;

import example.tasks.TaskPrinter;
import example.tasks.TaskRepository;
import example.tasks.TaskService;

public class ManualApp {
    public static void main(String[] args) {
        var repository = new TaskRepository();
        var service = new TaskService(repository);
        var printer = new TaskPrinter(service);

        printer.print();
    }
}
