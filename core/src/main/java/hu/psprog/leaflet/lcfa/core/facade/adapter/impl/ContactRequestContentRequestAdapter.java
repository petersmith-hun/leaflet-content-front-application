package hu.psprog.leaflet.lcfa.core.facade.adapter.impl;

import hu.psprog.leaflet.api.rest.request.contact.ContactRequestModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.DefaultNonSuccessfulResponseException;
import hu.psprog.leaflet.bridge.service.ContactBridgeService;
import hu.psprog.leaflet.lcfa.core.converter.ContactRequestModelConverter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapter;
import hu.psprog.leaflet.lcfa.core.facade.adapter.ContentRequestAdapterIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * {@link ContentRequestAdapter} implementation to process contact requests.
 *
 * @author Peter Smith
 */
@Component
public class ContactRequestContentRequestAdapter implements ContentRequestAdapter<Boolean, hu.psprog.leaflet.lcfa.core.domain.request.ContactRequestModel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactRequestContentRequestAdapter.class);

    private ContactBridgeService contactBridgeService;
    private ContactRequestModelConverter contactRequestModelConverter;

    @Autowired
    public ContactRequestContentRequestAdapter(ContactBridgeService contactBridgeService, ContactRequestModelConverter contactRequestModelConverter) {
        this.contactBridgeService = contactBridgeService;
        this.contactRequestModelConverter = contactRequestModelConverter;
    }

    @Override
    public Optional<Boolean> getContent(hu.psprog.leaflet.lcfa.core.domain.request.ContactRequestModel contentRequestParameter) {

        ContactRequestModel contactRequestModel = contactRequestModelConverter.convert(contentRequestParameter);
        Boolean success = null;
        try {
            contactBridgeService.sendContactRequest(contactRequestModel, contentRequestParameter.getRecaptchaToken());
            success = true;
        } catch (DefaultNonSuccessfulResponseException | CommunicationFailureException e) {
            LOGGER.error("Failed to process contact request", e);
        }

        return Optional.ofNullable(success);
    }

    @Override
    public ContentRequestAdapterIdentifier getIdentifier() {
        return ContentRequestAdapterIdentifier.CONTACT_REQUEST;
    }
}
