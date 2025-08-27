package nomz.commands;

import nomz.data.exception.NomzException;
import nomz.data.tasks.TaskList;
import nomz.storage.Storage;
import nomz.ui.Ui;

public abstract class Command {

    /**
     * Executes the command.
     * @param tasks
     * @param ui
     * @param storage
     * @throws NomzException
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws NomzException;

    /**
     * Checks if the command is an exit command.
     * @return
     */
    public boolean isExit() { 
        return false; 
    }
}
