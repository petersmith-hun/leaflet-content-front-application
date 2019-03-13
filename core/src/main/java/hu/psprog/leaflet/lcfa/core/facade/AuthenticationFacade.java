package hu.psprog.leaflet.lcfa.core.facade;

import hu.psprog.leaflet.lcfa.core.domain.request.SignUpRequestModel;
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
}
