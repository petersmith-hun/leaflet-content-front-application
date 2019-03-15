package hu.psprog.leaflet.lcfa.core.facade.impl;

import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.DefaultNonSuccessfulResponseException;
import hu.psprog.leaflet.jwt.auth.support.service.AuthenticationService;
import hu.psprog.leaflet.lcfa.core.domain.request.AccountDeletionRequest;
import hu.psprog.leaflet.lcfa.core.exception.UserRequestProcessingException;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Handler component for account deletion requests.
 *
 * @author Peter Smith
 */
@Component
class AccountDeletionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountDeletionHandler.class);

    private ContentRequestAdapterRegistry contentRequestAdapterRegistry;
    private AuthenticationService authenticationService;

    @Autowired
    public AccountDeletionHandler(ContentRequestAdapterRegistry contentRequestAdapterRegistry, AuthenticationService authenticationService) {
        this.contentRequestAdapterRegistry = contentRequestAdapterRegistry;
        this.authenticationService = authenticationService;
    }

    /**
     * Deletes the given (currently authenticated) user.
     * Account deletion is a complex operation, consists of the following steps:
     *  1) Re-authentication: user is re-authenticated to ensure their intention. Re-authenticated claims a new authenticated token, and revokes the current one.
     *     If this step fails, we cannot move on with processing the deletion request.
     *  2) Actually deleting the account (via Bridge).
     *  3) Forcibly log the user out and destroying the security context.
     *
     * @param userID ID of the authenticated user
     * @param accountDeletionRequest {@link AccountDeletionRequest} object containing confirmation information
     * @return operation result as boolean - {@code true} on success, {@code false} otherwise
     */
    boolean deleteAccount(Long userID, AccountDeletionRequest accountDeletionRequest) {

        assertUserIsAuthenticated(userID);
        boolean successful = false;
        if (reAuthenticate(userID, accountDeletionRequest)) {
            successful = deleteAccount(userID);
            if (successful) {
                forceLogout();
            }
        }

        return successful;
    }

    private void assertUserIsAuthenticated(Long userID) {
        if (Objects.isNull(userID)) {
            throw new UserRequestProcessingException("User is not authenticated");
        }
    }

    private boolean reAuthenticate(Long userID, AccountDeletionRequest accountDeletionRequest) {
        return contentRequestAdapterRegistry.<ExtendedUserDataModel, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_BASE_INFO)
                .getContent(userID)
                .map(extendedUserDataModel -> doReAuthenticate(accountDeletionRequest, extendedUserDataModel))
                .orElse(false);
    }

    private Boolean doReAuthenticate(AccountDeletionRequest accountDeletionRequest, ExtendedUserDataModel extendedUserDataModel) {

        boolean successful = true;
        Authentication authenticationToken = createAuthenticationToken(accountDeletionRequest, extendedUserDataModel);
        try {
            Authentication renewedAuthentication = authenticationService.claimToken(authenticationToken);
            authenticationService.revokeToken();
            SecurityContextHolder.getContext().setAuthentication(renewedAuthentication);
        } catch (DefaultNonSuccessfulResponseException | CommunicationFailureException e) {
            LOGGER.error("Failed to reauthenticate user.", e);
            successful = false;
        }

        return successful;
    }

    private UsernamePasswordAuthenticationToken createAuthenticationToken(AccountDeletionRequest accountDeletionRequest, ExtendedUserDataModel extendedUserDataModel) {
        return new UsernamePasswordAuthenticationToken(extendedUserDataModel.getEmail(), accountDeletionRequest.getPassword());
    }

    private boolean deleteAccount(Long userID) {
        return contentRequestAdapterRegistry.<Boolean, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_DELETE)
                .getContent(userID)
                .orElse(false);
    }

    private void forceLogout() {
        try {
            authenticationService.revokeToken();
            SecurityContextHolder.clearContext();
        } catch (DefaultNonSuccessfulResponseException | CommunicationFailureException e) {
            LOGGER.error("Failed to sign-out user", e);
        }
    }
}
