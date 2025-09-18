package nomz.data.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class TaskTest {
    @Test
    void todo_markUnmark_success() {
        Todo todo = new Todo("read book", new ArrayList<>());
        assertFalse(todo.isDone());
        todo.mark();
        assertTrue(todo.isDone());
        todo.unmark();
        assertFalse(todo.isDone());
    }

    @Test
    void deadline_withDateTime_success() {
        LocalDateTime dt = LocalDateTime.of(2025, 9, 18, 23, 59);
        Deadline deadline = new Deadline("submit", dt, new ArrayList<>());
        assertEquals("submit", deadline.getDescription());
        assertEquals(dt, deadline.getByTime());
    }

    @Test
    void deadline_withString_success() {
        Deadline deadline = new Deadline("submit", "tomorrow", new ArrayList<>());
        assertEquals("submit", deadline.getDescription());
        assertEquals("tomorrow", deadline.getByRaw());
    }

    @Test
    void event_withDateTime_success() {
        LocalDateTime from = LocalDateTime.of(2025, 9, 18, 10, 0);
        LocalDateTime to = LocalDateTime.of(2025, 9, 18, 12, 0);
        Event event = new Event("meeting", from, to, new ArrayList<>());
        assertEquals("meeting", event.getDescription());
        assertEquals(from, event.getFromTime());
        assertEquals(to, event.getToTime());
    }

    @Test
    void event_withString_success() {
        Event event = new Event("meeting", "Monday", "Tuesday", new ArrayList<>());
        assertEquals("meeting", event.getDescription());
        assertEquals("Monday", event.getFromRaw());
        assertEquals("Tuesday", event.getToRaw());
    }

    @Test
    void todo_tags_success() {
        ArrayList<String> tags = new ArrayList<>();
        tags.add("urgent");
        tags.add("school");
        Todo todo = new Todo("read book", tags);
        assertTrue(todo.getTagsString().contains("urgent"));
        assertTrue(todo.getTagsString().contains("school"));
    }

    @Test
    void task_equality_success() {
        Todo t1 = new Todo("read book", new ArrayList<>());
        Todo t2 = new Todo("read book", new ArrayList<>());
        assertEquals(t1, t2);
        t1.mark();
        assertNotEquals(t1, t2);
    }

    @Test
    void todo_roundTrip_success() throws Exception {
        ArrayList<String> tags = new ArrayList<>();
        tags.add("urgent");
        Todo todo = new Todo("read book", tags);
        String saved = todo.toSavedString();
        Task loaded = nomz.parser.Parser.parseTaskFileContent(saved);
        assertEquals(todo, loaded);
    }
}
