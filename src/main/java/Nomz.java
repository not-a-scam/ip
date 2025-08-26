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
            String fullCommand = ui.readCommand();
            try {
                Parser.ParsedCommand pc = Parser.parse(fullCommand);
                switch (pc.command) {
                case LIST:
                    ui.show(taskList.toDisplayString());
                    break;
                case TODO: {
                    Task t = taskList.add(new Todo(pc.description));
                    storage.append(t);
                    ui.show(Messages.MESSAGE_ADD_TASK.formatted(t.toString()));
                    break;
                }
                case DEADLINE: {
                    Task t;
                    if (pc.byTime == null) {
                        t = taskList.add(new Deadline(pc.description, pc.by));
                    } else {
                        t = taskList.add(new Deadline(pc.description, pc.byTime));
                    }
                    storage.append(t);
                    ui.show(Messages.MESSAGE_ADD_TASK.formatted(t.toString()));
                    break;
                }
                case EVENT: {
                    Task t;
                    if (pc.fromTime == null || pc.toTime == null) {
                        t = taskList.add(new Event(pc.description, pc.from, pc.to));
                    } else {
                        t = taskList.add(new Event(pc.description, pc.fromTime, pc.toTime));
                    }
                    storage.append(t);
                    ui.show(Messages.MESSAGE_ADD_TASK.formatted(t.toString()));
                    break;
                }
                case MARK: {
                    Task t = taskList.get(pc.index);
                    t.mark();
                    storage.saveAll(taskList.getTasks()); // overwrite to persist mark
                    ui.show(Messages.MESSAGE_TASK_MARKED.formatted(t.toString()));
                    break;
                }
                case UNMARK: {
                    Task t = taskList.get(pc.index);
                    t.unmark();
                    storage.saveAll(taskList.getTasks());
                    ui.show(Messages.MESSAGE_TASK_UNMARKED.formatted(t.toString()));
                    break;
                }
                case DELETE: {
                    taskList.delete(pc.index);
                    storage.saveAll(taskList.getTasks());
                    ui.show(Messages.MESSAGE_DELETE_TASK.formatted(pc.index));
                    break;
                }
                case BYE:
                    isExit = true;
                    ui.showGoodbye();
                    break;
                default:
                    throw new InvalidNomzCommandException();
                }
            } catch (NomzException e) {
                ui.showError(e.getMessage());
            } catch (IOException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {

        new Nomz("data/tasks.txt").run();;
    }
}
