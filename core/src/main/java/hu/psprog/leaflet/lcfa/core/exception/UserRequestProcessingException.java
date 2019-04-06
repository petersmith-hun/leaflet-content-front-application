package hu.psprog.leaflet.lcfa.core.exception;

/**
 * Exception to be thrown when a request made by the user cannot be processed.
 *
 * @author Peter Smith
 */
public class UserRequestProcessingException extends RuntimeException {

    public UserRequestProcessingException(String message) {
        super(message);
    }
}
