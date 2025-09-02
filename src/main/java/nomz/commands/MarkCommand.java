package nomz.commands;

import nomz.common.Messages;
import nomz.data.exception.NomzException;
import nomz.data.tasks.Task;
import nomz.data.tasks.TaskList;
import nomz.storage.Storage;
import nomz.ui.Ui;

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
    public void execute(TaskList tasks, Ui ui, Storage storage) throws NomzException {
        Task t = tasks.get(index);
        if (toMark) {
            t.mark();
            ui.show(Messages.MESSAGE_TASK_MARKED.formatted(t));
        } else {
            t.unmark();
            ui.show(Messages.MESSAGE_TASK_UNMARKED.formatted(t));
        }
        try {
            storage.saveAll(tasks.getTasks());
        } catch (Exception e) {
            ui.showError(e.getMessage());
        }
    }
}
