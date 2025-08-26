package nomz.commands;

import nomz.data.exception.NomzException;
import nomz.data.tasks.TaskList;
import nomz.storage.Storage;
import nomz.ui.Ui;

public abstract class Command {
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws NomzException;

    public boolean isExit() { 
        return false; 
    }
}
