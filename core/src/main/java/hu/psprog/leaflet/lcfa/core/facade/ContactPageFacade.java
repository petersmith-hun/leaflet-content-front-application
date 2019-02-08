package hu.psprog.leaflet.lcfa.core.facade;

import hu.psprog.leaflet.lcfa.core.domain.content.ContactPageContent;
import hu.psprog.leaflet.lcfa.core.domain.request.ContactRequestModel;

/**
 * Facade for contact page related operations.
 *
 * @author Peter Smith
 */
public interface ContactPageFacade {

    /**
     * Retrieves necessary information to show on contact page.
     *
     * @return populated {@link ContactPageContent} object
     */
    ContactPageContent getContactPageContent();

    /**
     * Processes a contact request represented as the given {@link ContactRequestModel}.
     *
     * @param contactRequestModel {@link ContactRequestModel} to be processed
     */
    void processContactRequest(ContactRequestModel contactRequestModel);
}
