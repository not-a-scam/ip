package nomz.data.tasks;

/**
 * Represents a task in Nomz.
 */
public class Task {
    protected String description;
    protected boolean isDone;
    protected TaskType type;

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
     * Returns a string representation of the task for saving.
     * 
     * @return
     */
    public String savedString() {
        return type.getSymbol() + "|" + (isDone ? 1 : 0) + "|" + description;
    }

    @Override
    public String toString(){
        return "["+ type.getSymbol() + "][" + getStatusIcon() + "] " + description;  
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
