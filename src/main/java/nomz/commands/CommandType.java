package nomz.commands;
import nomz.data.exception.InvalidNomzCommandException;

public enum CommandType {
    LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, FIND, BYE;

    public static CommandType fromString(String input) throws InvalidNomzCommandException {
        try { 
            return CommandType.valueOf(input.toUpperCase()); 
        } catch (IllegalArgumentException e) { 
            throw new InvalidNomzCommandException(); 
        }
    }
}
