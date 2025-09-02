package nomz;

import nomz.commands.Command;
import nomz.common.Messages;
import nomz.data.exception.NomzException;
import nomz.data.tasks.TaskList;
import nomz.parser.Parser;
import nomz.storage.Storage;
import nomz.ui.Ui;

/**
 * Main entry point for the Nomz application.
 */
public class Nomz {

    private Ui ui;
    private Storage storage;
    private TaskList taskList;

    /**
     * Creates a Nomz application instance.
     *
     * @param filepath The path to the task storage file.
     */
    public Nomz(String filepath) {
        this.storage = new Storage(filepath);
        this.ui = new Ui();
        TaskList loaded;
        try {
            loaded = new TaskList(storage.load());
            ui.show(Messages.MESSAGE_LOAD_TASK_SUCCESS);
        } catch (NomzException e) {
            ui.showError(e.getMessage());
            loaded = new TaskList();
        }
        this.taskList = loaded;
    }

    /**
     * Starts the Nomz application.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                Command c = Parser.parse(fullCommand);
                c.execute(taskList, ui, storage);
                isExit = c.isExit();
            } catch (NomzException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    public String getResponse(Command command) {
        try {
            String message = command.execute(taskList, ui, storage);
            return message;
        } catch (NomzException e) {
            return e.getMessage();
        }
    }

    public static void main(String[] args) {
        new Nomz("data/tasks.txt").run();
    }
}
