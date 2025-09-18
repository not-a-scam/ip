package nomz.data.tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Represents an event task in Nomz.
 */
public class Event extends Task {

    private static final DateTimeFormatter OUT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    private String from;
    private String to;
    private LocalDateTime fromTime = null;
    private LocalDateTime toTime = null;

    private Event(String description, ArrayList<String> tags) {
        super(description, TaskType.EVENT, tags);
    }

    /**
     * Creates an Event task with the specified description and String represented time.
     *
     * @param description
     * @param from
     * @param to
     */
    public Event(String description, String from, String to, ArrayList<String> tags) {
        this(description, tags);
        this.from = from;
        this.to = to;
    }

    /**
     * Creates an Event task with the specified description and LocalDateTime objects.
     *
     * @param description
     * @param fromTime
     * @param toTime
     */
    public Event(String description, LocalDateTime fromTime, LocalDateTime toTime, ArrayList<String> tags) {
        this(description, tags);
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    /**
     * Returns the LocalDateTime object for from time.
     *
     * @return
     */
    public LocalDateTime getFromTime() {
        return fromTime;
    }

    /**
     * Returns the LocalDateTime object for to time.
     *
     * @return
     */
    public LocalDateTime getToTime() {
        return toTime;
    }

    /**
     * Returns the raw from time.
     *
     * @return
     */
    public String getFromRaw() {
        return from;
    }

    /**
     * Returns the raw to time.
     *
     * @return
     */
    public String getToRaw() {
        return to;
    }

    @Override
    public String toString() {
        String str;
        if (fromTime == null || toTime == null) {
            str = super.toString() + " (from: " + from + " to: " + to + ")";
        } else {
            str = super.toString() + " (from: " + OUT.format(fromTime) + " to: " + OUT.format(toTime) + ")";
        }

        str += getTagsString();
        return str;
    }

    @Override
    public String toSavedString() {
        String str;
        if (fromTime == null || toTime == null) {
            str = super.toSavedString() + "|" + from + "|" + to;
        } else {
            str = super.toSavedString() + "|" + fromTime.toString() + "|" + toTime.toString();
        }

        str += "|" + getTagsString();
        return str;
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
