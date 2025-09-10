package nomz.commands;

import nomz.data.exception.InvalidNomzCommandException;

/**
 * Represents the different types of commands in the application.
 */
public enum CommandType {
    LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, FIND, TAG, BYE;

    /**
     * Converts a string representation of a command to its CommandType enum.
     *
     * @param input
     * @return
     * @throws InvalidNomzCommandException
     */
    public static CommandType fromString(String input) throws InvalidNomzCommandException {
        assert input != null : "String input should not be null";
        try {
            return CommandType.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidNomzCommandException();
        }
    }
}
