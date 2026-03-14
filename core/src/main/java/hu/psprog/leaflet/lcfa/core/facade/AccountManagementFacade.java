package hu.psprog.leaflet.lcfa.core.facade;

import hu.psprog.leaflet.lcfa.core.domain.content.UserCommentsPageContent;

/**
 * Facade interface for account management operations.
 *
 * @author Peter Smith
 */
public interface AccountManagementFacade {

    /**
     * Retrieves list of comments created by the given (currently authenticated) user.
     *
     * @param userID ID of the authenticated user
     * @param page page number
     * @return list of existing comments with pagination information as {@link UserCommentsPageContent}
     */
    UserCommentsPageContent getCommentsForUser(Long userID, int page);

    /**
     * Performs logical deletion of the comment identified by given ID.
     * Users are able to logically delete their own comments!
     *
     * @param commentID ID of the comment to be deleted
     * @return operation result as boolean - {@code true} on success, {@code false} otherwise
     */
    boolean deleteComment(Long commentID);
}
