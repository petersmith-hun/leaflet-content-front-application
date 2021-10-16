package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.response.comment.CommentListDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.bridge.client.domain.OrderBy;
import hu.psprog.leaflet.bridge.client.domain.OrderDirection;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.CommentBridgeService;
import hu.psprog.leaflet.bridge.service.EntryBridgeService;
import hu.psprog.leaflet.lcfa.core.domain.raw.ArticlePageRawResponseWrapper;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

/**
 * Unit tests for {@link ArticleContentRequestAdapter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class ArticleContentRequestAdapterTest extends AbstractParallalContentRequestAdapterBaseTest {

    private static final int COMMENT_PAGE = 1;
    private static final int COMMENT_LIMIT = 1000;
    private static final OrderBy.Comment COMMENT_ORDER_BY = OrderBy.Comment.CREATED;
    private static final OrderDirection COMMENT_ORDER_DIRECTION = OrderDirection.DESC;
    private static final String ENTRY_LINK = "entry-link";
    private static final WrapperBodyDataModel<ExtendedEntryDataModel> WRAPPED_ENTRY_DATA_MODEL = WrapperBodyDataModel.getBuilder()
            .withBody(ExtendedEntryDataModel.getExtendedBuilder().withLink(ENTRY_LINK).build())
            .build();
    private static final WrapperBodyDataModel<CommentListDataModel> WRAPPED_COMMENT_LIST_DATA_MODEL = WrapperBodyDataModel.getBuilder()
            .withBody(CommentListDataModel.getBuilder().build())
            .build();

    @Mock
    private EntryBridgeService entryBridgeService;

    @Mock
    private CommentBridgeService commentBridgeService;

    @InjectMocks
    private ArticleContentRequestAdapter adapter;

    @Test
    public void shouldGetContentReturnWithSuccess() throws CommunicationFailureException {

        // given
        given(entryBridgeService.getEntryByLink(ENTRY_LINK)).willReturn(WRAPPED_ENTRY_DATA_MODEL);
        given(commentBridgeService.getPageOfPublicCommentsForEntry(ENTRY_LINK, COMMENT_PAGE, COMMENT_LIMIT, COMMENT_ORDER_BY, COMMENT_ORDER_DIRECTION))
                .willReturn(WRAPPED_COMMENT_LIST_DATA_MODEL);
        givenSuccessfulFilteringDataRetrieval();

        // when
        Optional<ArticlePageRawResponseWrapper> result = adapter.getContent(ENTRY_LINK);

        // when
        assertThat(result.isPresent(), is(true));
        assertThat(result.get().getCategoryListDataModel(), equalTo(CATEGORY_LIST_DATA_MODEL));
        assertThat(result.get().getWrappedTagListDataModel(), equalTo(WRAPPED_TAG_LIST_DATA_MODEL));
        assertThat(result.get().getWrappedCommentListDataModel(), equalTo(WRAPPED_COMMENT_LIST_DATA_MODEL));
        assertThat(result.get().getWrappedExtendedEntryDataModel(), equalTo(WRAPPED_ENTRY_DATA_MODEL));
    }

    @Test
    public void shouldGetContentReturnWithoutCommentsAndFilteringData() throws CommunicationFailureException {

        // given
        given(entryBridgeService.getEntryByLink(ENTRY_LINK)).willReturn(WRAPPED_ENTRY_DATA_MODEL);
        doThrow(CommunicationFailureException.class).when(commentBridgeService)
                .getPageOfPublicCommentsForEntry(ENTRY_LINK, COMMENT_PAGE, COMMENT_LIMIT, COMMENT_ORDER_BY, COMMENT_ORDER_DIRECTION);
        givenFailedFilteringDataRetrieval();

        // when
        Optional<ArticlePageRawResponseWrapper> result = adapter.getContent(ENTRY_LINK);

        // when
        assertThat(result.isPresent(), is(true));
        assertThat(result.get().getWrappedCommentListDataModel(), nullValue());
        assertThat(result.get().getCategoryListDataModel(), nullValue());
        assertThat(result.get().getWrappedTagListDataModel(), nullValue());
        assertThat(result.get().getWrappedExtendedEntryDataModel(), equalTo(WRAPPED_ENTRY_DATA_MODEL));
    }

    @Test
    public void shouldGetContentReturnWithEmptyResponseIfArticleCallFails() throws CommunicationFailureException {

        // given
        doThrow(CommunicationFailureException.class).when(entryBridgeService).getEntryByLink(ENTRY_LINK);
        doThrow(CommunicationFailureException.class).when(commentBridgeService)
                .getPageOfPublicCommentsForEntry(ENTRY_LINK, COMMENT_PAGE, COMMENT_LIMIT, COMMENT_ORDER_BY, COMMENT_ORDER_DIRECTION);
        givenFailedFilteringDataRetrieval();

        // when
        Optional<ArticlePageRawResponseWrapper> result = adapter.getContent(ENTRY_LINK);

        // when
        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void shouldGetContentReturnWithEmptyResponseInCaseOfAnyOtherException() {

        // given
        setMock("contentAdapterExecutor", null);

        // when
        Optional<ArticlePageRawResponseWrapper> result = adapter.getContent(ENTRY_LINK);

        // when
        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void shouldGetIdentifierReturnProperValue() {

        // when
        ContentRequestAdapterIdentifier result = adapter.getIdentifier();

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(ContentRequestAdapterIdentifier.ARTICLE));
    }

    @Override
    ContentRequestAdapter<?, ?> adapterUnderTest() {
        return adapter;
    }
}
