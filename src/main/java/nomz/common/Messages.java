package nomz.common;

/**
 * Contains the various messages used in the application.
 */
public class Messages {
    // UI
    public static final String MESSAGE_LINEBREAK = "-----------------------------------------";
    public static final String MESSAGE_BYE = "Bye! hope to see you again soon!" ; 
    public static final String MESSAGE_WELCOME = "hi im nomz! \nhope you're having a nomztacular day";
    public static final String MESSAGE_LOAD_TASK_SUCCESS = "Nomz has successfully loaded all previous tasks!";
    public static final String MESSAGE_TASK_LIST_HEADER = "here are the tasks in your nomz list:\n\n";

    // Tasks
    public static final String MESSAGE_ADD_TASK = "Nomz haz added:\n\t%s\nto the nomz list!";
    public static final String MESSAGE_TASK_UNMARKED = "Nomz has unmarked your task:\n%s";
    public static final String MESSAGE_TASK_MARKED = "Nomz says good job!:\n%s";
    public static final String MESSAGE_DELETE_TASK = "nomz haz removed task %s from the nomz list";

    // Errors
    public static final String MESSAGE_INVALID_INTEGER_ARGUMENT = "your index argument is not a valid integer!";
    public static final String MESSAGE_INVALID_TASK_INDEX = "task index is out of bounds!";
    public static final String MESSAGE_INVALID_FORMAT = "the file contains an invalid format :(";
    
    public static final String MESSAGE_NO_INDEX_ARGUMENT = "you need to provide an index argument :((";
    public static final String MESSAGE_NO_DESCRIPTION_ARGUMENT = "you didnt specify the task :((";
    public static final String MESSAGE_NO_ARGUMENTS = "you don't have enough arguments :(";
    public static final String MESSAGE_NO_BY_KEYWORD = "you didnt use the /by keyword :((";

    public static final String MESSAGE_WRONG_FROM_KEYWORD = "you didnt use the /from keyword properly :((";
    public static final String MESSAGE_WRONG_TO_KEYWORD = "you didnt use the /to keyword properly :(("; 
}
