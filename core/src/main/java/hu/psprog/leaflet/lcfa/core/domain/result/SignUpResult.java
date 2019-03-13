package hu.psprog.leaflet.lcfa.core.domain.result;

/**
 * Possible sign-up request results.
 *
 * @author Peter Smith
 */
public enum SignUpResult {

    /**
     * Sign-up request has been successfully processed.
     */
    SUCCESS,

    /**
     * Given email address is already in use.
     */
    ADDRESS_IN_USE,

    /**
     * Other processing error.
     */
    FAILURE
}
