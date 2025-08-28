package nomz.data.tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a deadline task in Nomz.
 */
public class Deadline extends Task {

    private static final DateTimeFormatter OUT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    private LocalDateTime byTime = null;
    private String by;

    private Deadline(String description) {
        super(description, TaskType.DEADLINE);
    }
    
    /**
     * Creates a Deadline task with the specified description and a LocalDateTime object.
     * 
     * @param description
     * @param by
     */
    public Deadline(String description, LocalDateTime by) {
        this(description);
        this.byTime = by;
    }

    /**
     * Creates a Deadline task with the specified description and String represented time.
     * 
     * @param description
     * @param by
     */
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
