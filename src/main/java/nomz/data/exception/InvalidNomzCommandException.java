package nomz.data.exception;

import static nomz.common.Messages.MESSAGE_INVALID_COMMAND;

/**
 * Exception thrown for invalid commands in Nomz.
 */
public class InvalidNomzCommandException extends NomzException {

    /**
     * Creates an InvalidNomzCommandException
     */
    public InvalidNomzCommandException() {
        super(MESSAGE_INVALID_COMMAND);
    }

}
