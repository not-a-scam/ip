import java.io.IOException;
import java.time.LocalDateTime;

import nomz.common.Messages;

import nomz.ui.Ui;

public class AddDeadlineCommand extends Command {
    private final String description;
    private final LocalDateTime byTime;
    private final String by;
    private final boolean useDateTime;

    public AddDeadlineCommand(String description, LocalDateTime byTime) {
        this.description = description;
        this.byTime = byTime;
        this.by = null;
        useDateTime = true;
    }

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
}
