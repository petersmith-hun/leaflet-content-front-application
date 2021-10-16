package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentListDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.bridge.client.domain.OrderBy;
import hu.psprog.leaflet.bridge.client.domain.OrderDirection;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.UnauthorizedAccessException;
import hu.psprog.leaflet.bridge.service.CommentBridgeService;
import hu.psprog.leaflet.lcfa.core.domain.content.request.FilteredPaginationContentRequest;
import hu.psprog.leaflet.lcfa.core.exception.UserSessionInvalidationRequiredException;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link UserCommentContentRequestAdapter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class UserCommentContentRequestAdapterTest {

    private static final long USER_ID = 1L;
    private static final int PAGE_NUMBER = 1;
    private static final int ITEM_LIMIT_ON_PAGE = 10;
    private static final OrderBy.Comment ORDER_BY = OrderBy.Comment.CREATED;
    private static final OrderDirection ORDER_DIRECTION = OrderDirection.DESC;
    private static final FilteredPaginationContentRequest<Long, hu.psprog.leaflet.lcfa.core.domain.content.request.OrderBy.Comment> CONTENT_REQUEST =
            FilteredPaginationContentRequest.<Long, hu.psprog.leaflet.lcfa.core.domain.content.request.OrderBy.Comment>builder()
                    .filterValue(USER_ID)
                    .page(PAGE_NUMBER)
                    .limit(ITEM_LIMIT_ON_PAGE)
                    .orderBy(hu.psprog.leaflet.lcfa.core.domain.content.request.OrderBy.Comment.CREATED)
                    .orderDirection(hu.psprog.leaflet.lcfa.core.domain.content.request.OrderDirection.DESC)
                    .build();
    private static final WrapperBodyDataModel<ExtendedCommentListDataModel> WRAPPED_COMMENT_LIST_DATA_MODEL = WrapperBodyDataModel.getBuilder()
            .withBody(ExtendedCommentListDataModel.getBuilder().build())
            .build();

    @Mock
    private CommentBridgeService commentBridgeService;

    @InjectMocks
    private UserCommentContentRequestAdapter adapter;

    @Test
    public void shouldGetContentReturnWithSuccess() throws CommunicationFailureException {

        // given
        given(commentBridgeService.getPageOfCommentsForUser(USER_ID, PAGE_NUMBER, ITEM_LIMIT_ON_PAGE, ORDER_BY, ORDER_DIRECTION))
                .willReturn(WRAPPED_COMMENT_LIST_DATA_MODEL);

        // when
        Optional<WrapperBodyDataModel<ExtendedCommentListDataModel>> result = adapter.getContent(CONTENT_REQUEST);

        // then
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), equalTo(WRAPPED_COMMENT_LIST_DATA_MODEL));
    }

    @Test
    public void shouldGetContentReturnThrowUserSessionInvalidationRequiredException() throws CommunicationFailureException {

        // given
        doThrow(UnauthorizedAccessException.class).when(commentBridgeService).getPageOfCommentsForUser(USER_ID, PAGE_NUMBER, ITEM_LIMIT_ON_PAGE, ORDER_BY, ORDER_DIRECTION);

        // when
        Assertions.assertThrows(UserSessionInvalidationRequiredException.class, () ->adapter.getContent(CONTENT_REQUEST));

        // then
        // exception expected
    }

    @Test
    public void shouldGetContentReturnWithFailureInCaseOfBridgeFailure() throws CommunicationFailureException {

        // given
        doThrow(CommunicationFailureException.class).when(commentBridgeService).getPageOfCommentsForUser(USER_ID, PAGE_NUMBER, ITEM_LIMIT_ON_PAGE, ORDER_BY, ORDER_DIRECTION);

        // when
        Optional<WrapperBodyDataModel<ExtendedCommentListDataModel>> result = adapter.getContent(CONTENT_REQUEST);

        // then
        assertThat(result.isPresent(), is(false));
        verify(commentBridgeService).getPageOfCommentsForUser(USER_ID, PAGE_NUMBER, ITEM_LIMIT_ON_PAGE, ORDER_BY, ORDER_DIRECTION);
    }

    @Test
    public void shouldGetIdentifierReturnProperValue() {

        // when
        ContentRequestAdapterIdentifier result = adapter.getIdentifier();

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(ContentRequestAdapterIdentifier.COMMENTS_OF_USER));
    }
}
