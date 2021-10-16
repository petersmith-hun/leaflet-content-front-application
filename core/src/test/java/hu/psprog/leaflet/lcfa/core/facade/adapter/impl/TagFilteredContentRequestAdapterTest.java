package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.bridge.client.domain.OrderBy;
import hu.psprog.leaflet.bridge.client.domain.OrderDirection;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.EntryBridgeService;
import hu.psprog.leaflet.lcfa.core.domain.content.request.FilteredPaginationContentRequest;
import hu.psprog.leaflet.lcfa.core.domain.raw.HomePageRawResponseWrapper;
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
 * Unit tests for {@link TagFilteredContentRequestAdapter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class TagFilteredContentRequestAdapterTest extends AbstractParallalContentRequestAdapterBaseTest {

    private static final WrapperBodyDataModel<EntryListDataModel> WRAPPED_ENTRY_LIST_DATA_MODEL = WrapperBodyDataModel.getBuilder()
            .withBody(EntryListDataModel.getBuilder().build())
            .build();
    private static final long TAG_ID = 1L;
    private static final int PAGE_NUMBER = 1;
    private static final int ITEM_LIMIT_ON_PAGE = 10;
    private static final OrderBy.Entry ORDER_BY = OrderBy.Entry.TITLE;
    private static final OrderDirection ORDER_DIRECTION = OrderDirection.DESC;
    private static final FilteredPaginationContentRequest<Long, hu.psprog.leaflet.lcfa.core.domain.content.request.OrderBy.Entry> CONTENT_REQUEST =
            FilteredPaginationContentRequest.<Long, hu.psprog.leaflet.lcfa.core.domain.content.request.OrderBy.Entry>builder()
                    .filterValue(TAG_ID)
                    .page(PAGE_NUMBER)
                    .limit(ITEM_LIMIT_ON_PAGE)
                    .orderBy(hu.psprog.leaflet.lcfa.core.domain.content.request.OrderBy.Entry.TITLE)
                    .orderDirection(hu.psprog.leaflet.lcfa.core.domain.content.request.OrderDirection.DESC)
                    .build();

    @Mock
    private EntryBridgeService entryBridgeService;

    @InjectMocks
    private TagFilteredContentRequestAdapter adapter;

    @Test
    public void shouldGetContentReturnWithSuccess() throws CommunicationFailureException {

        // given
        given(entryBridgeService.getPageOfPublicEntriesByTag(TAG_ID, PAGE_NUMBER, ITEM_LIMIT_ON_PAGE, ORDER_BY, ORDER_DIRECTION))
                .willReturn(WRAPPED_ENTRY_LIST_DATA_MODEL);
        givenSuccessfulFilteringDataRetrieval();

        // when
        Optional<HomePageRawResponseWrapper> result = adapter.getContent(CONTENT_REQUEST);

        // then
        assertThat(result.isPresent(), is(true));
        assertThat(result.get().getWrappedEntryListDataModel(), equalTo(WRAPPED_ENTRY_LIST_DATA_MODEL));
        assertThat(result.get().getCategoryListDataModel(), equalTo(CATEGORY_LIST_DATA_MODEL));
        assertThat(result.get().getWrappedTagListDataModel(), equalTo(WRAPPED_TAG_LIST_DATA_MODEL));
    }

    @Test
    public void shouldGetContentReturnWithoutFilteringData() throws CommunicationFailureException {

        // given
        given(entryBridgeService.getPageOfPublicEntriesByTag(TAG_ID, PAGE_NUMBER, ITEM_LIMIT_ON_PAGE, ORDER_BY, ORDER_DIRECTION))
                .willReturn(WRAPPED_ENTRY_LIST_DATA_MODEL);
        givenFailedFilteringDataRetrieval();

        // when
        Optional<HomePageRawResponseWrapper> result = adapter.getContent(CONTENT_REQUEST);

        // then
        assertThat(result.isPresent(), is(true));
        assertThat(result.get().getWrappedEntryListDataModel(), equalTo(WRAPPED_ENTRY_LIST_DATA_MODEL));
        assertThat(result.get().getCategoryListDataModel(), nullValue());
        assertThat(result.get().getWrappedTagListDataModel(), nullValue());
    }

    @Test
    public void shouldGetContentReturnWithEmptyResponse() throws CommunicationFailureException {

        // given
        doThrow(CommunicationFailureException.class).when(entryBridgeService)
                .getPageOfPublicEntriesByTag(TAG_ID, PAGE_NUMBER, ITEM_LIMIT_ON_PAGE, ORDER_BY, ORDER_DIRECTION);
        givenFailedFilteringDataRetrieval();

        // when
        Optional<HomePageRawResponseWrapper> result = adapter.getContent(CONTENT_REQUEST);

        // then
        assertThat(result.isPresent(), is(true));
        assertThat(result.get().getWrappedEntryListDataModel(), nullValue());
        assertThat(result.get().getCategoryListDataModel(), nullValue());
        assertThat(result.get().getWrappedTagListDataModel(), nullValue());
    }

    @Test
    public void shouldGetIdentifierReturnProperValue() {

        // when
        ContentRequestAdapterIdentifier result = adapter.getIdentifier();

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(ContentRequestAdapterIdentifier.TAG_FILTER));
    }

    @Override
    ContentRequestAdapter<?, ?> adapterUnderTest() {
        return adapter;
    }
}
