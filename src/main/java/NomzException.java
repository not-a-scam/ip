public class NomzException extends Exception{
    protected String input;
    
    public NomzException(String m, String input) {
        super(m);
        this.input = input;
    }
}
