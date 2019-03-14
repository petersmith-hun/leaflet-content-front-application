package hu.psprog.leaflet.lcfa.core.domain.result;

/**
 * Possible result statuses of password reset demand and confirmation requests.
 *
 * @author Peter Smith
 */
public enum PasswordResetResult {

    /**
     * Password reset demand successfully processed.
     */
    DEMAND_PROCESSED,

    /**
     * Failed to process password reset demand.
     */
    DEMAND_FAILED,

    /**
     * Password reset confirmation successfully processed.
     */
    CONFIRMATION_PROCESSED,

    /**
     * Failed to process password reset confirmation.
     */
    CONFIRMATION_FAILED
}
