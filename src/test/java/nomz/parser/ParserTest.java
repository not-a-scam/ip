package nomz.parser;

import nomz.commands.Command;
import nomz.commands.AddTodoCommand;

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
}
