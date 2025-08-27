package nomz.parser;

import nomz.commands.Command;
import nomz.commands.AddTodoCommand;
import nomz.commands.AddEventCommand;

import java.beans.Transient;
import java.time.LocalDateTime ;

import static nomz.common.Messages.MESSAGE_NO_DESCRIPTION_ARGUMENT;

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

}
