package hu.psprog.leaflet.lcfa.core.exception;

/**
 * Exception to be thrown when the user's session (JWT token) is invalidated on the backend side and so it needs to be dropped.
 *
 * @author Peter Smith
 */
public class UserSessionInvalidationRequiredException extends RuntimeException {

    private static final String USER_SESSION_INVALIDATED_MESSAGE = "User session invalidated on backend - dropping session";

    public UserSessionInvalidationRequiredException(Exception exception) {
        super(USER_SESSION_INVALIDATED_MESSAGE, exception);
    }
}
