package nomz.commands;

import static nomz.common.Messages.MESSAGE_BYE;

import nomz.data.tasks.TaskList;
import nomz.storage.Storage;
import nomz.ui.Ui;

/**
 * Command to exit the application.
 */
public class ByeCommand extends Command {
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        return MESSAGE_BYE;
    }
    @Override
    public boolean isExit() {
        return true;
    }
}
