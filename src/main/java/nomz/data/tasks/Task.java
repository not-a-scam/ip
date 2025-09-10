package nomz.data.tasks;

import java.util.ArrayList;

/**
 * Represents a task in Nomz.
 */
public class Task {
    protected String description;
    protected boolean isDone;
    protected TaskType type;
    protected ArrayList<String> tags;

    /**
     * Creates a Task with the specified description and type.
     *
     * @param description
     * @param type
     */
    public Task(String description, TaskType type) {
        this.description = description;
        this.isDone = false;
        this.type = type;
        this.tags = new ArrayList<>();
    }

    /**
     * Returns the status icon of the task.
     *
     * @return
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /**
     * Marks the task as done.
     */
    public void mark() {
        this.isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void unmark() {
        this.isDone = false;
    }

    /**
     * Adds a tag to the task
     * @param tag
     */
    public void addTag(String tag) {
        tags.add(tag);
    }

    /**
     * Returns all tags associated with the task
     */
    public ArrayList<String> getTags() {
        return tags;
    }

    /**
     * Returns tags in string format
     * @return
     */
    public String getTagsString() {
        return tags.isEmpty() ? "" : " [tags: " + String.join(", ", tags) + "]";
    }

    /**
     * Returns a string representation of the task for saving.
     *
     * @return
     */
    public String toSavedString() {
        return type.getSymbol() + "|" + (isDone ? 1 : 0) + "|" + description;
    }

    /**
     * Returns the description of the task.
     * @return the description of the task
     */
    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return "[" + type.getSymbol() + "][" + getStatusIcon() + "] " + description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        Task task = (Task) o;
        return isDone == task.isDone && description.equals(task.description) && type == task.type;
    }
}
