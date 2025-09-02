package nomz.data.exception;

/**
 * Exception thrown for invalid arguments in Nomz.
 */
public class InvalidNomzArgumentException extends NomzException {

    /**
     * Creates an InvalidNomzArgumentException with the specified message.
     *
     * @param m
     */
    public InvalidNomzArgumentException(String m) {
        super(m);
    }
}
