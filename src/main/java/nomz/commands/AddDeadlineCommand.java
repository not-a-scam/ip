package nomz.commands;

import static nomz.common.Messages.MESSAGE_ADD_TASK;

import java.io.IOException;
import java.time.LocalDateTime;

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
        Task task;
        if (!useDateTime) {
            task = new Deadline(description, by);
        } else {
            task = new Deadline(description, byTime);
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
}
