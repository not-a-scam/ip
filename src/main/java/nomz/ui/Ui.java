package nomz.ui;

import java.util.Scanner;

import nomz.common.Messages;

/**
 * Handles user interaction and input/output.
 */
public class Ui {

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Displays a message to the user.
     * 
     * @param s The message to display.
     */
    public void show(String s) {
        System.out.println(Messages.MESSAGE_LINEBREAK + "\n" + s + "\n" + Messages.MESSAGE_LINEBREAK);
    }

    /**
     * Displays a welcome message to the user.
     */
    public void showWelcome() {
        show(Messages.MESSAGE_WELCOME);
    }

    /**
     * Reads a command from the user input.
     * 
     * @return The command entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays an error message to the user.
     * 
     * @param message The error message to display.
     */
    public void showError(String message) {
        System.err.println(Messages.MESSAGE_LINEBREAK + "\n" + message + "\n" + Messages.MESSAGE_LINEBREAK);
    }

    /**
     * Displays a goodbye message to the user.
     */
    public void showGoodbye() {
        show(Messages.MESSAGE_BYE);
        scanner.close();
    }
}
