package nomz.commands;

import java.io.IOException;
import java.time.LocalDateTime;

import nomz.common.Messages;
import nomz.data.tasks.Deadline;
import nomz.data.tasks.Task;
import nomz.data.tasks.TaskList;
import nomz.storage.Storage;
import nomz.ui.Ui;

public class AddDeadlineCommand extends Command {
    private final String description;
    private final LocalDateTime byTime;
    private final String by;
    private final boolean useDateTime;

    /**
     * Creates an AddDeadlineCommand with the specified description and a LocalDateTime
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
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task t;
        if(!useDateTime) {
            t = new Deadline(description, by);
        } else {
            t = new Deadline(description, byTime);
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
        if (!(obj instanceof AddDeadlineCommand)) {
            return false;
        }
        AddDeadlineCommand other = (AddDeadlineCommand) obj;
        if (useDateTime != other.useDateTime) {
            return false;
        }
        if (useDateTime) {
            return description.equals(other.description) && byTime.equals(other.byTime);
        } else {
            return description.equals(other.description) && by.equals(other.by);
        }
    }
}
