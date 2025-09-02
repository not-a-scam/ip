package nomz.commands;

import java.io.IOException;

import nomz.common.Messages;
import nomz.data.exception.NomzException;
import nomz.data.tasks.TaskList;
import nomz.storage.Storage;
import nomz.ui.Ui;

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
    public String execute(TaskList tasks, Ui ui, Storage storage) throws NomzException {
        tasks.delete(index);
        try {
            storage.saveAll(tasks.getTasks());
        } catch (IOException e) {
            return e.getMessage();
        }
        return Messages.MESSAGE_DELETE_TASK.formatted(index);
    }
}
