package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.DefaultNonSuccessfulResponseException;
import hu.psprog.leaflet.bridge.service.DocumentBridgeService;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * {@link ContentRequestAdapter} implementation to retrieve static page content.
 * Requests a document by its link.
 *
 * @author Peter Smith
 */
@Component
public class StaticPageContentRequestAdapter implements ContentRequestAdapter<WrapperBodyDataModel<DocumentDataModel>, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StaticPageContentRequestAdapter.class);

    private final DocumentBridgeService documentBridgeService;

    @Autowired
    public StaticPageContentRequestAdapter(DocumentBridgeService documentBridgeService) {
        this.documentBridgeService = documentBridgeService;
    }

    @Override
    public Optional<WrapperBodyDataModel<DocumentDataModel>> getContent(String contentRequestParameter) {

        WrapperBodyDataModel<DocumentDataModel> response = null;
        try {
            response = documentBridgeService.getDocumentByLink(contentRequestParameter);
        } catch (DefaultNonSuccessfulResponseException | CommunicationFailureException e) {
            LOGGER.error("Failed to retrieve static page content for link [{}]", contentRequestParameter, e);
        }

        return Optional.ofNullable(response);
    }

    @Override
    public ContentRequestAdapterIdentifier getIdentifier() {
        return ContentRequestAdapterIdentifier.STATIC_PAGE;
    }
}
