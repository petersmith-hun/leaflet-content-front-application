package hu.psprog.leaflet.lcfa.core.exception;

/**
 * Exception to be thrown when an explicitly requested content is not found.
 *
 * @author Peter Smith
 */
public class ContentNotFoundException extends ContentRetrievalException {

    public ContentNotFoundException(String message) {
        super(message);
    }
}
