package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagListDataModel;
import hu.psprog.leaflet.bridge.service.CategoryBridgeService;
import hu.psprog.leaflet.bridge.service.EntryBridgeService;
import hu.psprog.leaflet.bridge.service.TagBridgeService;
import hu.psprog.leaflet.lcfa.core.domain.CallType;
import hu.psprog.leaflet.lcfa.core.domain.raw.ArticlePageRawResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * {@link AbstractParallelContentRequestAdapter} implementation returning all the necessary information for article page rendering.
 * Requests public lists of categories, public list of tags and the requested article (by its identifier link).
 * Combinator implementation returns the responses as {@link ArticlePageRawResponseWrapper} for further processing.
 *
 * @author Peter Smith
 */
@Component
public class ArticleContentRequestAdapter extends AbstractParallelContentRequestAdapter<ArticlePageRawResponseWrapper, String> {

    private CategoryBridgeService categoryBridgeService;
    private EntryBridgeService entryBridgeService;
    private TagBridgeService tagBridgeService;

    @Autowired
    public ArticleContentRequestAdapter(CategoryBridgeService categoryBridgeService, EntryBridgeService entryBridgeService, TagBridgeService tagBridgeService) {
        this.categoryBridgeService = categoryBridgeService;
        this.entryBridgeService = entryBridgeService;
        this.tagBridgeService = tagBridgeService;
    }

    @Override
    Map<CallType, Callable<BaseBodyDataModel>> callers(String contentRequestParameter) {

        Map<CallType, Callable<BaseBodyDataModel>> callableMap = new HashMap<>();
        callableMap.put(CallType.CATEGORY, categoryBridgeService::getPublicCategories);
        callableMap.put(CallType.ENTRY, () -> entryBridgeService.getEntryByLink(contentRequestParameter));
        callableMap.put(CallType.TAG, tagBridgeService::getAllPublicTags);

        return callableMap;
    }

    @Override
    ArticlePageRawResponseWrapper combinator(Map<CallType, BaseBodyDataModel> result) {
        return ArticlePageRawResponseWrapper.builder()
                .categoryListDataModel((CategoryListDataModel) result.get(CallType.CATEGORY))
                .wrappedExtendedEntryDataModel((WrapperBodyDataModel<ExtendedEntryDataModel>) result.get(CallType.ENTRY))
                .wrappedTagListDataModel((WrapperBodyDataModel<TagListDataModel>) result.get(CallType.TAG))
                .build();
    }
}
