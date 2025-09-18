package nomz.commands;

import static nomz.common.Messages.MESSAGE_ADD_TASK;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

import nomz.data.tasks.Deadline;
import nomz.data.tasks.Task;
import nomz.data.tasks.TaskList;
import nomz.storage.Storage;

/**
 * Adds a deadline task to the task list.
 */
public class AddDeadlineCommand extends Command {
    private final String description;
    private final String by;
    private final LocalDateTime byTime;
    private final boolean useDateTime;

    /**
     * Creates an AddDeadlineCommand with the specified description and a LocalDateTime
     *
     * @param description
     * @param byTime
     */
    public AddDeadlineCommand(String description, LocalDateTime byTime) {
        this.description = description;
        this.byTime = byTime;
        this.by = null;
        useDateTime = true;
    }

    /**
     * Creates an AddDeadlineCommand with the specified description and a String to represent complement time
     *
     * @param description
     * @param by
     */
    public AddDeadlineCommand(String description, String by) {
        this.description = description;
        this.byTime = null;
        this.by = by;
        useDateTime = false;
    }

    @Override
    public String execute(TaskList tasks, Storage storage) {
        assert tasks != null : "TaskList should not be null";
        assert storage != null : "Storage should not be null";
        Task task;
        if (!useDateTime) {
            task = new Deadline(description, by, new ArrayList<>());
        } else {
            task = new Deadline(description, byTime, new ArrayList<>());
        }

        tasks.add(task);

        try {
            storage.append(task);
        } catch (IOException e) {
            return e.getMessage();
        }

        String taskString = task.toString();
        String message = MESSAGE_ADD_TASK.formatted(taskString);

        return message;
    }

    @Override
    public boolean equals(Object obj) { // for testing
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof AddDeadlineCommand)) {
            return false;
        }

        AddDeadlineCommand other = (AddDeadlineCommand) obj;
        if (useDateTime != other.useDateTime) {
            return false;
        }

        boolean isDescriptionEquals = description.equals(other.description);
        if (useDateTime) {
            boolean isByTimeEquals = byTime.equals(other.byTime);

            return isDescriptionEquals && isByTimeEquals;
        } else {
            boolean isByEquals = by.equals(other.by);
            return isDescriptionEquals && isByEquals;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, by, byTime, useDateTime);
    }
}
