package hu.psprog.leaflet.lcfa.core.facade.impl.utility;

import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
import hu.psprog.leaflet.lcfa.core.exception.UserRequestProcessingException;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Handler component for account deletion requests.
 *
 * @author Peter Smith
 */
@Component
public class AccountDeletionHandler {

    private final ContentRequestAdapterRegistry contentRequestAdapterRegistry;

    @Autowired
    public AccountDeletionHandler(ContentRequestAdapterRegistry contentRequestAdapterRegistry) {
        this.contentRequestAdapterRegistry = contentRequestAdapterRegistry;
    }

    /**
     * Deletes the given (currently authenticated) user.
     * Account deletion is a complex operation, consists of the following steps:
     *  1) Check if the user is still authorized (by requesting user data).
     *     If this step fails, we cannot move on with processing the deletion request.
     *  2) Actually deleting the account (via Bridge).
     *  3) Forcibly logging the user out and destroying the security context.
     *
     * @param userID ID of the authenticated user
     * @return operation result as boolean - {@code true} on success, {@code false} otherwise
     */
    public boolean deleteAccount(Long userID) {

        assertUserIsAuthenticated(userID);
        boolean successful = false;
        if (checkIfStillAuthorized(userID)) {
            successful = doDeleteAccount(userID);
            if (successful) {
                SecurityContextHolder.clearContext();
            }
        }

        return successful;
    }

    private void assertUserIsAuthenticated(Long userID) {

        if (Objects.isNull(userID)) {
            throw new UserRequestProcessingException("User is not authenticated");
        }
    }

    private boolean checkIfStillAuthorized(Long userID) {

        return contentRequestAdapterRegistry.<ExtendedUserDataModel, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_BASE_INFO)
                .getContent(userID)
                .isPresent();
    }

    private boolean doDeleteAccount(Long userID) {

        return contentRequestAdapterRegistry.<Boolean, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.PROFILE_DELETE)
                .getContent(userID)
                .orElse(false);
    }
}
