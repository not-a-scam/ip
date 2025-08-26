import java.io.IOException;
import java.time.LocalDateTime;

public class AddDeadlineCommand extends Command {
    private final String description;
    private final LocalDateTime by;
    public AddDeadlineCommand(String description, LocalDateTime by) {
        this.description = description;
        this.by = by;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task t = tasks.add(new Deadline(description, by));
        try { 
            storage.append(t); 
        } catch (IOException e) { 
            ui.showError(e.getMessage()); 
        }
        ui.show(Messages.MESSAGE_ADD_TASK.formatted(t.toString()));
    }
}
