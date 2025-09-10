package nomz.commands;

import java.io.IOException;
import java.util.ArrayList;

import nomz.common.Messages;
import nomz.data.exception.NomzException;
import nomz.data.tasks.Task;
import nomz.data.tasks.TaskList;
import nomz.storage.Storage;

/**
 * Command to delete a task.
 */
public class DeleteCommand extends Command {
    private final int index;

    /**
     * Creates a DeleteCommand to delete a task at the specified index.
     *
     * @param index
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public String execute(TaskList tasks, Storage storage) throws NomzException {
        assert tasks != null : "TaskList should not be null";
        assert storage != null : "Storage should not be null";
        tasks.delete(index);

        try {
            ArrayList<Task> taskArrayList = tasks.getTasks();
            storage.saveAll(taskArrayList);
        } catch (IOException e) {
            return e.getMessage();
        }
        return Messages.MESSAGE_DELETE_TASK.formatted(index);
    }
}
