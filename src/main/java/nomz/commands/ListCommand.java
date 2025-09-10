package nomz.commands;

import nomz.data.tasks.TaskList;
import nomz.storage.Storage;

/**
 * Command to list all tasks.
 */
public class ListCommand extends Command {
    @Override
    public String execute(TaskList tasks, Storage storage) {
        assert tasks != null : "TaskList should not be null";
        assert storage != null : "Storage should not be null";
        return tasks.toDisplayString();
    }
}
