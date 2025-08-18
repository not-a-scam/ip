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

    public static void main(String[] args) {
        System.out.println(responseFormat("Hi im nomz! \nhope you're having a nomztacular day"));
        System.out.println(responseFormat(BYE));
    }
}
