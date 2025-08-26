import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.File; 
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Nomz {
    private static final String DIRECTORYPATH = "./data";
    private static final String FILENAME = "nomz.txt";
    private static final Ui ui = new Ui();
    private static final Storage storage = new Storage("./data/nomz.txt");
    private static final DateParser dp = new DateParser();

    private static ArrayList<Task> taskList = new ArrayList<>();


    /**
     * Prints current task list to console
     *
     */
    public static void printTaskList() {
        String res = Messages.MESSAGE_TASK_LIST_HEADER;
        for(int i = 0; i < taskList.size(); i++) {
            Task t = taskList.get(i);
            int index = i + 1;
            res += index + ". " + t.toString() + "\n";
        }

        ui.show(res);

    }

    /**
     * Adds task to task list
     * @param task task to be added
     */
    public static void addTask(Task task) {
        taskList.add(task);
        try {
            storage.append(task);
        } catch (IOException e) {
            ui.showError(e.getMessage());
        }
        ui.show(String.format(Messages.MESSAGE_ADD_TASK, task.toString()));
    }

    /**
     * returns an int from String with error handling
     * @param index String to be converted
     * @return index of int type
     * @throws InvalidNomzArgumentException
     */
    public static int intFromString(String index) throws InvalidNomzArgumentException {
        int taskIndex = -1;
        try {
            taskIndex = Integer.valueOf(index);
        } catch (NumberFormatException e) {
            throw new InvalidNomzArgumentException(Messages.MESSAGE_INVALID_INTEGER_ARGUMENT);
        }

        return taskIndex;
    }

    /**
     * returns task in taskList given a String type index
     * @param index index of task in taskList
     * @return task 
     * @throws InvalidNomzArgumentException
     */
    public static Task getTaskFromString(String index) throws InvalidNomzArgumentException{
       
        int taskIndex = intFromString(index);

        if (taskIndex - 1 >= taskList.size()) {
            throw new InvalidNomzArgumentException(Messages.MESSAGE_INVALID_TASK_INDEX);
        }

        return taskList.get(taskIndex - 1);
    }

    /**
     * Marks/unmarks a task based on index given in args
     * 
     * @param args must have an integer in args[1]
     * @param toMark flag to set mark / unmark
     * @throws InvalidNomzArgumentException
     */
    public static void setTaskMark(String[] args, boolean toMark) throws InvalidNomzArgumentException {
        if(args.length < 2) {
            throw new InvalidNomzArgumentException(Messages.MESSAGE_NO_INDEX_ARGUMENT);
        }

        Task t = getTaskFromString(args[1]);
        if(toMark){
            t.mark();
            ui.show(String.format(Messages.MESSAGE_TASK_MARKED, t.toString()));
        } else {
            t.unmark();
            ui.show(String.format(Messages.MESSAGE_TASK_UNMARKED, t.toString()));
        }

        try {
            storage.saveAll(taskList);
        } catch (IOException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Creates a todo task and inserts it into the task list
     * @param args uses args[1] to end of args as the name of todo
     * @throws InvalidNomzArgumentException
     */
    public static void createTodo(String[] args) throws InvalidNomzArgumentException {
        if(args.length < 2) {
            throw new InvalidNomzArgumentException(Messages.MESSAGE_NO_DESCRIPTION_ARGUMENT);
        }
        String description = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        Todo todo = new Todo(description);
        addTask(todo);
    }

    /**
     * Creates a deadline task and inserts it into the task list
     * @param args searches for /by keyword in args. all arguments before keyword is used as name, 
     * and all arguments after are used as time
     * @throws InvalidNomzArgumentException
     */
    public static void createDeadline(String[] args) throws InvalidNomzArgumentException {
        if(args.length < 4) {
            throw new InvalidNomzArgumentException(Messages.MESSAGE_NO_ARGUMENTS);
        }

        for(int i = 2; i < args.length; i++) {
            if(args[i].equals("/by")){
                String description = String.join(" ", Arrays.copyOfRange(args, 1, i));
                String byRaw = String.join(" ", Arrays.copyOfRange(args, i+1, args.length));
                LocalDateTime by = dp.parseDateTimeFlexible(byRaw);
                if(by == null) {
                    addTask(new Deadline(description, byRaw));
                } else {
                    addTask(new Deadline(description, by));
                }
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
    public static void createEvent(String[] args) throws InvalidNomzArgumentException {
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

            LocalDateTime from = dp.parseDateTimeFlexible(fromRaw);
            LocalDateTime to = dp.parseDateTimeFlexible(toRaw);

            if(from == null || to == null) {
                addTask(new Event(description, fromRaw, toRaw));
            } else {
                addTask(new Event(description, from, to));
            }
        } 

    }

    /**
     * Deletes task based on index given
     * @param args args[1] must contain a valid index
     * @throws InvalidNomzArgumentException
     */
    private static void deleteTask(String[] args) throws InvalidNomzArgumentException {
        if(args.length < 2) {
            throw new InvalidNomzArgumentException(Messages.MESSAGE_NO_INDEX_ARGUMENT);
        }

        int index = intFromString(args[1]);
        taskList.remove(index - 1);
        try {
            storage.saveAll(taskList);
        } catch (IOException e) {
            ui.showError(e.getMessage());
        }
        ui.show(String.format(Messages.MESSAGE_DELETE_TASK, index));
    }


    /**
     * Handles the logic of the chat
     * @param input User input
     */
    public static void chat(String input) throws NomzException {
        String[] args = input.split("[\\s]");
        Command command = Command.fromString(args[0]);
        switch(command) {
        case LIST:
            printTaskList();
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
        // Greeting
        ui.showWelcome();
        try {
            taskList = storage.load();
        } catch (NomzException e) {
            ui.show(e.getMessage());
        }

        // Chat
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        while(!input.equals("bye")) {
            try {
                chat(input);
            } catch(NomzException e) {
                ui.showError(e.getMessage());
            } finally {
                input = sc.nextLine();
            }

        }
        ui.showGoodbye();
        sc.close();
    }
}
