package hu.psprog.leaflet.lcfa.core.facade;

import hu.psprog.leaflet.lcfa.core.domain.request.SignUpRequestModel;

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
     */
    void signUp(SignUpRequestModel signUpRequestModel);
}
