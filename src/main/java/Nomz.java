import java.util.Scanner;

public class Nomz {
    public static final String LINEBREAK = "-----------------------------------------";
    public static final String BYE = "Bye! hope to see you again soon!" ; 

    /**
     * Formats a given string to be printed as a response from the chatbot
     * 
     * @param input String to be formatted
     * @return Formatted string 
     */
    public static String responseFormat(String input) {
        return LINEBREAK + "\n" + input + "\n" + LINEBREAK;
    }

    /**
     * Echoes the user input
     * 
     * @param input User chat input
     * @return Formatted chat response
     */
    public static String echo(String input) {
        return responseFormat(input);
    }

    public static void main(String[] args) {
        // Greeting
        System.out.println(responseFormat("Hi im nomz! \nhope you're having a nomztacular day"));        

        // Chat
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        while(!input.equals("bye")) {
            System.out.println(echo(input));
            input = sc.nextLine();
        }
        System.out.println(responseFormat(BYE));
        sc.close();
    }
}
