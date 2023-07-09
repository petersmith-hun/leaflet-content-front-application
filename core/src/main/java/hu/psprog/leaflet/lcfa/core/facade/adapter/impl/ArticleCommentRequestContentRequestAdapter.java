package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.request.comment.CommentCreateRequestModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.DefaultNonSuccessfulResponseException;
import hu.psprog.leaflet.bridge.client.exception.UnauthorizedAccessException;
import hu.psprog.leaflet.bridge.service.CommentBridgeService;
import hu.psprog.leaflet.lcfa.core.domain.request.ArticleCommentRequest;
import hu.psprog.leaflet.lcfa.core.exception.UserSessionInvalidationRequiredException;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import hu.psprog.leaflet.lcfa.core.facade.impl.utility.CommentCreateRequestFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * {@link ContentRequestAdapter} implementation for handling article comment requests.
 *
 * @author Peter Smith
 */
@Component
public class ArticleCommentRequestContentRequestAdapter implements ContentRequestAdapter<Boolean, ArticleCommentRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleCommentRequestContentRequestAdapter.class);

    private final CommentBridgeService commentBridgeService;
    private final CommentCreateRequestFactory commentCreateRequestFactory;

    @Autowired
    public ArticleCommentRequestContentRequestAdapter(CommentBridgeService commentBridgeService, CommentCreateRequestFactory commentCreateRequestFactory) {
        this.commentBridgeService = commentBridgeService;
        this.commentCreateRequestFactory = commentCreateRequestFactory;
    }

    @Override
    public Optional<Boolean> getContent(ArticleCommentRequest contentRequestParameter) {

        Boolean successful = null;
        CommentCreateRequestModel commentCreateRequestModel = commentCreateRequestFactory.create(contentRequestParameter);
        try {
            commentBridgeService.createComment(commentCreateRequestModel, contentRequestParameter.getRecaptchaToken());
            successful = true;
        } catch (UnauthorizedAccessException e) {
            throw new UserSessionInvalidationRequiredException(e);
        } catch (DefaultNonSuccessfulResponseException | CommunicationFailureException e) {
            LOGGER.error("Failed to create comment [{}]", contentRequestParameter, e);
        }

        return Optional.ofNullable(successful);
    }

    @Override
    public ContentRequestAdapterIdentifier getIdentifier() {
        return ContentRequestAdapterIdentifier.COMMENT_POST;
    }
}
