import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {

    protected LocalDateTime by = null;
    protected String byDescription;
    private static final DateTimeFormatter OUT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    public Deadline(String description, LocalDateTime by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    public Deadline(String description, String byDescription) {
        this(description, (LocalDateTime) null);
        this.byDescription = byDescription;  
    }

    @Override
    public String toString() {
        if(by.equals(null)) {
            return super.toString() + " (by: " + byDescription + ")";
        }
        return super.toString() + " (by: " + OUT.format(by) + ")";
    }

    @Override
    public String savedString(){
        if(by.equals(null)) {
            return super.savedString() + "|" + byDescription;
        }
        return super.savedString() + "|" + by.toString();
    }
}
