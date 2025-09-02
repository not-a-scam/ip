package nomz.commands;

import java.io.IOException;

import nomz.common.Messages;
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
        Task t = tasks.add(new Todo(description));
        try {
            storage.append(t);
        } catch (IOException e) {
            return e.getMessage();
        }
        return Messages.MESSAGE_ADD_TASK.formatted(t.toString());
    }

    @Override
    public boolean equals(Object obj) {
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
