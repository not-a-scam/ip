package nomz.data.tasks;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {

    protected LocalDateTime byTime = null;
    protected String by;
    private static final DateTimeFormatter OUT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    private Deadline(String description) {
        super(description, TaskType.DEADLINE);
    }
    
    public Deadline(String description, LocalDateTime by) {
        this(description);
        this.byTime = by;
    }

    public Deadline(String description, String by) {
        this(description);
        this.by = by;  
    }

    @Override
    public String toString() {
        if(byTime == null) {
            return super.toString() + " (by: " + by + ")";
        }
        return super.toString() + " (by: " + OUT.format(byTime) + ")";
    }

    @Override
    public String savedString(){
        if(byTime == null) {
            return super.savedString() + "|" + by;
        }
        return super.savedString() + "|" + byTime.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }
        if (!(other instanceof Deadline)) {
            return false;
        }
        Deadline deadline = (Deadline) other;
        if (byTime == null) {
            return (by.equals(deadline.by));
        }
        return byTime.equals(deadline.byTime);
    }
}
