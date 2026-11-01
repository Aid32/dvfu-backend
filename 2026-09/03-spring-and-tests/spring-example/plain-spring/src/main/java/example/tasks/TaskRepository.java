package example.tasks;

import java.util.List;

public class TaskRepository {
    private final List<String> titles = List.of("Git", "Maven", "Spring");

    public TaskRepository() {
        System.out.println("TaskRepository создан");
    }

    public List<String> findAll() {
        return titles;
    }
}
