package nomz.commands;

import static nomz.common.Messages.MESSAGE_ADD_TASK;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import nomz.data.tasks.Event;
import nomz.data.tasks.Task;
import nomz.data.tasks.TaskList;
import nomz.storage.Storage;

/**
 * Adds an event task to the task list.
 */
public class AddEventCommand extends Command {
    private final String description;
    private final String from;
    private final String to;
    private final LocalDateTime fromTime;
    private final LocalDateTime toTime;
    private final boolean useDateTime;

    /**
     * Creates an AddEventCommand with the specified description and LocalDateTime
     * objects to represent the event's time period.
     *
     * @param description
     * @param fromTime
     * @param toTime
     */
    public AddEventCommand(String description, LocalDateTime fromTime, LocalDateTime toTime) {
        this.description = description;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.from = null;
        this.to = null;
        this.useDateTime = true;
    }

    /**
     * Creates an AddEventCommand with the specified description and Strings to represent
     * the event's start and end time.
     *
     * @param description
     * @param from
     * @param to
     */
    public AddEventCommand(String description, String from, String to) {
        this.description = description;
        this.fromTime = null;
        this.toTime = null;
        this.from = from;
        this.to = to;
        useDateTime = false;
    }

    @Override
    public String execute(TaskList tasks, Storage storage) {
        assert tasks != null : "TaskList should not be null";
        assert storage != null : "Storage should not be null";
        Task task;
        if (!useDateTime) {
            task = new Event(description, from, to, new ArrayList<>());
        } else {
            task = new Event(description, fromTime, toTime, new ArrayList<>());
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

        if (!(obj instanceof AddEventCommand)) {
            return false;
        }

        AddEventCommand other = (AddEventCommand) obj;
        if (useDateTime != other.useDateTime) {
            return false;
        }

        boolean isDescriptionEquals = description.equals(other.description);
        if (useDateTime) {
            boolean isFromTimeEquals = fromTime.equals(other.fromTime);
            boolean isToTimeEquals = toTime.equals(other.toTime);

            return isDescriptionEquals
                    && isFromTimeEquals
                    && isToTimeEquals;
        } else {
            boolean isFromEquals = from.equals(other.from);
            boolean isToEquals = to.equals(other.to);

            return isDescriptionEquals
                    && isFromEquals
                    && isToEquals;
        }
    }
}
