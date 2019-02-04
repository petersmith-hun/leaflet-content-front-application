package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagListDataModel;
import hu.psprog.leaflet.bridge.client.domain.OrderBy;
import hu.psprog.leaflet.bridge.client.domain.OrderDirection;
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
 * {@link AbstractFilteringSupportParallelContentRequestAdapter} implementation returning all the necessary information for category filter page.
 * Requests public lists of categories, public list of tags and public list of entries under given category in parallel.
 * Combinator implementation returns the responses as {@link HomePageRawResponseWrapper} for further processing.
 *
 * @author Peter Smith
 */
@Component
public class CategoryFilteredContentRequestAdapter
        extends AbstractFilteringSupportParallelContentRequestAdapter<HomePageRawResponseWrapper, FilteredPaginationContentRequest<Long>> {

    private EntryBridgeService entryBridgeService;

    @Autowired
    public CategoryFilteredContentRequestAdapter(EntryBridgeService entryBridgeService) {
        this.entryBridgeService = entryBridgeService;
    }

    @Override
    void addContentCalls(Map<CallType, Callable<BaseBodyDataModel>> callableMap, FilteredPaginationContentRequest<Long> contentRequestParameter) {
        callableMap.put(CallType.ENTRY, getPublicEntriesByCategory(contentRequestParameter));
    }

    @Override
    HomePageRawResponseWrapper combinator(Map<CallType, BaseBodyDataModel> result) {
        return HomePageRawResponseWrapper.builder()
                .categoryListDataModel((CategoryListDataModel) result.get(CallType.CATEGORY))
                .wrappedEntryListDataModel((WrapperBodyDataModel<EntryListDataModel>) result.get(CallType.ENTRY))
                .wrappedTagListDataModel((WrapperBodyDataModel<TagListDataModel>) result.get(CallType.TAG))
                .build();
    }

    @Override
    public ContentRequestAdapterIdentifier getIdentifier() {
        return ContentRequestAdapterIdentifier.CATEGORY_FILTER;
    }

    private Callable<BaseBodyDataModel> getPublicEntriesByCategory(FilteredPaginationContentRequest<Long> contentRequestParameter) {
        return () -> entryBridgeService.getPageOfPublicEntriesByCategory(contentRequestParameter.getFilterValue(), contentRequestParameter.getPage(),
                contentRequestParameter.getLimit(), mapOrdering(contentRequestParameter), mapOrderDirection(contentRequestParameter));
    }

    private OrderBy.Entry mapOrdering(FilteredPaginationContentRequest<Long> paginatedContentRequest) {
        return OrderBy.Entry.valueOf(paginatedContentRequest.getEntryOrderBy().name());
    }

    private OrderDirection mapOrderDirection(FilteredPaginationContentRequest<Long> paginatedContentRequest) {
        return OrderDirection.valueOf(paginatedContentRequest.getEntryOrderDirection().name());
    }
}
