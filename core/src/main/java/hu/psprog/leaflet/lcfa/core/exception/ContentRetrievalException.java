package hu.psprog.leaflet.lcfa.core.exception;

/**
 * Exception to be thrown when a backend content retrieval request could not be fulfilled.
 *
 * @author Peter Smith
 */
public class ContentRetrievalException extends RuntimeException {

    public ContentRetrievalException(String message) {
        super(message);
    }

    public ContentRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }
}
