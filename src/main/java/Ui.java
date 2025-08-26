import java.util.Scanner;

public class Ui {

    private final Scanner scanner = new Scanner(System.in);

    public void show(String s) {
        System.out.println(Messages.MESSAGE_LINEBREAK + "\n" + s + "\n" + Messages.MESSAGE_LINEBREAK);
    }
    public void showWelcome() {
        show(Messages.MESSAGE_WELCOME);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showError(String message) {
        System.err.println(Messages.MESSAGE_LINEBREAK + "\n" + message + "\n" + Messages.MESSAGE_LINEBREAK);
    }

    public void showGoodbye() {
        show(Messages.MESSAGE_BYE);
        scanner.close();
    }
}
