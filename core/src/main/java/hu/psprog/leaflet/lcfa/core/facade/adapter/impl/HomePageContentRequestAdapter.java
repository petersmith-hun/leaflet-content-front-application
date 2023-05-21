package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.bridge.client.domain.OrderBy;
import hu.psprog.leaflet.bridge.client.domain.OrderDirection;
import hu.psprog.leaflet.bridge.service.CategoryBridgeService;
import hu.psprog.leaflet.bridge.service.EntryBridgeService;
import hu.psprog.leaflet.bridge.service.TagBridgeService;
import hu.psprog.leaflet.lcfa.core.domain.CallType;
import hu.psprog.leaflet.lcfa.core.domain.content.request.PaginatedContentRequest;
import hu.psprog.leaflet.lcfa.core.domain.raw.HomePageRawResponseWrapper;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * {@link AbstractFilteringSupportParallelContentRequestAdapter} implementation returning all the necessary information for home page rendering.
 * Requests public lists of categories, public list of tags and public list of entries in parallel.
 * Combinator implementation returns the responses as {@link HomePageRawResponseWrapper} for further processing.
 *
 * @author Peter Smith
 */
@Component
public class HomePageContentRequestAdapter extends AbstractFilteredEntryPageContentRequestAdapter<PaginatedContentRequest> {

    private final EntryBridgeService entryBridgeService;

    @Autowired
    public HomePageContentRequestAdapter(@Qualifier("contentAdapterAsyncTaskExecutor") AsyncTaskExecutor contentAdapterExecutor,
                                         CategoryBridgeService categoryBridgeService,
                                         TagBridgeService tagBridgeService,
                                         EntryBridgeService entryBridgeService) {
        super(contentAdapterExecutor, categoryBridgeService, tagBridgeService);
        this.entryBridgeService = entryBridgeService;
    }

    @Override
    void addContentCalls(Map<CallType, Callable<BaseBodyDataModel>> callableMap, PaginatedContentRequest contentRequestParameter) {
        callableMap.put(CallType.ENTRY, getPublicEntries(contentRequestParameter));
    }

    @Override
    public ContentRequestAdapterIdentifier getIdentifier() {
        return ContentRequestAdapterIdentifier.HOME_PAGE;
    }

    private Callable<BaseBodyDataModel> getPublicEntries(PaginatedContentRequest contentRequestParameter) {
        return () -> entryBridgeService.getPageOfPublicEntries(contentRequestParameter.getPage(), contentRequestParameter.getLimit(),
                mapOrdering(contentRequestParameter), mapOrderDirection(contentRequestParameter));
    }

    private OrderBy.Entry mapOrdering(PaginatedContentRequest paginatedContentRequest) {
        return OrderBy.Entry.valueOf(paginatedContentRequest.getEntryOrderBy().name());
    }

    private OrderDirection mapOrderDirection(PaginatedContentRequest paginatedContentRequest) {
        return OrderDirection.valueOf(paginatedContentRequest.getEntryOrderDirection().name());
    }
}
