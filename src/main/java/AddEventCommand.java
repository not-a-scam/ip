// AddEventCommand.java
import java.io.IOException;
import java.time.LocalDateTime;

public class AddEventCommand extends Command {
    private final String description;
    private final LocalDateTime from, to;
    public AddEventCommand(String description, LocalDateTime from, LocalDateTime to) {
        this.description = description; 
        this.from = from; 
        this.to = to;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task t = tasks.add(new Event(description, from, to));
        try { 
            storage.append(t); 
        } catch (IOException e) { 
            ui.showError(e.getMessage()); 
        }
        ui.show(Messages.MESSAGE_ADD_TASK.formatted(t.toString()));
    }
}
