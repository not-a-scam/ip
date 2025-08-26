public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String symbol;

    TaskType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static TaskType fromSymbol(String symbol) {
        for (TaskType type : TaskType.values()) {
            if (type.getSymbol().equalsIgnoreCase(symbol)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown task type symbol: " + symbol);
    }
}
