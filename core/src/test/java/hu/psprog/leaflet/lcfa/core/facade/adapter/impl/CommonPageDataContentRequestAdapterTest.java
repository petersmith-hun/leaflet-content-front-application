package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.bridge.client.domain.OrderBy;
import hu.psprog.leaflet.bridge.client.domain.OrderDirection;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.EntryBridgeService;
import hu.psprog.leaflet.lcfa.core.domain.content.request.PaginatedContentRequest;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

/**
 * Unit tests for {@link CommonPageDataContentRequestAdapter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class CommonPageDataContentRequestAdapterTest {

    private static final WrapperBodyDataModel<EntryListDataModel> WRAPPED_ENTRY_LIST_DATA_MODEL = WrapperBodyDataModel.<EntryListDataModel>getBuilder()
            .withBody(EntryListDataModel.getBuilder().build())
            .build();
    private static final int PAGE_NUMBER = 1;
    private static final int ITEM_LIMIT_ON_PAGE = 10;
    private static final OrderBy.Entry ORDER_BY = OrderBy.Entry.TITLE;
    private static final OrderDirection ORDER_DIRECTION = OrderDirection.DESC;
    private static final PaginatedContentRequest CONTENT_REQUEST = PaginatedContentRequest.builder()
            .page(PAGE_NUMBER)
            .limit(ITEM_LIMIT_ON_PAGE)
            .entryOrderBy(hu.psprog.leaflet.lcfa.core.domain.content.request.OrderBy.Entry.TITLE)
            .entryOrderDirection(hu.psprog.leaflet.lcfa.core.domain.content.request.OrderDirection.DESC)
            .build();

    @Mock
    private EntryBridgeService entryBridgeService;

    @InjectMocks
    private CommonPageDataContentRequestAdapter adapter;

    @Test
    public void shouldGetContentReturnWithSuccess() throws CommunicationFailureException {

        // given
        given(entryBridgeService.getPageOfPublicEntries(PAGE_NUMBER, ITEM_LIMIT_ON_PAGE, ORDER_BY, ORDER_DIRECTION)).willReturn(WRAPPED_ENTRY_LIST_DATA_MODEL);

        // when
        Optional<WrapperBodyDataModel<EntryListDataModel>> result = adapter.getContent(CONTENT_REQUEST);

        // then
        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), equalTo(WRAPPED_ENTRY_LIST_DATA_MODEL));
    }

    @Test
    public void shouldGetContentReturnWithEmptyResponseInCaseOfBridgeFailure() throws CommunicationFailureException {

        // given
        doThrow(CommunicationFailureException.class).when(entryBridgeService).getPageOfPublicEntries(PAGE_NUMBER, ITEM_LIMIT_ON_PAGE, ORDER_BY, ORDER_DIRECTION);

        // when
        Optional<WrapperBodyDataModel<EntryListDataModel>> result = adapter.getContent(CONTENT_REQUEST);

        // then
        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void shouldGetIdentifierReturnProperValue() {

        // when
        ContentRequestAdapterIdentifier result = adapter.getIdentifier();

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(ContentRequestAdapterIdentifier.COMMON_PAGE_DATA));
    }
}
