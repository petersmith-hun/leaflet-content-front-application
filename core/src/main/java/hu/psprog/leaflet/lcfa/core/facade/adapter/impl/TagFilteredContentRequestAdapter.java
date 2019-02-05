package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.bridge.service.EntryBridgeService;
import hu.psprog.leaflet.lcfa.core.domain.CallType;
import hu.psprog.leaflet.lcfa.core.domain.content.request.FilteredPaginationContentRequest;
import hu.psprog.leaflet.lcfa.core.domain.raw.HomePageRawResponseWrapper;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * {@link AbstractFilteredEntryPageContentRequestAdapter} implementation returning all the necessary information for tag filter page.
 * Requests public list of categories, public list of tags and public list of entries under given tag in parallel.
 * Combinator implementation returns the responses as {@link HomePageRawResponseWrapper} for further processing.
 *
 * @author Peter Smith
 */
@Component
public class TagFilteredContentRequestAdapter extends AbstractFilteredEntryPageContentRequestAdapter<FilteredPaginationContentRequest<Long>> {

    private EntryBridgeService entryBridgeService;

    @Autowired
    public TagFilteredContentRequestAdapter(EntryBridgeService entryBridgeService) {
        this.entryBridgeService = entryBridgeService;
    }

    @Override
    void addContentCalls(Map<CallType, Callable<BaseBodyDataModel>> callableMap, FilteredPaginationContentRequest<Long> contentRequestParameter) {
        callableMap.put(CallType.ENTRY, getPublicEntriesByTag(contentRequestParameter));
    }

    @Override
    public ContentRequestAdapterIdentifier getIdentifier() {
        return ContentRequestAdapterIdentifier.TAG_FILTER;
    }

    private Callable<BaseBodyDataModel> getPublicEntriesByTag(FilteredPaginationContentRequest<Long> contentRequestParameter) {
        return () -> entryBridgeService.getPageOfPublicEntriesByTag(contentRequestParameter.getFilterValue(), contentRequestParameter.getPage(),
                contentRequestParameter.getLimit(), mapOrdering(contentRequestParameter), mapOrderDirection(contentRequestParameter));
    }
}
