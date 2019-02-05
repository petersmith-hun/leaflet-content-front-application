package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.bridge.client.domain.OrderBy;
import hu.psprog.leaflet.bridge.client.domain.OrderDirection;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.DefaultNonSuccessfulResponseException;
import hu.psprog.leaflet.bridge.service.EntryBridgeService;
import hu.psprog.leaflet.lcfa.core.domain.common.CommonPageData;
import hu.psprog.leaflet.lcfa.core.domain.content.request.PaginatedContentRequest;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * {@link ContentRequestAdapter} implementation to retrieve information for populating {@link CommonPageData}.
 * Requests public list of entries for the latest entries, which also returns the default SEO information and front-end menus.
 *
 * @author Peter Smith
 */
@Component
public class CommonPageDataContentRequestAdapter implements ContentRequestAdapter<WrapperBodyDataModel<EntryListDataModel>, PaginatedContentRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonPageDataContentRequestAdapter.class);

    private EntryBridgeService entryBridgeService;

    @Autowired
    public CommonPageDataContentRequestAdapter(EntryBridgeService entryBridgeService) {
        this.entryBridgeService = entryBridgeService;
    }

    @Override
    public Optional<WrapperBodyDataModel<EntryListDataModel>> getContent(PaginatedContentRequest contentRequestParameter) {

        WrapperBodyDataModel<EntryListDataModel> response = null;
        try {
            response = entryBridgeService.getPageOfPublicEntries(contentRequestParameter.getPage(),
                    contentRequestParameter.getLimit(), mapOrdering(contentRequestParameter), mapOrderDirection(contentRequestParameter));
        } catch (DefaultNonSuccessfulResponseException | CommunicationFailureException e) {
            LOGGER.error("Failed to retrieve common page data", e);
        }

        return Optional.ofNullable(response);
    }

    @Override
    public ContentRequestAdapterIdentifier getIdentifier() {
        return ContentRequestAdapterIdentifier.COMMON_PAGE_DATA;
    }

    private OrderBy.Entry mapOrdering(PaginatedContentRequest paginatedContentRequest) {
        return OrderBy.Entry.valueOf(paginatedContentRequest.getEntryOrderBy().name());
    }

    private OrderDirection mapOrderDirection(PaginatedContentRequest paginatedContentRequest) {
        return OrderDirection.valueOf(paginatedContentRequest.getEntryOrderDirection().name());
    }
}
