package hu.psprog.leaflet.lcfa.core.facade;

import hu.psprog.leaflet.lcfa.core.domain.request.ArticleCommentRequest;

/**
 * Facade interface for article operations.
 *
 * @author Peter Smith
 */
public interface ArticleOperationFacade {

    /**
     * Processes a comment request.
     *
     * @param userID ID of the authenticated user, or {@code null} if anonymous
     * @param articleCommentRequest comment form contents as {@link ArticleCommentRequest} object
     * @return processing result status as boolean - {@code true} on success, {@code false} otherwise
     */
    boolean processCommentRequest(Long userID, ArticleCommentRequest articleCommentRequest);
}
