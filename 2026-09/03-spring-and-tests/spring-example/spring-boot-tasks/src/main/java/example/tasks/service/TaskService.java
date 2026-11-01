package example.tasks.service;

import example.tasks.model.Task;
import example.tasks.storage.TaskRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> list(Boolean done) {
        List<Task> all = repository.findAll();
        if (done == null) {
            return all;
        }
        return all.stream()
                .filter(task -> task.done() == done)
                .toList();
    }

    public Task get(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task create(String title) {
        return repository.create(title.strip());
    }

    public Task updateDone(long id, boolean done) {
        return repository.updateDone(id, done)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public void delete(long id) {
        if (!repository.deleteById(id)) {
            throw new TaskNotFoundException(id);
        }
    }
}
