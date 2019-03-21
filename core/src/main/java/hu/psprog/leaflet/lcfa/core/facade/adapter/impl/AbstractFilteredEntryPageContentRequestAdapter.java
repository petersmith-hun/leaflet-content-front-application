package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagListDataModel;
import hu.psprog.leaflet.bridge.client.domain.OrderBy;
import hu.psprog.leaflet.bridge.client.domain.OrderDirection;
import hu.psprog.leaflet.lcfa.core.domain.CallType;
import hu.psprog.leaflet.lcfa.core.domain.content.request.FilteredPaginationContentRequest;
import hu.psprog.leaflet.lcfa.core.domain.raw.HomePageRawResponseWrapper;

import java.util.Map;

/**
 * Abstract {@link AbstractFilteringSupportParallelContentRequestAdapter} implementation that provides the common combinator implementation
 * for every ContentRequestAdapter implementation that serves filtered entry list pages. Also adds mapper for order by and order direction parameters.
 *
 * @author Peter Smith
 */
abstract class AbstractFilteredEntryPageContentRequestAdapter<P> extends AbstractFilteringSupportParallelContentRequestAdapter<HomePageRawResponseWrapper, P> {

    @Override
    HomePageRawResponseWrapper combinator(Map<CallType, BaseBodyDataModel> result) {
        return HomePageRawResponseWrapper.builder()
                .categoryListDataModel((CategoryListDataModel) result.get(CallType.CATEGORY))
                .wrappedEntryListDataModel((WrapperBodyDataModel<EntryListDataModel>) result.get(CallType.ENTRY))
                .wrappedTagListDataModel((WrapperBodyDataModel<TagListDataModel>) result.get(CallType.TAG))
                .build();
    }

    OrderBy.Entry mapOrdering(FilteredPaginationContentRequest<?, ?> paginatedContentRequest) {
        return OrderBy.Entry.valueOf(paginatedContentRequest.getOrderBy().name());
    }

    OrderDirection mapOrderDirection(FilteredPaginationContentRequest<?, ?> paginatedContentRequest) {
        return OrderDirection.valueOf(paginatedContentRequest.getOrderDirection().name());
    }
}
