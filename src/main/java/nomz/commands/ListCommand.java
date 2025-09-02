package nomz.commands;

import nomz.data.tasks.TaskList;
import nomz.storage.Storage;

/**
 * Command to list all tasks.
 */
public class ListCommand extends Command {
    @Override
    public String execute(TaskList tasks, Storage storage) {
        return tasks.toDisplayString();
    }
}
