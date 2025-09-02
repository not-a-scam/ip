package nomz;

import nomz.commands.Command;
import nomz.data.exception.NomzException;
import nomz.data.tasks.TaskList;
import nomz.storage.Storage;

/**
 * Represents the Nomz application.
 */
public class Nomz {

    private Storage storage;
    private TaskList taskList;

    /**
     * Creates a Nomz application instance.
     *
     * @param filepath The path to the task storage file.
     */
    public Nomz(String filepath) {
        this.storage = new Storage(filepath);
        TaskList loaded;
        try {
            loaded = new TaskList(storage.load());
        } catch (NomzException e) {
            loaded = new TaskList();
        }
        this.taskList = loaded;
    }

    /**
     * Returns a message to display based on the command's execution.
     * @param command
     * @return
     */
    public String getResponse(Command command) {
        try {
            String message = command.execute(taskList, storage);
            return message;
        } catch (NomzException e) {
            return e.getMessage();
        }
    }
}
