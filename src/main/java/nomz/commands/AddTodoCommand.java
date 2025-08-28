package nomz.commands;

import java.io.IOException;

import nomz.common.Messages;
import nomz.data.tasks.Task;
import nomz.data.tasks.TaskList;
import nomz.data.tasks.Todo;
import nomz.storage.Storage;
import nomz.ui.Ui;

public class AddTodoCommand extends Command {
    private final String description;
    
    public AddTodoCommand(String description) { 
        this.description = description; 
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task t = tasks.add(new Todo(description));
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
        if (!(obj instanceof AddTodoCommand)) {
            return false;
        }
        AddTodoCommand other = (AddTodoCommand) obj;
        return description.equals(other.description);
    }
}
