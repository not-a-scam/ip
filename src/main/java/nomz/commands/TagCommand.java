package nomz.commands;

import static nomz.common.Messages.MESSAGE_ADD_TAG;

import java.io.IOException;

import nomz.data.exception.NomzException;
import nomz.data.tasks.Task;
import nomz.data.tasks.TaskList;
import nomz.storage.Storage;

/**
 * Command to tag a task
 */
public class TagCommand extends Command {
    private final int index;
    private final String tag;

    /**
     * Creates a new tag command
     * @param index
     * @param tag
     */
    public TagCommand(int index, String tag) {
        this.index = index;
        this.tag = tag;
    }

    @Override
    public String execute(TaskList tasks, Storage storage) throws NomzException {
        assert tasks != null : "TaskList should not be null";
        assert storage != null : "Storage should not be null";

        Task task = tasks.get(index);
        task.addTag(tag);
        try {
            storage.saveAll(tasks.getTasks());
        } catch (IOException e) {
            return e.getMessage();
        }

        return MESSAGE_ADD_TAG.formatted(task.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TagCommand)) {
            return false;
        }
        TagCommand that = (TagCommand) o;
        boolean indexEqual = this.index == that.index;
        boolean tagEqual = this.tag.equals(that.tag);
        return indexEqual && tagEqual;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(index) + tag.hashCode();
    }
}
