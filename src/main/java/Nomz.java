import java.util.Scanner;

public class Nomz {
    public static final String LINEBREAK = "-----------------------------------------";
    public static final String BYE = "Bye! hope to see you again soon!" ; 

    public static String[] textList = new String[100];
    public static int textListIdx = 0;

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
     * adds input to Nomz static textList
     * 
     * @param input String to be added
     */
    public static void addToList(String input) {
        textList[textListIdx] = input;
        textListIdx++;
        System.out.println(responseFormat("nomz added [" + input + "] to your nomz list!"));
    }

    /**
     * Prints contents of textList in a list format
     * @return Formatted string of array content
     */
    public static void printTextList(){
        String res = "";
        for(int i = 0; i < textListIdx; i++) {
            res += i + ". " + textList[i] + "\n";
        }

        System.out.println(responseFormat(res));
    }

    /**
     * Handles the logic of the chat
     * @param input User input
     */
    public static void chat(String input) {
        switch(input) {
            case "list":
                printTextList();
                break;
            default:
                addToList(input);
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
