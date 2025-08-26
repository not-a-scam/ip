// AddEventCommand.java
import java.io.IOException;
import java.time.LocalDateTime;

public class AddEventCommand extends Command {
    private final String description;
    private final LocalDateTime fromTime, toTime;
    private final String from, to;
    private final boolean useDateTime;

    public AddEventCommand(String description, LocalDateTime fromTime, LocalDateTime toTime) {
        this.description = description; 
        this.fromTime = fromTime; 
        this.toTime = toTime;
        this.from = null;
        this.to = null;
        this.useDateTime = true;
    }

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
}
