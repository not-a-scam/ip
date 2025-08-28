package nomz.commands;

import java.io.IOException;
import java.time.LocalDateTime;

import nomz.common.Messages;
import nomz.data.tasks.Event;
import nomz.data.tasks.Task;
import nomz.data.tasks.TaskList;
import nomz.storage.Storage;
import nomz.ui.Ui;

/**
 * Adds an event task to the task list.
 */
public class AddEventCommand extends Command {
    private final String description;
    private final LocalDateTime fromTime, toTime;
    private final String from, to;
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
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task t;
        if(!useDateTime) {
            t = new Event(description, from, to);
        } else {
            t = new Event(description, fromTime, toTime);
        }
        tasks.add(t);
        try { 
            storage.append(t); 
        } catch (IOException e) { 
            ui.showError(e.getMessage()); 
        }
        ui.show(Messages.MESSAGE_ADD_TASK.formatted(t.toString()));
    }

    @Override
    public boolean equals(Object obj) {
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
        if (useDateTime) {
            return description.equals(other.description)
                    && fromTime.equals(other.fromTime)
                    && toTime.equals(other.toTime);
        } else {
            return description.equals(other.description)
                    && from.equals(other.from)
                    && to.equals(other.to);
        }
    }
}
