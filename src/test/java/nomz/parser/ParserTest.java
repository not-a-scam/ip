package nomz.parser;

import nomz.commands.Command;
import nomz.commands.AddTodoCommand;
import nomz.commands.AddEventCommand;

import java.beans.Transient;
import java.time.LocalDateTime ;

import static nomz.common.Messages.MESSAGE_NO_DESCRIPTION_ARGUMENT;
import static nomz.common.Messages.MESSAGE_WRONG_FROM_KEYWORD;
import static nomz.common.Messages.MESSAGE_WRONG_TO_KEYWORD;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {
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
        Command expected = new AddEventCommand("project meeting", LocalDateTime.of(2024, 10, 10, 14, 0), LocalDateTime.of(2024, 10, 10, 16, 0));
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

}
