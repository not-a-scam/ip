import java.util.Scanner;
import java.util.Arrays;

public class Nomz {
    public static final String LINEBREAK = "-----------------------------------------";
    public static final String BYE = "Bye! hope to see you again soon!" ; 

    public static Task[] taskList = new Task[100];
    public static int taskListIdx = 0;

    /**
     * Formats a given string to be printed as a response from the chatbot
     * 
     * @param input String to be formatted
     * @return Formatted string 
     */
    public static String responseFormat(String input) {
        return LINEBREAK + "\n" + input + "\n" + LINEBREAK;
    }

    public static void printTaskList() {
        String res = "here are the tasks in your nomz list:\n\n";
        for(int i = 0; i < taskListIdx; i++) {
            Task t = taskList[i];
            res += i + ". " + t.toString() + "\n";
        }

        System.out.println(responseFormat(res));

    }

    public static void addTask(Task task) {
        taskList[taskListIdx] = task;
        taskListIdx++;
        System.out.println(responseFormat("Nomz haz added:\n\t" + task.toString() + "\nto the nomz list!"));
    }

    public static Task getTaskFromString(String index) throws InvalidNomzArgumentException{
        int taskIndex = -1;
        try {
            taskIndex = Integer.valueOf(index);
        } catch (NumberFormatException e) {
            System.err.println("Invalid string format for integer conversion.");
            return null;
        }

        if (taskIndex >= taskListIdx) {
            throw new InvalidNomzArgumentException("task index is out of bounds!");
        }

        return taskList[taskIndex];
    }

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
    }

    public static void createTodo(String[] args) throws InvalidNomzArgumentException {
        if(args.length < 2) {
            throw new InvalidNomzArgumentException("you didnt specify the task :((");
        }
        String description = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        Todo todo = new Todo(description);
        addTask(todo);
    }

    public static void createDeadline(String[] args) throws InvalidNomzArgumentException {
        if(args.length < 4) {
            throw new InvalidNomzArgumentException("you don't have enough arguments :(");
        }

        for(int i = 2; i < args.length; i++) {
            if(args[i].equals("/by")){
                String description = String.join(" ", Arrays.copyOfRange(args, 1, i));
                String by = String.join(" ", Arrays.copyOfRange(args, i+1, args.length));

                addTask(new Deadline(description, by));
                return;
            }
        } 
        throw new InvalidNomzArgumentException("you didnt use the /by keyword :((");
    }

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
            String from = String.join(" ", Arrays.copyOfRange(args, fromIndex + 1, toIndex));
            String to = String.join(" ", Arrays.copyOfRange(args, toIndex + 1, args.length));

            addTask(new Event(description, from, to));
            return;
        } 

    }

    /**
     * Handles the logic of the chat
     * @param input User input
     */
    public static void chat(String input) throws NomzException {
        String[] args = input.split("[\\s]");
        String command = args[0];
        switch(command) {
            case "list":
                printTaskList();
                break;
            case "mark":
                setTaskMark(args, true);
                break;
            case "unmark":
                setTaskMark(args, false);
                break;
            case "todo":
                createTodo(args);
                break;
            case "deadline":
                createDeadline(args);
                break;
            case "event":
                createEvent(args);
                break;
            default:
                throw new InvalidNomzCommandException(input);
        }
    }

    public static void main(String[] args) {
        // Greeting
        System.out.println(responseFormat("Hi im nomz! \nhope you're having a nomztacular day"));        

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
