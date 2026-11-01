package example.tasks.web;

import example.tasks.model.Task;

public record TaskResponse(long id, String title, boolean done) {
    public static TaskResponse from(Task task) {
        return new TaskResponse(task.id(), task.title(), task.done());
    }
}
