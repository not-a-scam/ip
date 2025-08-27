package nomz.ui;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.io.InputStream;

import static nomz.common.Messages.MESSAGE_LINEBREAK;
import static nomz.common.Messages.MESSAGE_BYE;
import static nomz.common.Messages.MESSAGE_WELCOME;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;


class UiTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private Ui ui;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        ui = new Ui();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void show_printsFormattedMessage() {
        String testMsg = "Hello, world!";
        ui.show(testMsg);
        String expected = MESSAGE_LINEBREAK + "\n" + testMsg + "\n" + MESSAGE_LINEBREAK + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

    @Test
    void showWelcome_printsWelcomeMessage() {
        ui.showWelcome();
        String expected = MESSAGE_LINEBREAK + "\n" + MESSAGE_WELCOME + "\n" + MESSAGE_LINEBREAK + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

    @Test
    void showError_printsErrorMessageToErr() {
        String errorMsg = "An error occurred!";
        ui.showError(errorMsg);
        String expected = MESSAGE_LINEBREAK + "\n" + errorMsg + "\n" + MESSAGE_LINEBREAK + System.lineSeparator();
        assertEquals(expected, errContent.toString());
    }

    @Test
    void showGoodbye_printsGoodbyeMessageAndClosesScanner() {
        ui.showGoodbye();
        String expected = MESSAGE_LINEBREAK + "\n" + MESSAGE_BYE + "\n" + MESSAGE_LINEBREAK + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

    @Test
    void readCommand_readsInputLine() {
        String input = "test command" + System.lineSeparator();
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Ui testUi = new Ui();
        assertEquals("test command", testUi.readCommand());
        System.setIn(System.in);
    }
}