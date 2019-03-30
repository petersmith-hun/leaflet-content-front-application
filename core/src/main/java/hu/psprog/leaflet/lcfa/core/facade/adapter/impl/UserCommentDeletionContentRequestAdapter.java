package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.DefaultNonSuccessfulResponseException;
import hu.psprog.leaflet.bridge.client.exception.UnauthorizedAccessException;
import hu.psprog.leaflet.bridge.service.CommentBridgeService;
import hu.psprog.leaflet.lcfa.core.exception.UserSessionInvalidationRequiredException;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * {@link ContentRequestAdapter} implementation for handling logical comment deletion requests.
 *
 * @author Peter Smith
 */
@Component
public class UserCommentDeletionContentRequestAdapter implements ContentRequestAdapter<Boolean, Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCommentDeletionContentRequestAdapter.class);

    private CommentBridgeService commentBridgeService;

    @Autowired
    public UserCommentDeletionContentRequestAdapter(CommentBridgeService commentBridgeService) {
        this.commentBridgeService = commentBridgeService;
    }

    @Override
    public Optional<Boolean> getContent(Long contentRequestParameter) {

        Boolean successful = null;
        try {
            commentBridgeService.deleteCommentLogically(contentRequestParameter);
            successful = true;
        } catch (UnauthorizedAccessException e) {
            throw new UserSessionInvalidationRequiredException(e);
        } catch (DefaultNonSuccessfulResponseException | CommunicationFailureException e) {
            LOGGER.error("Failed to logically delete comment of ID [{}]", contentRequestParameter, e);
        }

        return Optional.ofNullable(successful);
    }

    @Override
    public ContentRequestAdapterIdentifier getIdentifier() {
        return ContentRequestAdapterIdentifier.COMMENT_DELETION;
    }
}
