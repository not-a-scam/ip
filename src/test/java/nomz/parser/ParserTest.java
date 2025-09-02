package nomz.parser;

import static nomz.common.Messages.MESSAGE_NO_ARGUMENTS;
import static nomz.common.Messages.MESSAGE_NO_DESCRIPTION_ARGUMENT;
import static nomz.common.Messages.MESSAGE_WRONG_FROM_KEYWORD;
import static nomz.common.Messages.MESSAGE_WRONG_TO_KEYWORD;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import nomz.commands.AddDeadlineCommand;
import nomz.commands.AddEventCommand;
import nomz.commands.AddTodoCommand;
import nomz.commands.Command;
import nomz.data.tasks.Deadline;
import nomz.data.tasks.Event;
import nomz.data.tasks.Task;
import nomz.data.tasks.Todo;

public class ParserTest {

    // Test for parse method
    @Test
    public void parse_validTodo_returnsTodo() {
        String input = "todo read book";
        Command expected = new AddTodoCommand("read book");
        Command result = null;
        try {
            result = Parser.parse(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expected, result);
    }

    @Test
    public void parse_validTodoWithExtraSpaces_returnsTodo() {
        String input = "todo    read book";
        Command expected = new AddTodoCommand("read book");
        Command result = null;
        try {
            result = Parser.parse(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expected, result);
    }

    @Test
    public void parse_validTodoWithTrailingSpaces_returnsTodo() {
        String input = "todo read book    ";
        Command expected = new AddTodoCommand("read book");
        Command result = null;
        try {
            result = Parser.parse(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expected, result);
    }

    @Test
    public void parse_invalidTodoNoDescription_throwsException() {
        String input = "todo";
        Command result = null;
        try {
            result = Parser.parse(input);
        } catch (Exception e) {
            assertEquals(MESSAGE_NO_DESCRIPTION_ARGUMENT, e.getMessage());
        }
    }

    @Test
    public void parse_validEventWithDateTime_returnsEventWithDateTime() {
        String input = "event project meeting /from 2024-10-10 14:00 /to 2024-10-10 16:00";
        Command expected = new AddEventCommand(
            "project meeting",
            LocalDateTime.of(2024, 10, 10, 14, 0),
            LocalDateTime.of(2024, 10, 10, 16, 0)
        );
        Command result = null;
        try {
            result = Parser.parse(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expected, result);
    }

    @Test
    public void parse_validEventWithStringTime_returnsEventWithStringTime() {
        String input = "event project meeting /from monday /to sunday";
        Command expected = new AddEventCommand("project meeting", "monday", "sunday");
        Command result = null;
        try {
            result = Parser.parse(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expected, result);
    }

    @Test
    public void parse_invalidEventNoDescription_throwsException() {
        String input = "event";
        Command result = null;
        try {
            result = Parser.parse(input);
        } catch (Exception e) {
            assertEquals(MESSAGE_WRONG_FROM_KEYWORD, e.getMessage());
        }
    }

    @Test
    public void parse_invalidEventNoFrom_throwsException() {
        String input = "event project meeting /to 2024-10-10 16:00";
        Command result = null;
        try {
            result = Parser.parse(input);
        } catch (Exception e) {
            assertEquals(MESSAGE_WRONG_FROM_KEYWORD, e.getMessage());
        }
    }

    @Test
    public void parse_invalidEventNoTo_throwsException() {
        String input = "event project meeting /from 2024-10-10 14:00";
        Command result = null;
        try {
            result = Parser.parse(input);
        } catch (Exception e) {
            assertEquals(MESSAGE_WRONG_TO_KEYWORD, e.getMessage());
        }
    }

    @Test
    public void parse_invalidEventWrongOrder_throwsException() {
        String input = "event project meeting /to 2024-10-10 14:00 /from 2024-10-10 16:00";
        Command result = null;
        try {
            result = Parser.parse(input);
        } catch (Exception e) {
            assertEquals(MESSAGE_WRONG_TO_KEYWORD, e.getMessage());
        }
    }

    @Test
    public void parse_invalidEventLackArguments_throwsException() {
        String input = "event /from /to";
        Command result = null;
        try {
            result = Parser.parse(input);
        } catch (Exception e) {
            assertEquals(MESSAGE_WRONG_FROM_KEYWORD, e.getMessage());
        }
    }

    @Test
    public void parse_validDeadlineWithDateTime_returnsDeadlineWithDateTime() {
        String input = "deadline submit report /by 2024-10-10 23:59";
        Command expected = new AddDeadlineCommand("submit report", LocalDateTime.of(2024, 10, 10, 23, 59));
        Command result = null;
        try {
            result = Parser.parse(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expected, result);
    }

    @Test
    public void parse_validDeadlineWithStringTime_returnsDeadlineWithStringTime() {
        String input = "deadline submit report /by tomorrow";
        Command expected = new AddDeadlineCommand("submit report", "tomorrow");
        Command result = null;
        try {
            result = Parser.parse(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expected, result);
    }

    @Test
    public void parse_invalidDeadlineNoArgs_throwsException() {
        String input = "deadline";
        Command result = null;
        try {
            result = Parser.parse(input);
        } catch (Exception e) {
            assertEquals(MESSAGE_NO_ARGUMENTS, e.getMessage());
        }
    }

    @Test
    public void parse_invalidDeadlineNoByDescription_throwsException() {
        String input = "deadline submit report /by";
        Command result = null;
        try {
            result = Parser.parse(input);
        } catch (Exception e) {
            assertEquals(MESSAGE_NO_ARGUMENTS, e.getMessage());
        }
    }

    @Test
    public void parse_invalidDeadlineNoBy_throwsException() {
        String input = "deadline submit report ";
        Command result = null;
        try {
            result = Parser.parse(input);
        } catch (Exception e) {
            assertEquals(MESSAGE_NO_ARGUMENTS, e.getMessage());
        }
    }

    @Test
    public void parse_invalidDeadlineNoDescription_throwsException() {
        String input = "deadline /by noon";
        Command result = null;
        try {
            result = Parser.parse(input);
        } catch (Exception e) {
            assertEquals(MESSAGE_NO_ARGUMENTS, e.getMessage());
        }
    }

    @Test
    public void parseTaskFileContent_validTodoUnmarked_returnsTodo() {
        String input = "T|0|read book";
        Task expected = new Todo("read book");
        Task result = null;
        try {
            result = Parser.parseTaskFileContent(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expected, result);
    }

    @Test
    public void parseTaskFileContent_validTodoMarked_returnsTodo() {
        String input = "T|1|read book";
        Task expected = new Todo("read book");
        expected.mark();
        Task result = null;
        try {
            result = Parser.parseTaskFileContent(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expected, result);
    }

    @Test
    public void parseTaskFileContent_validDeadlineUnmarkedWithDateTime_returnsDeadlineWithDateTime() {
        String input = "D|0|submit report|2024-10-10 23:59";
        Task expected = new Deadline("submit report", LocalDateTime.of(2024, 10, 10, 23, 59));
        Task result = null;
        try {
            result = Parser.parseTaskFileContent(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expected, result);
    }

    @Test
    public void parseTaskFileContent_validDeadlineUnmarkedWithStringTime_returnsDeadlineWithStringTime() {
        String input = "D|0|submit report|tomorrow";
        Task expected = new Deadline("submit report", "tomorrow");
        Task result = null;
        try {
            result = Parser.parseTaskFileContent(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expected, result);
    }

    @Test
    public void parseTaskFileContent_validDeadlineMarkedWithStringTime_returnsDeadlineWithStringTime() {
        String input = "D|1|submit report|tomorrow";
        Task expected = new Deadline("submit report", "tomorrow");
        expected.mark();
        Task result = null;
        try {
            result = Parser.parseTaskFileContent(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expected, result);
    }

    @Test
    public void parseTaskFileContent_validDeadlineMarkedWithDateTime_returnsDeadlineWithDateTime() {
        String input = "D|1|submit report|2024-10-10 23:59";
        Task expected = new Deadline("submit report", LocalDateTime.of(2024, 10, 10, 23, 59));
        expected.mark();
        Task result = null;
        try {
            result = Parser.parseTaskFileContent(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expected, result);
    }

    @Test
    public void parseTaskFileContent_validEventUnmarkedWithDateTime_returnsEventWithDateTime() {
        String input = "E|0|team meeting|2024-10-10 10:00|2024-10-10 11:00";
        Task expected = new Event(
            "team meeting",
            LocalDateTime.of(2024, 10, 10, 10, 0),
            LocalDateTime.of(2024, 10, 10, 11, 0)
        );
        Task result = null;
        try {
            result = Parser.parseTaskFileContent(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expected, result);
    }

    @Test
    public void parseTaskFileContent_validEventUnmarkedWithStringTime_returnsEventWithStringTime() {
        String input = "E|0|team meeting|tomorrow|sunday";
        Task expected = new Event("team meeting", "tomorrow", "sunday");
        Task result = null;
        try {
            result = Parser.parseTaskFileContent(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expected, result);
    }

    @Test
    public void parseTaskFileContent_validEventMarkedWithStringTime_returnsEventWithStringTime() {
        String input = "E|1|team meeting|tomorrow|sunday";
        Task expected = new Event("team meeting", "tomorrow", "sunday");
        expected.mark();
        Task result = null;
        try {
            result = Parser.parseTaskFileContent(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expected, result);
    }

    @Test
    public void parseTaskFileContent_validEventMarkedWithDateTime_returnsEventWithDateTime() {
        String input = "E|1|team meeting|2024-10-10 10:00|2024-10-10 11:00";
        Task expected = new Event(
            "team meeting",
            LocalDateTime.of(2024, 10, 10, 10, 0),
            LocalDateTime.of(2024, 10, 10, 11, 0)
        );
        expected.mark();
        Task result = null;
        try {
            result = Parser.parseTaskFileContent(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expected, result);
    }
}
