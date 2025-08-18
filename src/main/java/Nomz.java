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

    public static void markTask(String input) {
        try {
            int taskIndex = Integer.valueOf(input);
            if (taskIndex >= taskListIdx) {
                System.out.println(responseFormat("you gave nomz a bad input :(("));
            } else {
                Task t = taskList[taskIndex];
                t.mark();
                System.out.println(responseFormat("Nomz says good job!:\n" + t.toString()));
            }

        } catch (NumberFormatException e) {
            System.err.println("Invalid string format for integer conversion.");
        }
    }

    public static void unmarkTask(String input) {
        try {
            int taskIndex = Integer.valueOf(input);
            if (taskIndex >= taskListIdx) {
                System.out.println(responseFormat("you gave nomz a bad input :(("));
            } else {
                Task t = taskList[taskIndex];
                t.unmark();
                System.out.println(responseFormat("Nomz has unmarked your task:\n" + t.toString()));
            }

        } catch (NumberFormatException e) {
            System.err.println("Invalid string format for integer conversion.");
        }
    }

    public static void createTodo(String[] args) {
        String description = String.join(" ", args);
        Todo todo = new Todo(description);
        addTask(todo);
    }

    public static void createDeadline(String[] args) {
        for(int i = 0; i < args.length; i++) {
            if(args[i].equals("/by")){
                String description = String.join(" ", Arrays.copyOfRange(args, 0, i));
                String by = String.join(" ", Arrays.copyOfRange(args, i+1, args.length));

                addTask(new Deadline(description, by));
                return;
            }
        } 

        System.out.println(responseFormat("you gave nomz a bad input :(("));
    }

    /**
     * Handles the logic of the chat
     * @param input User input
     */
    public static void chat(String input) {
        String[] args = input.split("[\\s]");
        String command = args[0];
        switch(command) {
            case "list":
                printTaskList();
                break;
            case "mark":
                markTask(args[1]);
                break;
            case "unmark":
                unmarkTask(args[1]);
                break;
            case "todo":
                createTodo(Arrays.copyOfRange(args, 1, args.length));
                break;
            case "deadline":
                createDeadline(Arrays.copyOfRange(args, 1, args.length));
                break;
            default:
                break;
        }
    }

    public static void main(String[] args) {
        // Greeting
        System.out.println(responseFormat("Hi im nomz! \nhope you're having a nomztacular day"));        

        // Chat
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        while(!input.equals("bye")) {
            chat(input);
            input = sc.nextLine();
        }
        System.out.println(responseFormat(BYE));
        sc.close();
    }
}
