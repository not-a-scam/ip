package nomz.data.tasks;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task{

    private static final DateTimeFormatter OUT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    private String from;
    private String to;
    private LocalDateTime fromTime = null;
    private LocalDateTime toTime = null;

    private Event(String description) {
        super(description, TaskType.EVENT);
    }

    public Event(String description, String from, String to) {
        this(description);
        this.from = from;
        this.to = to;
    }

    public Event(String description, LocalDateTime fromTime, LocalDateTime toTime) {
        this(description);
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    @Override
    public String toString() {
        if (fromTime == null || toTime == null) {
            return super.toString() + " (from: " + from + " to: " + to + ")";
        }
        return super.toString() + " (from: " + OUT.format(fromTime) + " to: " + OUT.format(toTime) + ")";
    }

    @Override
    public String savedString(){
        if (fromTime == null || toTime == null) {
            return super.savedString() + "|" + from + "|" + to;
        }
        return super.savedString() + "|" + fromTime.toString() + "|" + toTime.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }
        if (!(other instanceof Event)) {
            return false;
        }
        Event event = (Event) other;
        if (fromTime == null) {
            return (from.equals(event.from) && to.equals(event.to));
        }

        return fromTime.equals(event.fromTime) && toTime.equals(event.toTime);
    }
}
