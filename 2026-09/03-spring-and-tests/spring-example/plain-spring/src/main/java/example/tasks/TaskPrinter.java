package example.tasks;

public class TaskPrinter {
    private final TaskService service;

    public TaskPrinter(TaskService service) {
        this.service = service;
        System.out.println("TaskPrinter создан");
    }

    public void print() {
        System.out.println("Задач: " + service.count() + " " + service.titles());
    }
}
