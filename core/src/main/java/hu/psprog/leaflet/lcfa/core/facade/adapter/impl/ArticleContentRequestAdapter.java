package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagListDataModel;
import hu.psprog.leaflet.bridge.service.EntryBridgeService;
import hu.psprog.leaflet.lcfa.core.domain.CallType;
import hu.psprog.leaflet.lcfa.core.domain.raw.ArticlePageRawResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * {@link AbstractFilteringSupportParallelContentRequestAdapter} implementation returning all the necessary information for article page rendering.
 * Requests public lists of categories, public list of tags and the requested article (by its identifier link).
 * Combinator implementation returns the responses as {@link ArticlePageRawResponseWrapper} for further processing.
 *
 * @author Peter Smith
 */
@Component
public class ArticleContentRequestAdapter extends AbstractFilteringSupportParallelContentRequestAdapter<ArticlePageRawResponseWrapper, String> {

    private EntryBridgeService entryBridgeService;

    @Autowired
    public ArticleContentRequestAdapter(EntryBridgeService entryBridgeService) {
        this.entryBridgeService = entryBridgeService;
    }

    @Override
    void addContentCalls(Map<CallType, Callable<BaseBodyDataModel>> callableMap, String contentRequestParameter) {
        callableMap.put(CallType.ENTRY, () -> entryBridgeService.getEntryByLink(contentRequestParameter));
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
