import java.io.IOException;

public class Nomz {
    private Ui ui;
    private Storage storage;
    private TaskList taskList;

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

    public static void main(String[] args) {

        new Nomz("data/tasks.txt").run();;
    }
}
