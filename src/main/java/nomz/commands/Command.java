package nomz.commands;

import nomz.data.exception.NomzException;
import nomz.data.tasks.TaskList;
import nomz.storage.Storage;

/**
 * Represents a command in the application.
 */
public abstract class Command {

    /**
     * Executes the command.
     *
     * @param tasks
     * @param ui
     * @param storage
     * @throws NomzException
     */
    public abstract String execute(TaskList tasks, Storage storage) throws NomzException;

    /**
     * Checks if the command is an exit command.
     *
     * @return
     */
    public boolean isExit() {
        return false;
    }
}
