package nomz.commands;

import nomz.data.tasks.TaskList;
import nomz.storage.Storage;
import nomz.ui.Ui;

/**
 * Command to exit the application.
 */
public class ByeCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }
    @Override
    public boolean isExit() {
        return true;
    }
}
