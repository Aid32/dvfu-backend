package example.tasks;

import java.util.List;

public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
        System.out.println("TaskService создан");
    }

    public List<String> titles() {
        return repository.findAll();
    }

    public int count() {
        return repository.findAll().size();
    }
}
