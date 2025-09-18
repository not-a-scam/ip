package nomz.commands;

import static nomz.common.Messages.MESSAGE_TASK_MARKED;
import static nomz.common.Messages.MESSAGE_TASK_UNMARKED;

import java.io.IOException;

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
        Task task = tasks.get(index);
        assert task != null : "Task should not be null";
        String message;

        if (toMark) {
            task.mark();
            message = MESSAGE_TASK_MARKED.formatted(task);
        } else {
            task.unmark();
            message = MESSAGE_TASK_UNMARKED.formatted(task);
        }

        try {
            storage.saveAll(tasks.getTasks());
        } catch (IOException e) {
            return e.getMessage();
        }

        return message;
    }
}
