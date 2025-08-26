import java.util.Scanner;
import java.util.Arrays;
import java.io.IOException;
import java.time.LocalDateTime;

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

    /**
     * Marks/unmarks a task based on index given in args
     * 
     * @param args must have an integer in args[1]
     * @param toMark flag to set mark / unmark
     * @throws InvalidNomzArgumentException
     */
    public void setTaskMark(String[] args, boolean toMark) throws InvalidNomzArgumentException {
        if(args.length < 2) {
            throw new InvalidNomzArgumentException(Messages.MESSAGE_NO_INDEX_ARGUMENT);
        }

        Task t = taskList.get(Parser.intFromString(args[1]));
        if(toMark){
            t.mark();
            ui.show(String.format(Messages.MESSAGE_TASK_MARKED, t.toString()));
        } else {
            t.unmark();
            ui.show(String.format(Messages.MESSAGE_TASK_UNMARKED, t.toString()));
        }

        try {
            storage.saveAll(taskList.getTasks());
        } catch (IOException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Creates a todo task and inserts it into the task list
     * @param args uses args[1] to end of args as the name of todo
     * @throws InvalidNomzArgumentException
     */
    public void createTodo(String[] args) throws InvalidNomzArgumentException {
        if(args.length < 2) {
            throw new InvalidNomzArgumentException(Messages.MESSAGE_NO_DESCRIPTION_ARGUMENT);
        }
        String description = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        Todo todo = new Todo(description);
        taskList.add(todo);
        ui.show(Messages.MESSAGE_ADD_TASK.formatted(todo.toString()));
    }

    /**
     * Creates a deadline task and inserts it into the task list
     * @param args searches for /by keyword in args. all arguments before keyword is used as name, 
     * and all arguments after are used as time
     * @throws InvalidNomzArgumentException
     */
    public void createDeadline(String[] args) throws InvalidNomzArgumentException {
        if(args.length < 4) {
            throw new InvalidNomzArgumentException(Messages.MESSAGE_NO_ARGUMENTS);
        }

        for(int i = 2; i < args.length; i++) {
            if(args[i].equals("/by")){
                String description = String.join(" ", Arrays.copyOfRange(args, 1, i));
                String byRaw = String.join(" ", Arrays.copyOfRange(args, i+1, args.length));
                LocalDateTime by = Parser.parseDateTimeFlexible(byRaw);
                Deadline deadline;
                if(by == null) {
                    deadline = new Deadline(description, byRaw);
                } else {
                    deadline = new Deadline(description, by);
                }
                taskList.add(deadline);
                ui.show(Messages.MESSAGE_ADD_TASK.formatted(deadline.toString()));
                return;
            }
        } 
        throw new InvalidNomzArgumentException(Messages.MESSAGE_NO_BY_KEYWORD);
    }

    /**
     * Creates an event task and inserts it into the task list
     * @param args add args before /from is used as name, all args between /from & /to used as from timing, 
     * all args after /to used as to timing.
     * @throws InvalidNomzArgumentException
     */
    public void createEvent(String[] args) throws InvalidNomzArgumentException {
        int fromIndex = -1;
        int toIndex = -1;
        for(int i = 1; i < args.length; i++) {
            if(args[i].equals("/from")) {
                fromIndex = i;
            } else if(args[i].equals("/to")){
                toIndex = i;
            }
        }

        if(fromIndex <= 1) {
            throw new InvalidNomzArgumentException(Messages.MESSAGE_WRONG_FROM_KEYWORD);
        }

        if (toIndex <= fromIndex || toIndex <= 3 || toIndex == args.length - 1) { // toIndex must be > from index
            throw new InvalidNomzArgumentException(Messages.MESSAGE_WRONG_TO_KEYWORD);
        }

        if(fromIndex > -1 && toIndex > -1) {
            String description = String.join(" ", Arrays.copyOfRange(args, 1, fromIndex));
            String fromRaw = String.join(" ", Arrays.copyOfRange(args, fromIndex + 1, toIndex));
            String toRaw = String.join(" ", Arrays.copyOfRange(args, toIndex + 1, args.length));

            LocalDateTime from = Parser.parseDateTimeFlexible(fromRaw);
            LocalDateTime to = Parser.parseDateTimeFlexible(toRaw);
            Event event;
            if(from == null || to == null) {
                event = new Event(description, fromRaw, toRaw);
            } else {
                event = new Event(description, from, to);
            }
            taskList.add(event);
            ui.show(Messages.MESSAGE_ADD_TASK.formatted(event.toString()));
        }

    }

    /**
     * Deletes task based on index given
     * @param args args[1] must contain a valid index
     * @throws InvalidNomzArgumentException
     */
    private void deleteTask(String[] args) throws InvalidNomzArgumentException {
        if(args.length < 2) {
            throw new InvalidNomzArgumentException(Messages.MESSAGE_NO_INDEX_ARGUMENT);
        }

        int index = Parser.intFromString(args[1]);
        taskList.delete(index);
        try {
            storage.saveAll(taskList.getTasks());
        } catch (IOException e) {
            ui.showError(e.getMessage());
        }
        ui.show(String.format(Messages.MESSAGE_DELETE_TASK, index));
    }


    /**
     * Handles the logic of the chat
     * @param input User input
     */
    public void chat(String input) throws NomzException {
        String[] args = input.split("[\\s]");
        Command command = Command.fromString(args[0]);
        switch(command) {
        case LIST:
            ui.show(taskList.toDisplayString());
            break;
        case MARK:
            setTaskMark(args, true);
            break;
        case UNMARK:
            setTaskMark(args, false);
            break;
        case TODO:
            createTodo(args);
            break;
        case DEADLINE:
            createDeadline(args);
            break;
        case EVENT:
            createEvent(args);
            break;
        case DELETE:
            deleteTask(args);
            break;
        default:
            throw new InvalidNomzCommandException();
        }
    }

    public static void main(String[] args) {

        Nomz nomz = new Nomz("data/tasks.txt");

        // Chat
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        while(!input.equals("bye")) {
            try {
                nomz.chat(input);
            } catch(NomzException e) {
                nomz.ui.showError(e.getMessage());
            } finally {
                input = sc.nextLine();
            }

        }
        nomz.ui.showGoodbye();
        sc.close();
    }
}
