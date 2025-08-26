public enum Command {
    LIST,
    MARK,
    UNMARK,
    TODO,
    DEADLINE,
    EVENT,
    DELETE,
    BYE;

    public static Command fromString(String input) throws InvalidNomzCommandException {
        try {
            return Command.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidNomzCommandException();
        }
    }
}

