package hu.psprog.leaflet.lcfa.core.facade;

import hu.psprog.leaflet.api.rest.request.user.PasswordChangeRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UpdateProfileRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.account.AccountBaseInfo;
import hu.psprog.leaflet.lcfa.core.domain.content.UserCommentsPageContent;
import hu.psprog.leaflet.lcfa.core.domain.request.AccountDeletionRequest;

/**
 * Facade interface for account management operations.
 *
 * @author Peter Smith
 */
public interface AccountManagementFacade {

    /**
     * Retrieves basic account information for the given (currently authenticated) user.
     *
     * @param userID ID of the authenticated user
     * @return populated {@link AccountBaseInfo}
     */
    AccountBaseInfo getAccountBaseInfo(Long userID);

    /**
     * Updates basic account information for the given (currently authenticated) user.
     *
     * @param userID ID of the authenticated user
     * @param updateProfileRequestModel {@link UpdateProfileRequestModel} object containing the updated profile information
     * @return operation result as boolean - {@code true} on success, {@code false} otherwise
     */
    boolean updateAccountBaseInfo(Long userID, UpdateProfileRequestModel updateProfileRequestModel);

    /**
     * Updates password for the given (currently authenticated) user.
     *
     * @param userID ID of the authenticated user
     * @param passwordChangeRequestModel {@link PasswordChangeRequestModel} object containing the updated password
     * @return operation result as boolean - {@code true} on success, {@code false} otherwise
     */
    boolean updatePassword(Long userID, PasswordChangeRequestModel passwordChangeRequestModel);

    /**
     * Retrieves list of comments created by the given (currently authenticated) user.
     *
     * @param userID ID of the authenticated user
     * @param page page number
     * @return list of existing comments with pagination information as {@link UserCommentsPageContent}
     */
    UserCommentsPageContent getCommentsForUser(Long userID, int page);

    /**
     * Deletes the given (currently authenticated) user.
     *
     * @param userID ID of the authenticated user
     * @param accountDeletionRequest {@link AccountDeletionRequest} object containing confirmation information
     * @return operation result as boolean - {@code true} on success, {@code false} otherwise
     */
    boolean deleteAccount(Long userID, AccountDeletionRequest accountDeletionRequest);

    /**
     * Performs logical deletion of the comment identified by given ID.
     * Users are able to logically delete their own comments!
     *
     * @param commentID ID of the comment to be deleted
     * @return operation result as boolean - {@code true} on success, {@code false} otherwise
     */
    boolean deleteComment(Long commentID);
}
