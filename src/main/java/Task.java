public class Task {
    protected String description;
    protected boolean isDone;
    protected TaskType type;

    public Task(String description, TaskType type) {
        this.description = description;
        this.isDone = false;
        this.type = type;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    public String savedString() {
        return type.getSymbol() + "|" + (isDone ? 1 : 0) + "|" + description;
    }

    @Override
    public String toString(){
        return "["+ type.getSymbol() + "][" + getStatusIcon() + "] " + description;  
    }
}

