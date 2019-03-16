package hu.psprog.leaflet.lcfa.core.facade.impl.utility;

import hu.psprog.leaflet.api.rest.request.comment.CommentCreateRequestModel;
import hu.psprog.leaflet.lcfa.core.domain.request.ArticleCommentRequest;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Factory for properly creating {@link CommentCreateRequestModel} objects.
 *
 * @author Peter Smith
 */
@Component
public class CommentCreateRequestFactory {

    /**
     * Creates a properly populated {@link CommentCreateRequestModel}.
     * If {@code authenticatedUserID} is a non-null value, adds it the request object and ignores email/name fields of {@link ArticleCommentRequest} object.
     * Otherwise it populates the relevant fields in the request.
     *
     * @param authenticatedUserID ID of the authenticated user ({@code null} for anonymous users)
     * @param articleCommentRequest {@link ArticleCommentRequest} object containing request data
     * @return created {@link CommentCreateRequestModel}
     */
    public CommentCreateRequestModel create(Long authenticatedUserID, ArticleCommentRequest articleCommentRequest) {

        CommentCreateRequestModel commentCreateRequestModel = new CommentCreateRequestModel();
        commentCreateRequestModel.setEntryId(articleCommentRequest.getEntryId());
        commentCreateRequestModel.setContent(articleCommentRequest.getMessage());

        if (Objects.nonNull(authenticatedUserID)) {
            commentCreateRequestModel.setAuthenticatedUserId(authenticatedUserID);
        } else {
            commentCreateRequestModel.setUsername(articleCommentRequest.getName());
            commentCreateRequestModel.setEmail(articleCommentRequest.getEmail());
        }

        return commentCreateRequestModel;
    }
}
