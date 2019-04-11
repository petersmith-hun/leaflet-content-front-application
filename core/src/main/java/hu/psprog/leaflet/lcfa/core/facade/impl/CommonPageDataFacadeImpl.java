package hu.psprog.leaflet.lcfa.core.facade.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.api.rest.response.sitemap.Sitemap;
import hu.psprog.leaflet.lcfa.core.config.PageConfigModel;
import hu.psprog.leaflet.lcfa.core.converter.CommonPageDataConverter;
import hu.psprog.leaflet.lcfa.core.domain.common.CommonPageData;
import hu.psprog.leaflet.lcfa.core.domain.content.request.OrderBy;
import hu.psprog.leaflet.lcfa.core.domain.content.request.OrderDirection;
import hu.psprog.leaflet.lcfa.core.domain.content.request.PaginatedContentRequest;
import hu.psprog.leaflet.lcfa.core.exception.ContentRetrievalException;
import hu.psprog.leaflet.lcfa.core.facade.CommonPageDataFacade;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterRegistry;
import hu.psprog.leaflet.lcfa.core.facade.cache.CommonPageDataCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

import static hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier.COMMON_PAGE_DATA;
import static hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier.SITEMAP;

/**
 * Implementation of {@link CommonPageDataFacade}.
 *
 * @author Peter Smith
 */
@Service
public class CommonPageDataFacadeImpl implements CommonPageDataFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonPageDataFacadeImpl.class);

    private static final int PAGE_NUMBER = 1;
    private static final OrderBy.Entry ORDER_BY = OrderBy.Entry.CREATED;
    private static final OrderDirection ORDER_DIRECTION = OrderDirection.DESC;
    private static final Sitemap EMPTY_SITEMAP = Sitemap.getBuilder().build();

    private ContentRequestAdapterRegistry contentRequestAdapterRegistry;
    private CommonPageDataCache commonPageDataCache;
    private CommonPageDataConverter commonPageDataConverter;

    private final PaginatedContentRequest latestEntries;

    @Autowired
    public CommonPageDataFacadeImpl(ContentRequestAdapterRegistry contentRequestAdapterRegistry, CommonPageDataCache commonPageDataCache,
                                    CommonPageDataConverter commonPageDataConverter, PageConfigModel pageConfigModel) {
        this.contentRequestAdapterRegistry = contentRequestAdapterRegistry;
        this.commonPageDataCache = commonPageDataCache;
        this.commonPageDataConverter = commonPageDataConverter;
        this.latestEntries = initLatestEntriesRequest(pageConfigModel.getCommonPageDataCache().getLatestEntriesCount());
    }

    @Override
    public CommonPageData getCommonPageData() {
        return commonPageDataCache.getCached()
                .orElseGet(requestCommonPageData());
    }

    @Override
    public Sitemap getSitemap() {
        return contentRequestAdapterRegistry.<Sitemap, Void>getContentRequestAdapter(SITEMAP)
                .getContent(null)
                .orElse(EMPTY_SITEMAP);
    }

    private PaginatedContentRequest initLatestEntriesRequest(int numberOfLatestEntries) {
        return PaginatedContentRequest.builder()
                .page(PAGE_NUMBER)
                .limit(numberOfLatestEntries)
                .entryOrderBy(ORDER_BY)
                .entryOrderDirection(ORDER_DIRECTION)
                .build();
    }

    private Supplier<CommonPageData> requestCommonPageData() {
        return () -> {
            LOGGER.info("Updating common page data...");
            CommonPageData commonPageData = contentRequestAdapterRegistry.<WrapperBodyDataModel<EntryListDataModel>, PaginatedContentRequest>getContentRequestAdapter(COMMON_PAGE_DATA)
                    .getContent(latestEntries)
                    .map(commonPageDataConverter::convert)
                    .orElseThrow(() -> new ContentRetrievalException("Failed to retrieve common page data."));
            commonPageDataCache.update(commonPageData);

            return commonPageData;
        };
    }
}
