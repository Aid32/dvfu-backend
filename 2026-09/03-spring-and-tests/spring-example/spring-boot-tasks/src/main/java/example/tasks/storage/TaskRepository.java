package example.tasks.storage;

import example.tasks.model.Task;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class TaskRepository {
    private final Map<Long, Task> tasks = new LinkedHashMap<>();
    private long nextId = 1;

    public List<Task> findAll() {
        return List.copyOf(tasks.values());
    }

    public Optional<Task> findById(long id) {
        return Optional.ofNullable(tasks.get(id));
    }

    public Task create(String title) {
        var task = new Task(nextId++, title, false);
        tasks.put(task.id(), task);
        return task;
    }

    public Optional<Task> updateDone(long id, boolean done) {
        Task task = tasks.get(id);
        if (task == null) {
            return Optional.empty();
        }
        Task updated = task.withDone(done);
        tasks.put(id, updated);
        return Optional.of(updated);
    }

    public boolean deleteById(long id) {
        return tasks.remove(id) != null;
    }
}
