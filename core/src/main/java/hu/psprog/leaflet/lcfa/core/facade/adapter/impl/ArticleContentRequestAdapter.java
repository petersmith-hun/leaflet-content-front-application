package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.EntryBridgeService;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * {@link ContentRequestAdapter} implementation to retrieve an article for rendering.
 *
 * @author Peter Smith
 */
@Component
public class ArticleContentRequestAdapter implements ContentRequestAdapter<WrapperBodyDataModel<ExtendedEntryDataModel>, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleContentRequestAdapter.class);

    private EntryBridgeService entryBridgeService;

    @Autowired
    public ArticleContentRequestAdapter(EntryBridgeService entryBridgeService) {
        this.entryBridgeService = entryBridgeService;
    }

    @Override
    public Optional<WrapperBodyDataModel<ExtendedEntryDataModel>> getContent(String contentRequestParameter) {

        WrapperBodyDataModel<ExtendedEntryDataModel> response = null;
        try {
            response = entryBridgeService.getEntryByLink(contentRequestParameter);
        } catch (CommunicationFailureException e) {
            LOGGER.error("Failed to retrieve article, identifier by [{}]", contentRequestParameter, e);
        }

        return Optional.ofNullable(response);
    }
}
