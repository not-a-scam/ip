import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.File; 
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Nomz {
    private static final String LINEBREAK = "-----------------------------------------";
    private static final String BYE = "Bye! hope to see you again soon!" ; 
    private static final String DIRECTORYPATH = "./data";
    private static final String FILENAME = "nomz.txt";

    private static ArrayList<Task> taskList = new ArrayList<>();


private static final DateTimeFormatter[] DATE_TIME_FORMATS = new DateTimeFormatter[] {
    DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"),
    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
    DateTimeFormatter.ofPattern("d/M/yyyy HHmm"),
    DateTimeFormatter.ofPattern("d/M/yyyy HH:mm"),
    DateTimeFormatter.ISO_LOCAL_DATE_TIME
};

private static final DateTimeFormatter[] DATE_ONLY_FORMATS = new DateTimeFormatter[] {
    DateTimeFormatter.ofPattern("yyyy-MM-dd"),
    DateTimeFormatter.ofPattern("d/M/yyyy"),
    DateTimeFormatter.ISO_LOCAL_DATE
};

    private static LocalDateTime parseDateTimeFlexible(String s) {
        for (DateTimeFormatter f : DATE_TIME_FORMATS) {
            try {
                return LocalDateTime.parse(s, f);
            } catch (DateTimeParseException ignored) {}
        }

        for (DateTimeFormatter f : DATE_ONLY_FORMATS) {
        try {
            return LocalDate.parse(s, f).atStartOfDay();
        } catch (DateTimeParseException ignored) {}
    }

        return null;
    }

    /**
     * Formats a given string to be printed as a response from the chatbot
     * 
     * @param input String to be formatted
     * @return Formatted string 
     */
    public static String responseFormat(String input) {
        return LINEBREAK + "\n" + input + "\n" + LINEBREAK;
    }

    public static void createTaskListFromFile(String directoryPath, String filename) {
        try {
            File directory = new File(directoryPath);
            if(!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directoryPath, filename);
            if(!file.createNewFile()) {
                Scanner s = new Scanner(file);
                while (s.hasNext()) {
                    parseTaskFileContent(s.nextLine());
                }
                s.close();
                System.out.println("Nomz has successfully loaded all previous tasks!");
                printTaskList();
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void parseTaskFileContent(String f) {
        try {
            String[] args = f.split("[\\|]");
            TaskType type = TaskType.fromSymbol(args[0]);
            boolean done = args[1].equals("1");
            switch(type){
            case TODO:
                Todo todo = new Todo(args[2]);
                initializeTask(todo, done);
                break;
            case DEADLINE:
                LocalDateTime by = parseDateTimeFlexible(args[3]);
                Deadline deadline;
                if (by == null) {
                    deadline = new Deadline(args[2], args[3]);
                } else {
                    deadline = new Deadline(args[2], by);
                }
                initializeTask(deadline, done);
                break;
            case EVENT:
                LocalDateTime from = parseDateTimeFlexible(args[3]);
                LocalDateTime to = parseDateTimeFlexible(args[4]);
                Event event;
                if (from == null || to == null) {
                    event = new Event(args[2], args[3], args[4]);
                } else {
                    event = new Event(args[2], from, to);
                }
                initializeTask(event, done);
                break;
            }
        } catch (NomzException e) {
            System.err.println(responseFormat(e.getMessage()));
        }
    }

    public static void writeTaskToFile(Task task){
        try {
            FileWriter fw = new FileWriter(DIRECTORYPATH + "/" + FILENAME, true);
            fw.write(task.savedString() + "\n");
            fw.close();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Prints current task list to console
     *
     */
    public static void printTaskList() {
        String res = "here are the tasks in your nomz list:\n\n";
        for(int i = 0; i < taskList.size(); i++) {
            Task t = taskList.get(i);
            int index = i + 1;
            res += index + ". " + t.toString() + "\n";
        }

        System.out.println(responseFormat(res));

    }

    /**
     * Adds task to task list
     * @param task task to be added
     */
    public static void addTask(Task task) {
        taskList.add(task);
        writeTaskToFile(task);
        System.out.println(responseFormat("Nomz haz added:\n\t" + task.toString() + "\nto the nomz list!"));
    }

    public static void initializeTask(Task task, boolean done) {
        if(done) {
            task.mark();
        }
        taskList.add(task);
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
            throw new InvalidNomzArgumentException("your index argument is not a valid integer!");
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
            throw new InvalidNomzArgumentException("task index is out of bounds!");
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
            throw new InvalidNomzArgumentException("you need to provide an index argument :((");
        }

        Task t = getTaskFromString(args[1]);
        if(toMark){
            t.mark();
            System.out.println(responseFormat("Nomz says good job!:\n" + t.toString()));
        } else {
            t.unmark();
            System.out.println(responseFormat("Nomz has unmarked your task:\n" + t.toString()));
        }

        rewriteFile();
    }

    /**
     * Creates a todo task and inserts it into the task list
     * @param args uses args[1] to end of args as the name of todo
     * @throws InvalidNomzArgumentException
     */
    public static void createTodo(String[] args) throws InvalidNomzArgumentException {
        if(args.length < 2) {
            throw new InvalidNomzArgumentException("you didnt specify the task :((");
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
            throw new InvalidNomzArgumentException("you don't have enough arguments :(");
        }

        for(int i = 2; i < args.length; i++) {
            if(args[i].equals("/by")){
                String description = String.join(" ", Arrays.copyOfRange(args, 1, i));
                String byRaw = String.join(" ", Arrays.copyOfRange(args, i+1, args.length));
                LocalDateTime by = parseDateTimeFlexible(byRaw);
                if(by == null) {
                    addTask(new Deadline(description, byRaw));
                } else {
                    addTask(new Deadline(description, by));
                }
                return;
            }
        } 
        throw new InvalidNomzArgumentException("you didnt use the /by keyword :((");
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
            throw new InvalidNomzArgumentException("you didn't use the /from keyword properly :(");
        }

        if (toIndex <= fromIndex || toIndex <= 3 || toIndex == args.length - 1) { // toIndex must be > from index
            throw new InvalidNomzArgumentException("you didn't use the /to keyword correctly");
        }

        if(fromIndex > -1 && toIndex > -1) {
            String description = String.join(" ", Arrays.copyOfRange(args, 1, fromIndex));
            String fromRaw = String.join(" ", Arrays.copyOfRange(args, fromIndex + 1, toIndex));
            String toRaw = String.join(" ", Arrays.copyOfRange(args, toIndex + 1, args.length));

            LocalDateTime from = parseDateTimeFlexible(fromRaw);
            LocalDateTime to = parseDateTimeFlexible(toRaw);

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
            throw new InvalidNomzArgumentException("you need to provide an index argument :((");
        }

        int index = intFromString(args[1]);
        taskList.remove(index - 1);
        rewriteFile();
        System.out.println(responseFormat("nomz haz removed task " + index + " from the nomz list"));
    }

    public static void rewriteFile() {
    try {
        FileWriter fw = new FileWriter(DIRECTORYPATH + "/" + FILENAME, false); // overwrite mode
        for (Task task : taskList) {
            fw.write(task.savedString() + "\n");
        }
        fw.close();
    } catch (IOException e) {
        System.err.println("Error: " + e.getMessage());
    }
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
        System.out.println(responseFormat("Hi im nomz! \nhope you're having a nomztacular day")); 
        createTaskListFromFile(DIRECTORYPATH, FILENAME);       

        // Chat
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        while(!input.equals("bye")) {
            try {
                chat(input);
            } catch(NomzException e) {
                System.err.println(responseFormat(e.getMessage()));
            } finally {
                input = sc.nextLine();
            }

        }
        System.out.println(responseFormat(BYE));
        sc.close();
    }
}
