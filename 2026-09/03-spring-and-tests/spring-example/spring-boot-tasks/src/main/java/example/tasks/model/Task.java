package example.tasks.model;

public record Task(long id, String title, boolean done) {
    public Task withDone(boolean done) {
        return new Task(id, title, done);
    }
}
