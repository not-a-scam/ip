package nomz.data.tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Represents a deadline task in Nomz.
 */
public class Deadline extends Task {

    private static final DateTimeFormatter OUT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    private LocalDateTime byTime = null;
    private String by;

    private Deadline(String description, ArrayList<String> tags) {
        super(description, TaskType.DEADLINE, tags);
    }

    /**
     * Creates a Deadline task with the specified description and a LocalDateTime object.
     *
     * @param description
     * @param by
     */
    public Deadline(String description, LocalDateTime by, ArrayList<String> tags) {
        this(description, tags);
        this.byTime = by;
    }

    /**
     * Creates a Deadline task with the specified description and String represented time.
     *
     * @param description
     * @param by
     */
    public Deadline(String description, String by, ArrayList<String> tags) {
        this(description, tags);
        this.by = by;
    }

    /**
     * Returns the LocalDateTime object for by time.
     *
     * @return
     */
    public LocalDateTime getByTime() {
        return byTime;
    }

    /**
     * Returns the raw by time.
     *
     * @return
     */
    public String getByRaw() {
        return by;
    }

    @Override
    public String toString() {
        String str;
        if (byTime == null) {
            str = super.toString() + " (by: " + by + ")";
        } else {
            str = super.toString() + " (by: " + OUT.format(byTime) + ")";
        }

        str += getTagsString();
        return str;

    }

    @Override
    public String toSavedString() {
        String str;
        if (byTime == null) {
            str = super.toSavedString() + "|" + by;
        } else {
            str = super.toSavedString() + "|" + byTime.toString();
        }

        str += "|" + getTagsString();
        return str;
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
