package nomz.data.exception;

import static nomz.common.Messages.MESSAGE_INVALID_COMMAND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ExceptionTest {
    @Test
    void nomzException_message_success() {
        NomzException e = new NomzException("Test message");
        assertEquals("Test message", e.getMessage());
    }

    @Test
    void invalidNomzArgumentException_message_success() {
        InvalidNomzArgumentException e = new InvalidNomzArgumentException("Invalid argument");
        assertEquals("Invalid argument", e.getMessage());
    }

    @Test
    void invalidNomzCommandException_message_success() {
        InvalidNomzCommandException e = new InvalidNomzCommandException();
        assertEquals(MESSAGE_INVALID_COMMAND, e.getMessage());
    }

    @Test
    void nomzException_isThrowable() {
        NomzException e = assertThrows(NomzException.class, () -> {
            throw new NomzException("fail");
        });
        assertNotNull(e);
    }

    @Test
    void invalidNomzArgumentException_isThrowable() {
        InvalidNomzArgumentException e = assertThrows(InvalidNomzArgumentException.class, () -> {
            throw new InvalidNomzArgumentException("fail");
        });
        assertNotNull(e);
    }

    @Test
    void invalidNomzCommandException_isThrowable() {
        InvalidNomzCommandException e = assertThrows(InvalidNomzCommandException.class, () -> {
            throw new InvalidNomzCommandException();
        });
        assertNotNull(e);
    }
}
