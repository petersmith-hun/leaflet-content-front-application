package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagListDataModel;
import hu.psprog.leaflet.bridge.client.domain.OrderBy;
import hu.psprog.leaflet.bridge.client.domain.OrderDirection;
import hu.psprog.leaflet.bridge.service.CategoryBridgeService;
import hu.psprog.leaflet.bridge.service.EntryBridgeService;
import hu.psprog.leaflet.bridge.service.TagBridgeService;
import hu.psprog.leaflet.lcfa.core.domain.CallType;
import hu.psprog.leaflet.lcfa.core.domain.content.request.PaginatedContentRequest;
import hu.psprog.leaflet.lcfa.core.domain.raw.HomePageRawResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * {@link AbstractParallelContentRequestAdapter} implementation returning all the necessary information for home page rendering.
 * Requests public lists of categories, public list of tags and public list of entries in parallel.
 * Combinator implementation returns the responses as {@link HomePageRawResponseWrapper} for further processing.
 *
 * @author Peter Smith
 */
@Component
public class HomePageContentRequestAdapter extends AbstractParallelContentRequestAdapter<HomePageRawResponseWrapper, PaginatedContentRequest> {

    private CategoryBridgeService categoryBridgeService;
    private EntryBridgeService entryBridgeService;
    private TagBridgeService tagBridgeService;

    @Autowired
    public HomePageContentRequestAdapter(CategoryBridgeService categoryBridgeService, EntryBridgeService entryBridgeService, TagBridgeService tagBridgeService) {
        this.categoryBridgeService = categoryBridgeService;
        this.entryBridgeService = entryBridgeService;
        this.tagBridgeService = tagBridgeService;
    }

    @Override
    Map<CallType, Callable<BaseBodyDataModel>> callers(PaginatedContentRequest contentRequestParameter) {

        Map<CallType, Callable<BaseBodyDataModel>> callableMap = new HashMap<>();
        callableMap.put(CallType.CATEGORY, categoryBridgeService::getPublicCategories);
        callableMap.put(CallType.ENTRY, getPublicEntries(contentRequestParameter));
        callableMap.put(CallType.TAG, tagBridgeService::getAllPublicTags);

        return callableMap;
    }

    @Override
    HomePageRawResponseWrapper combinator(Map<CallType, BaseBodyDataModel> result) {
        return HomePageRawResponseWrapper.builder()
                .categoryListDataModel((CategoryListDataModel) result.get(CallType.CATEGORY))
                .wrappedEntryListDataModel((WrapperBodyDataModel<EntryListDataModel>) result.get(CallType.ENTRY))
                .wrappedTagListDataModel((WrapperBodyDataModel<TagListDataModel>) result.get(CallType.TAG))
                .build();
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
