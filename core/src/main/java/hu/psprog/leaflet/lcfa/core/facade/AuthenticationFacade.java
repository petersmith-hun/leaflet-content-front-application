package hu.psprog.leaflet.lcfa.core.facade;

import hu.psprog.leaflet.lcfa.core.domain.request.PasswordReclaimRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.request.PasswordResetConfirmationRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.request.SignUpRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.result.PasswordResetResult;
import hu.psprog.leaflet.lcfa.core.domain.result.SignUpResult;

/**
 * Facade interface for authentication related operations.
 *
 * @author Peter Smith
 */
public interface AuthenticationFacade {

    /**
     * Processes a sign-up request.
     *
     * @param signUpRequestModel {@link SignUpRequestModel} object containing data of the user to be registered
     * @return result of sign-up request processing as {@link SignUpResult}
     */
    SignUpResult signUp(SignUpRequestModel signUpRequestModel);

    /**
     * Processes a password reset demand request.
     * This call should trigger starting the reset process on backend site.
     *
     * @param passwordReclaimRequestModel {@link PasswordReclaimRequestModel} object containing the necessary data (email address and ReCaptcha token)
     * @return result status as {@link PasswordResetResult}
     */
    PasswordResetResult requestPasswordReset(PasswordReclaimRequestModel passwordReclaimRequestModel);

    /**
     * Processes a password reset confirmation request.
     * This call should update the user's password on backend side in case of a valid demand.
     *
     * @param passwordResetConfirmationRequestModel {@link PasswordResetConfirmationRequestModel} object containing the necessary data (new password and ReCaptcha token)
     * @param reclaimToken reclaim token provided by the backend via a password reset demand call
     * @return result status as {@link PasswordResetResult}
     */
    PasswordResetResult confirmPasswordReset(PasswordResetConfirmationRequestModel passwordResetConfirmationRequestModel, String reclaimToken);
}
