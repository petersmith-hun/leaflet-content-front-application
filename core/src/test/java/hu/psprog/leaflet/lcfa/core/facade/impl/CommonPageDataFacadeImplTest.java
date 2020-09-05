package hu.psprog.leaflet.lcfa.core.facade.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.api.rest.response.sitemap.Sitemap;
import hu.psprog.leaflet.lcfa.core.config.CommonPageDataCacheConfigModel;
import hu.psprog.leaflet.lcfa.core.config.DefaultPaginationAttributes;
import hu.psprog.leaflet.lcfa.core.config.PageConfigModel;
import hu.psprog.leaflet.lcfa.core.converter.CommonPageDataConverter;
import hu.psprog.leaflet.lcfa.core.domain.common.CommonPageData;
import hu.psprog.leaflet.lcfa.core.domain.content.EntrySummary;
import hu.psprog.leaflet.lcfa.core.domain.content.request.OrderBy;
import hu.psprog.leaflet.lcfa.core.domain.content.request.OrderDirection;
import hu.psprog.leaflet.lcfa.core.domain.content.request.PaginatedContentRequest;
import hu.psprog.leaflet.lcfa.core.exception.ContentRetrievalException;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterRegistry;
import hu.psprog.leaflet.lcfa.core.facade.cache.CommonPageDataCache;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Unit tests for {@link CommonPageDataFacadeImpl}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class CommonPageDataFacadeImplTest {

    private static final int LATEST_ENTRIES_COUNT = 5;
    private static final int PAGE_NUMBER = 1;
    private static final OrderBy.Entry ORDER_BY = OrderBy.Entry.PUBLISHED;
    private static final OrderDirection ORDER_DIRECTION = OrderDirection.DESC;
    private static final PageConfigModel PAGE_CONFIG_MODEL = new PageConfigModel();
    private static final CommonPageDataCacheConfigModel COMMON_PAGE_DATA_CACHE_CONFIG_MODEL = new CommonPageDataCacheConfigModel();
    private static final CommonPageData COMMON_PAGE_DATA = CommonPageData.builder()
            .latestEntries(Collections.singletonList(EntrySummary.builder().title("entry").build()))
            .build();
    private static final PaginatedContentRequest PAGINATED_CONTENT_REQUEST = PaginatedContentRequest.builder()
            .page(PAGE_NUMBER)
            .limit(LATEST_ENTRIES_COUNT)
            .entryOrderBy(ORDER_BY)
            .entryOrderDirection(ORDER_DIRECTION)
            .build();
    private static final WrapperBodyDataModel<EntryListDataModel> WRAPPED_ENTRY_LIST_DATA_MODEL = WrapperBodyDataModel.getBuilder()
            .withBody(EntryListDataModel.getBuilder().withItem(EntryDataModel.getBuilder().withId(3L).build()).build())
            .build();
    private static final Sitemap SITEMAP = Sitemap.getBuilder()
            .withLocation("/location/test")
            .build();
    private static final Sitemap EMPTY_SITEMAP = Sitemap.getBuilder().build();

    static {
        COMMON_PAGE_DATA_CACHE_CONFIG_MODEL.setLatestEntriesCount(LATEST_ENTRIES_COUNT);
        PAGE_CONFIG_MODEL.setCommonPageDataCache(COMMON_PAGE_DATA_CACHE_CONFIG_MODEL);
    }

    @Mock
    private ContentRequestAdapterRegistry contentRequestAdapterRegistry;

    @Mock
    private CommonPageDataCache commonPageDataCache;

    @Mock
    private CommonPageDataConverter commonPageDataConverter;

    @Mock
    private DefaultPaginationAttributes<OrderBy.Entry> defaultPaginationAttributes;

    @Mock
    private ContentRequestAdapter<WrapperBodyDataModel<EntryListDataModel>, PaginatedContentRequest> commonPageDataContentRequestAdapter;

    @Mock
    private ContentRequestAdapter<Sitemap, Void> sitemapContentRequestAdapter;

    private CommonPageDataFacadeImpl commonPageDataFacade;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        given(defaultPaginationAttributes.getOrderBy()).willReturn(ORDER_BY);
        given(defaultPaginationAttributes.getOrderDirection()).willReturn(ORDER_DIRECTION);

        commonPageDataFacade = new CommonPageDataFacadeImpl(contentRequestAdapterRegistry, commonPageDataCache,
                commonPageDataConverter, defaultPaginationAttributes, PAGE_CONFIG_MODEL);
    }

    @Test
    public void shouldGetCommonPageDataFromCache() {

        // given
        given(commonPageDataCache.getCached()).willReturn(Optional.of(COMMON_PAGE_DATA));

        // when
        CommonPageData result = commonPageDataFacade.getCommonPageData();

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(COMMON_PAGE_DATA));
        verify(commonPageDataCache, never()).update(any(CommonPageData.class));
        verifyZeroInteractions(contentRequestAdapterRegistry, commonPageDataContentRequestAdapter, commonPageDataConverter);
    }

    @Test
    public void shouldGetCommonPageDataFromBackend() {

        // given
        given(commonPageDataCache.getCached()).willReturn(Optional.empty());
        given(contentRequestAdapterRegistry.<WrapperBodyDataModel<EntryListDataModel>, PaginatedContentRequest>getContentRequestAdapter(ContentRequestAdapterIdentifier.COMMON_PAGE_DATA))
                .willReturn(commonPageDataContentRequestAdapter);
        given(commonPageDataContentRequestAdapter.getContent(PAGINATED_CONTENT_REQUEST)).willReturn(Optional.of(WRAPPED_ENTRY_LIST_DATA_MODEL));
        given(commonPageDataConverter.convert(WRAPPED_ENTRY_LIST_DATA_MODEL)).willReturn(COMMON_PAGE_DATA);

        // when
        CommonPageData result = commonPageDataFacade.getCommonPageData();

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(COMMON_PAGE_DATA));
        verify(commonPageDataCache).update(COMMON_PAGE_DATA);
    }

    @Test(expected = ContentRetrievalException.class)
    public void shouldGetCommonPageDataThrowContentRetrievalExceptionForMissingData() {

        // given
        given(commonPageDataCache.getCached()).willReturn(Optional.empty());
        given(contentRequestAdapterRegistry.<WrapperBodyDataModel<EntryListDataModel>, PaginatedContentRequest>getContentRequestAdapter(ContentRequestAdapterIdentifier.COMMON_PAGE_DATA))
                .willReturn(commonPageDataContentRequestAdapter);
        given(commonPageDataContentRequestAdapter.getContent(PAGINATED_CONTENT_REQUEST)).willReturn(Optional.empty());

        // when
        commonPageDataFacade.getCommonPageData();

        // then
        // exception expected
    }

    @Test
    public void shouldGetSitemapReturnWithSuccess() {

        // given
        given(contentRequestAdapterRegistry.<Sitemap, Void>getContentRequestAdapter(ContentRequestAdapterIdentifier.SITEMAP))
                .willReturn(sitemapContentRequestAdapter);
        given(sitemapContentRequestAdapter.getContent(nullable(Void.class))).willReturn(Optional.of(SITEMAP));

        // when
        Sitemap result = commonPageDataFacade.getSitemap();

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(SITEMAP));
    }

    @Test
    public void shouldGetSitemapReturnEmptySitemapForMissingData() {

        // given
        given(contentRequestAdapterRegistry.<Sitemap, Void>getContentRequestAdapter(ContentRequestAdapterIdentifier.SITEMAP))
                .willReturn(sitemapContentRequestAdapter);
        given(sitemapContentRequestAdapter.getContent(nullable(Void.class))).willReturn(Optional.empty());

        // when
        Sitemap result = commonPageDataFacade.getSitemap();

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(EMPTY_SITEMAP));
    }
}
