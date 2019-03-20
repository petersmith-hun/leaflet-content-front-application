package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentListDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.bridge.client.domain.OrderDirection;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.DefaultNonSuccessfulResponseException;
import hu.psprog.leaflet.bridge.service.CommentBridgeService;
import hu.psprog.leaflet.lcfa.core.domain.content.request.FilteredPaginationContentRequest;
import hu.psprog.leaflet.lcfa.core.domain.content.request.OrderBy;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * {@link ContentRequestAdapter} implementation to retrieve all comments of the specified user (should be used on profile page).
 *
 * @author Peter Smith
 */
@Component
public class UserCommentContentRequestAdapter implements ContentRequestAdapter<WrapperBodyDataModel<ExtendedCommentListDataModel>, FilteredPaginationContentRequest<Long, OrderBy.Comment>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCommentContentRequestAdapter.class);

    private CommentBridgeService commentBridgeService;

    @Autowired
    public UserCommentContentRequestAdapter(CommentBridgeService commentBridgeService) {
        this.commentBridgeService = commentBridgeService;
    }

    @Override
    public Optional<WrapperBodyDataModel<ExtendedCommentListDataModel>> getContent(FilteredPaginationContentRequest<Long, OrderBy.Comment> contentRequestParameter) {

        WrapperBodyDataModel<ExtendedCommentListDataModel> commentListDataModel = null;
        try {
            commentListDataModel = commentBridgeService.getPageOfCommentsForUser(contentRequestParameter.getFilterValue(), contentRequestParameter.getPage(),
                    contentRequestParameter.getLimit(), mapOrdering(contentRequestParameter), mapOrderDirection(contentRequestParameter));
        } catch (DefaultNonSuccessfulResponseException | CommunicationFailureException e) {
            LOGGER.error("Failed to retrieve comments for user [{}]", contentRequestParameter.getFilterValue(), e);
        }

        return Optional.ofNullable(commentListDataModel);
    }

    @Override
    public ContentRequestAdapterIdentifier getIdentifier() {
        return ContentRequestAdapterIdentifier.COMMENTS_OF_USER;
    }

    private hu.psprog.leaflet.bridge.client.domain.OrderBy.Comment mapOrdering(FilteredPaginationContentRequest<Long, OrderBy.Comment> paginatedContentRequest) {
        return hu.psprog.leaflet.bridge.client.domain.OrderBy.Comment.valueOf(paginatedContentRequest.getOrderBy().name());
    }

    private OrderDirection mapOrderDirection(FilteredPaginationContentRequest<Long, OrderBy.Comment> paginatedContentRequest) {
        return OrderDirection.valueOf(paginatedContentRequest.getOrderDirection().name());
    }
}
