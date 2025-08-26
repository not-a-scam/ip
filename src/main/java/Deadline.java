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
        if(byTime.equals(null)) {
            return super.toString() + " (by: " + by + ")";
        }
        return super.toString() + " (by: " + OUT.format(byTime) + ")";
    }

    @Override
    public String savedString(){
        if(byTime.equals(null)) {
            return super.savedString() + "|" + by;
        }
        return super.savedString() + "|" + byTime.toString();
    }
}
