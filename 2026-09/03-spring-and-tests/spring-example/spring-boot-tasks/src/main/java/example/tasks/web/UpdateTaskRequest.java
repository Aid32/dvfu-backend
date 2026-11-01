package example.tasks.web;

import jakarta.validation.constraints.NotNull;

public record UpdateTaskRequest(@NotNull Boolean done) {
}
