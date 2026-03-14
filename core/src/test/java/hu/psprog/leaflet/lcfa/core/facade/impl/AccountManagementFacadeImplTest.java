package hu.psprog.leaflet.lcfa.core.facade.impl;

import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentDataModel;
import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentListDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.lcfa.core.config.DefaultPaginationAttributes;
import hu.psprog.leaflet.lcfa.core.domain.content.CommentSummary;
import hu.psprog.leaflet.lcfa.core.domain.content.UserCommentsPageContent;
import hu.psprog.leaflet.lcfa.core.domain.content.request.FilteredPaginationContentRequest;
import hu.psprog.leaflet.lcfa.core.domain.content.request.OrderBy;
import hu.psprog.leaflet.lcfa.core.domain.content.request.OrderDirection;
import hu.psprog.leaflet.lcfa.core.exception.UserRequestProcessingException;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link AccountManagementFacadeImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class AccountManagementFacadeImplTest {

    private static final long USER_ID = 1L;
    private static final int PAGE_NUMBER = 3;
    private static final int ITEM_LIMIT_ON_PAGE = 10;
    private static final OrderBy.Comment ORDER_BY = OrderBy.Comment.CREATED;
    private static final OrderDirection ORDER_DIRECTION = OrderDirection.ASC;
    private static final long COMMENT_ID = 8L;
    private static final String CONTENT = "comment";
    private static final FilteredPaginationContentRequest<Long, OrderBy.Comment> FILTERED_PAGINATION_CONTENT_REQUEST = FilteredPaginationContentRequest.<Long, OrderBy.Comment>builder()
            .filterValue(USER_ID)
            .page(PAGE_NUMBER)
            .limit(ITEM_LIMIT_ON_PAGE)
            .orderBy(ORDER_BY)
            .orderDirection(ORDER_DIRECTION)
            .build();
    private static final WrapperBodyDataModel<ExtendedCommentListDataModel> WRAPPED_EXTENDED_COMMENT_LIST_DATA_MODEL = WrapperBodyDataModel.<ExtendedCommentListDataModel>getBuilder()
            .withBody(ExtendedCommentListDataModel.getBuilder()
                    .withComments(List.of(ExtendedCommentDataModel.getBuilder().withContent(CONTENT).build()))
                    .build())
            .build();
    private static final UserCommentsPageContent USER_COMMENTS_PAGE_CONTENT = UserCommentsPageContent.builder()
            .comments(Collections.singletonList(CommentSummary.builder().content(CONTENT).build()))
            .build();

    @Mock
    private ContentRequestAdapterRegistry contentRequestAdapterRegistry;

    @Mock
    private ConversionService conversionService;

    @Mock
    private DefaultPaginationAttributes<OrderBy.Comment> defaultPaginationAttributes;

    @Mock
    private ContentRequestAdapter<WrapperBodyDataModel<ExtendedCommentListDataModel>, FilteredPaginationContentRequest<Long, OrderBy.Comment>> userCommentsContentRequestAdapter;

    @Mock
    private ContentRequestAdapter<Boolean, Long> accountDeletionContentRequestAdapter;

    @InjectMocks
    private AccountManagementFacadeImpl accountManagementFacade;

    @Test
    public void shouldGetCommentForUserReturnWithSuccess() {

        // given
        given(defaultPaginationAttributes.getLimit()).willReturn(ITEM_LIMIT_ON_PAGE);
        given(defaultPaginationAttributes.getOrderBy()).willReturn(ORDER_BY);
        given(defaultPaginationAttributes.getOrderDirection()).willReturn(ORDER_DIRECTION);
        given(contentRequestAdapterRegistry.<WrapperBodyDataModel<ExtendedCommentListDataModel>, FilteredPaginationContentRequest<Long, OrderBy.Comment>>getContentRequestAdapter(ContentRequestAdapterIdentifier.COMMENTS_OF_USER))
                .willReturn(userCommentsContentRequestAdapter);
        given(userCommentsContentRequestAdapter.getContent(FILTERED_PAGINATION_CONTENT_REQUEST)).willReturn(Optional.of(WRAPPED_EXTENDED_COMMENT_LIST_DATA_MODEL));
        given(conversionService.convert(WRAPPED_EXTENDED_COMMENT_LIST_DATA_MODEL, UserCommentsPageContent.class)).willReturn(USER_COMMENTS_PAGE_CONTENT);

        // when
        UserCommentsPageContent result = accountManagementFacade.getCommentsForUser(USER_ID, PAGE_NUMBER);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(USER_COMMENTS_PAGE_CONTENT));
    }

    @Test
    public void shouldGetCommentForUserThrowUserRequestProcessingExceptionForNullUserID() {

        // when
        Assertions.assertThrows(UserRequestProcessingException.class,
                () -> accountManagementFacade.getCommentsForUser(null, PAGE_NUMBER));

        // then
        // exception expected
    }

    @Test
    public void shouldGetCommentForUserReturnWithEmptyContentObjectForMissingData() {

        // given
        given(defaultPaginationAttributes.getLimit()).willReturn(ITEM_LIMIT_ON_PAGE);
        given(defaultPaginationAttributes.getOrderBy()).willReturn(ORDER_BY);
        given(defaultPaginationAttributes.getOrderDirection()).willReturn(ORDER_DIRECTION);
        given(contentRequestAdapterRegistry.<WrapperBodyDataModel<ExtendedCommentListDataModel>, FilteredPaginationContentRequest<Long, OrderBy.Comment>>getContentRequestAdapter(ContentRequestAdapterIdentifier.COMMENTS_OF_USER))
                .willReturn(userCommentsContentRequestAdapter);
        given(userCommentsContentRequestAdapter.getContent(FILTERED_PAGINATION_CONTENT_REQUEST)).willReturn(Optional.empty());

        // when
        UserCommentsPageContent result = accountManagementFacade.getCommentsForUser(USER_ID, PAGE_NUMBER);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(UserCommentsPageContent.EMPTY_CONTENT));
    }

    @Test
    public void shouldDeleteCommentReturnWithSuccess() {

        // given
        given(contentRequestAdapterRegistry.<Boolean, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.COMMENT_DELETION))
                .willReturn(accountDeletionContentRequestAdapter);
        given(accountDeletionContentRequestAdapter.getContent(COMMENT_ID)).willReturn(Optional.of(true));

        // when
        boolean result = accountManagementFacade.deleteComment(COMMENT_ID);

        // then
        assertThat(result, is(true));
    }

    @Test
    public void shouldDeleteCommentReturnWithFailureForMissingData() {

        // given
        given(contentRequestAdapterRegistry.<Boolean, Long>getContentRequestAdapter(ContentRequestAdapterIdentifier.COMMENT_DELETION))
                .willReturn(accountDeletionContentRequestAdapter);
        given(accountDeletionContentRequestAdapter.getContent(COMMENT_ID)).willReturn(Optional.empty());

        // when
        boolean result = accountManagementFacade.deleteComment(COMMENT_ID);

        // then
        assertThat(result, is(false));
    }
}
