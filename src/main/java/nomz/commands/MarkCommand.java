package nomz.commands;

import nomz.common.Messages;
import nomz.data.exception.NomzException;
import nomz.data.tasks.Task;
import nomz.data.tasks.TaskList;
import nomz.storage.Storage;

/**
 * Command to mark a task as done or not done.
 */
public class MarkCommand extends Command {
    private final int index;
    private final boolean toMark;

    /**
     * Creates a MarkCommand to mark a task as done or not done.
     *
     * @param index
     * @param toMark
     */
    public MarkCommand(int index, boolean toMark) {
        this.index = index;
        this.toMark = toMark;
    }

    @Override
    public String execute(TaskList tasks, Storage storage) throws NomzException {
        assert tasks != null : "TaskList should not be null";
        assert storage != null : "Storage should not be null";
        Task t = tasks.get(index);
        assert t != null : "Task should not be null";
        String message;
        if (toMark) {
            t.mark();
            message = Messages.MESSAGE_TASK_MARKED.formatted(t);
        } else {
            t.unmark();
            message = Messages.MESSAGE_TASK_UNMARKED.formatted(t);
        }
        try {
            storage.saveAll(tasks.getTasks());
        } catch (Exception e) {
            return e.getMessage();
        }
        return message;
    }
}
