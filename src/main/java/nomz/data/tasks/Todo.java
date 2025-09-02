package nomz.data.tasks;

/**
 * Represents a todo task in Nomz.
 */
public class Todo extends Task {

    /**
     * Creates a todo task.
     *
     * @param description
     */
    public Todo(String description) {
        super(description, TaskType.TODO);
    }
}
