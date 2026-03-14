package hu.psprog.leaflet.lcfa.core.facade.impl;

import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentListDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.lcfa.core.config.DefaultPaginationAttributes;
import hu.psprog.leaflet.lcfa.core.domain.content.UserCommentsPageContent;
import hu.psprog.leaflet.lcfa.core.domain.content.request.FilteredPaginationContentRequest;
import hu.psprog.leaflet.lcfa.core.domain.content.request.OrderBy;
import hu.psprog.leaflet.lcfa.core.exception.UserRequestProcessingException;
import hu.psprog.leaflet.lcfa.core.facade.AccountManagementFacade;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Implementation of {@link AccountManagementFacade}.
 *
 * @author Peter Smith
 */
@Service
public class AccountManagementFacadeImpl implements AccountManagementFacade {


    private final ContentRequestAdapterRegistry contentRequestAdapterRegistry;
    private final ConversionService conversionService;
    private final DefaultPaginationAttributes<OrderBy.Comment> defaultPaginationAttributes;

    @Autowired
    public AccountManagementFacadeImpl(ContentRequestAdapterRegistry contentRequestAdapterRegistry, ConversionService conversionService,
                                       DefaultPaginationAttributes<OrderBy.Comment> defaultPaginationAttributes) {
        this.contentRequestAdapterRegistry = contentRequestAdapterRegistry;
        this.conversionService = conversionService;
        this.defaultPaginationAttributes = defaultPaginationAttributes;
    }

    @Override
    public UserCommentsPageContent getCommentsForUser(Long userID, int page) {

        assertUserIsAuthenticated(userID);

        return contentRequestAdapterRegistry.<WrapperBodyDataModel<ExtendedCommentListDataModel>, FilteredPaginationContentRequest<Long, OrderBy.Comment>>getContentRequestAdapter(ContentRequestAdapterIdentifier.COMMENTS_OF_USER)
                .getContent(createFilteredRequest(userID, page))
                .map(response -> conversionService.convert(response, UserCommentsPageContent.class))
                .orElse(UserCommentsPageContent.EMPTY_CONTENT);
    }

    @Override
    public boolean deleteComment(Long commentID) {
        return contentRequestAdapterRegistry.<Boolean, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.COMMENT_DELETION)
                .getContent(commentID)
                .orElse(false);
    }

    private void assertUserIsAuthenticated(Long userID) {
        if (Objects.isNull(userID)) {
            throw new UserRequestProcessingException("User is not authenticated");
        }
    }

    private FilteredPaginationContentRequest<Long, OrderBy.Comment> createFilteredRequest(Long userID, int page) {
        return FilteredPaginationContentRequest.<Long, OrderBy.Comment>builder()
                .filterValue(userID)
                .page(page)
                .limit(defaultPaginationAttributes.getLimit())
                .orderBy(defaultPaginationAttributes.getOrderBy())
                .orderDirection(defaultPaginationAttributes.getOrderDirection())
                .build();
    }
}
