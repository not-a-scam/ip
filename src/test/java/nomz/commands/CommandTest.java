package nomz.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class CommandTest {
    @Test
    void addTodoCommand_equality_success() {
        AddTodoCommand c1 = new AddTodoCommand("read book");
        AddTodoCommand c2 = new AddTodoCommand("read book");
        assertEquals(c1, c2);
    }

    @Test
    void addDeadlineCommand_dateTimeEquality_success() {
        LocalDateTime dt = LocalDateTime.of(2025, 9, 18, 23, 59);
        AddDeadlineCommand c1 = new AddDeadlineCommand("submit", dt);
        AddDeadlineCommand c2 = new AddDeadlineCommand("submit", dt);
        assertEquals(c1, c2);
    }

    @Test
    void addEventCommand_dateTimeEquality_success() {
        LocalDateTime from = LocalDateTime.of(2025, 9, 18, 10, 0);
        LocalDateTime to = LocalDateTime.of(2025, 9, 18, 12, 0);
        AddEventCommand c1 = new AddEventCommand("meeting", from, to);
        AddEventCommand c2 = new AddEventCommand("meeting", from, to);
        assertEquals(c1, c2);
    }

    @Test
    void addDeadlineCommand_stringEquality_success() {
        AddDeadlineCommand c1 = new AddDeadlineCommand("submit", "tomorrow");
        AddDeadlineCommand c2 = new AddDeadlineCommand("submit", "tomorrow");
        assertEquals(c1, c2);
    }

    @Test
    void addEventCommand_stringEquality_success() {
        AddEventCommand c1 = new AddEventCommand("meeting", "tomorrow", "afternoon");
        AddEventCommand c2 = new AddEventCommand("meeting", "tomorrow", "afternoon");
        assertEquals(c1, c2);
    }

    @Test
    void markCommand_equality_success() {
        MarkCommand c1 = new MarkCommand(1, true);
        MarkCommand c2 = new MarkCommand(1, true);
        assertEquals(c1, c2);
        MarkCommand c3 = new MarkCommand(1, false);
        assertNotEquals(c1, c3);
    }

    @Test
    void deleteCommand_equality_success() {
        DeleteCommand c1 = new DeleteCommand(1);
        DeleteCommand c2 = new DeleteCommand(1);
        assertEquals(c1, c2);
    }

    @Test
    void tagCommand_equality_success() {
        TagCommand c1 = new TagCommand(1, "urgent");
        TagCommand c2 = new TagCommand(1, "urgent");
        assertEquals(c1, c2);
    }

    @Test
    void findCommand_equality_success() {
        FindCommand c1 = new FindCommand("homework");
        FindCommand c2 = new FindCommand("homework");
        assertEquals(c1, c2);
    }
}
