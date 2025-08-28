package nomz.commands;

import java.io.IOException;

import nomz.common.Messages;
import nomz.data.exception.NomzException;
import nomz.data.tasks.TaskList;
import nomz.storage.Storage;
import nomz.ui.Ui;

public class DeleteCommand extends Command {
    private final int index;
    
    public DeleteCommand(int index) {
        this.index = index; 
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws NomzException {
        tasks.delete(index);
        try { 
            storage.saveAll(tasks.getTasks()); 
        } catch (IOException e) { 
            ui.showError(e.getMessage()); 
        }
        ui.show(Messages.MESSAGE_DELETE_TASK.formatted(index));
    }
}
