package nomz.data.tasks;

import nomz.data.exception.InvalidNomzCommandException;

/**
 * Represents the type of a task in Nomz.
 */
public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String symbol;

    TaskType(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Returns the symbol representing the task type.
     *
     * @return
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Returns the task type corresponding to the given symbol.
     *
     * @param symbol
     * @return
     * @throws InvalidNomzCommandException
     */
    public static TaskType fromSymbol(String symbol) throws InvalidNomzCommandException {
        for (TaskType type : TaskType.values()) {
            if (type.getSymbol().equalsIgnoreCase(symbol)) {
                return type;
            }
        }
        throw new InvalidNomzCommandException();
    }
}
