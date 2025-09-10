package nomz.commands;

import static nomz.common.Messages.MESSAGE_ADD_TASK;

import java.io.IOException;
import java.util.ArrayList;

import nomz.data.tasks.Task;
import nomz.data.tasks.TaskList;
import nomz.data.tasks.Todo;
import nomz.storage.Storage;

/**
 * Adds a todo task to the task list.
 */
public class AddTodoCommand extends Command {
    private final String description;

    /**
     * Creates an AddTodoCommand with the specified description.
     *
     * @param description
     */
    public AddTodoCommand(String description) {
        this.description = description;
    }

    @Override
    public String execute(TaskList tasks, Storage storage) {
        assert tasks != null : "TaskList should not be null";
        assert storage != null : "Storage should not be null";
        Task task = tasks.add(new Todo(description, new ArrayList<>()));
        try {
            storage.append(task);
        } catch (IOException e) {
            return e.getMessage();
        }

        String taskString = task.toString();
        String message = MESSAGE_ADD_TASK.formatted(taskString);

        return message;
    }

    @Override
    public boolean equals(Object obj) { // for testing
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof AddTodoCommand)) {
            return false;
        }

        AddTodoCommand other = (AddTodoCommand) obj;
        return description.equals(other.description);
    }
}
