package nomz.data.tasks;

import java.util.ArrayList;

/**
 * Represents a todo task in Nomz.
 */
public class Todo extends Task {

    /**
     * Creates a todo task.
     *
     * @param description
     */
    public Todo(String description, ArrayList<String> tags) {
        super(description, TaskType.TODO, tags);
    }

    @Override
    public String toString() {
        return super.toString() + getTagsString();
    }

    @Override
    public String toSavedString() {
        return super.toSavedString() + "|" + getTagsString();
    }
}
